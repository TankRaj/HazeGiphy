package com.hazetest.hazegiphy.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hazetest.hazegiphy.R
import com.hazetest.hazegiphy.data.db.FavoriteGifDatabase
import com.hazetest.hazegiphy.data.db.entity.FavoriteGif
import com.hazetest.hazegiphy.databinding.GiphyListItemBinding
import com.hazetest.hazegiphy.utils.FavoriteDiffUtilCallback
import com.hazetest.hazegiphy.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteGifListAdapter(
    private val mainViewModel: MainViewModel,
    private val context: Context
) : ListAdapter<FavoriteGif, FavoriteGifListAdapter.FavoriteGifListViewHolder>(
    FavoriteDiffUtilCallback()
) {
    val db = FavoriteGifDatabase.getInstance(context.applicationContext)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteGifListViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<GiphyListItemBinding>(
            layoutInflater,
            R.layout.giphy_list_item,
            parent,
            false
        )
        return FavoriteGifListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteGifListViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
        holder.binding.heartBtn.setImageResource(R.drawable.heart)
        Glide.with(context).asGif().load(item.url).into(holder.binding.gifImgView)
    }

    private fun deleteFavoriteGif(id: String) {
        db!!.favoriteGifDao()
            .favoriteGifDelete(id)
    }

    private fun heartBtnClick(item: FavoriteGif, binding: GiphyListItemBinding) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                deleteFavoriteGif(id = item.gifId)
            }

            val data = withContext(Dispatchers.IO) {
                db!!.favoriteGifDao().favoriteGifAllSelectAndGet()
            }

            mainViewModel.callAdapterDataReset(data)
            binding.heartBtn.setImageResource(R.drawable.heart_thin)
        }
    }

    inner class FavoriteGifListViewHolder(val binding: GiphyListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FavoriteGif) {
            binding.heartBtn.setOnClickListener {
                heartBtnClick(item, binding)
            }
        }
    }
}