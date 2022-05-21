package com.hazetest.hazegiphy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hazetest.hazegiphy.R
import com.hazetest.hazegiphy.data.db.FavoriteGifDatabase
import com.hazetest.hazegiphy.data.db.entity.FavoriteGif
import com.hazetest.hazegiphy.data.remote.model.Data
import com.hazetest.hazegiphy.databinding.GiphyListItemBinding
import com.hazetest.hazegiphy.utils.GiphyDiffUtilCallback
import com.hazetest.hazegiphy.viewmodel.MainViewModel
import kotlinx.coroutines.*


class GiphyListAdapter(
    private val context: Context,
    private val fragment: Fragment,
    private val mainViewModel: MainViewModel
) :
    PagingDataAdapter<Data, GiphyListAdapter.GiphyListViewHolder>(GiphyDiffUtilCallback()) {
    val db = FavoriteGifDatabase.getInstance(context.applicationContext)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GiphyListAdapter.GiphyListViewHolder {
        return GiphyListViewHolder(
            GiphyListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: GiphyListViewHolder, position: Int) {
        val item = getItem(position) ?: return

        observeViewModel(holder, item)

        holder.bind(item)

        CoroutineScope(Dispatchers.IO).launch {
            checkFavoriteGifInRoom(binding = holder.binding, item = item)
        }

        Glide.with(context).asGif().load(item.images.fixed_width.url)
            .into(holder.binding.gifImgView)
    }

    private fun observeViewModel(holder: GiphyListViewHolder, item: Data) {
        mainViewModel.favoriteGifList.observe(fragment, Observer {
            if (checkFavoriteGifInList(item = item)) holder.binding.heartBtn.setImageResource(R.drawable.heart)
            else holder.binding.heartBtn.setImageResource(R.drawable.heart_thin)
        })
    }

    private fun checkFavoriteGifInList(item: Data): Boolean {
        var checkId = false
        mainViewModel.favoriteGifList.value?.forEach {
            if (it.gifId == item.id) checkId = true
        }
        return checkId
    }

    private suspend fun checkFavoriteGifInRoom(binding: GiphyListItemBinding, item: Data): Boolean {
        val favoriteState = CoroutineScope(Dispatchers.Main).async {
            val gifList = withContext(Dispatchers.IO) {
                db!!.favoriteGifDao().favoriteGifSelect(item.id)
            }
            if (gifList.isNullOrEmpty()) {
                binding.heartBtn.setImageResource(R.drawable.heart_thin)
                false
            } else {
                binding.heartBtn.setImageResource(R.drawable.heart)
                true
            }
        }
        return favoriteState.await()
    }

    private fun insertFavoriteGifInRoom(item: Data) {
        db!!.favoriteGifDao()
            .favoriteGifInsert(FavoriteGif(gifId = item.id, url = item.images.fixed_width.url))
    }

    private fun deleteFavoriteGifInRoom(item: Data) {
        db!!.favoriteGifDao()
            .favoriteGifDelete(item.id)
    }

    private fun heartBtnClick(binding: GiphyListItemBinding, item: Data) {
        CoroutineScope(Dispatchers.IO).launch {

            val favoriteState = withContext(Dispatchers.IO) {
                checkFavoriteGifInRoom(binding = binding, item = item)
            }

            withContext(Dispatchers.IO) {
                if (favoriteState) deleteFavoriteGifInRoom(item = item)
                else insertFavoriteGifInRoom(item = item)
            }

            val data = withContext(Dispatchers.IO) {
                db!!.favoriteGifDao().favoriteGifAllSelectAndGet()
            }

            mainViewModel.callAdapterDataReset(data)

            checkFavoriteGifInRoom(binding = binding, item = item)
        }
    }

    inner class GiphyListViewHolder(val binding: GiphyListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item : Data?){
            binding.heartBtn.setOnClickListener {
                if (item != null) heartBtnClick(
                    binding = binding,
                    item = item
                )
            }
        }
    }
}