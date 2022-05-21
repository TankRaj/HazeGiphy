package com.hazetest.hazegiphy.view

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.hazetest.hazegiphy.R
import com.hazetest.hazegiphy.adapter.FavoriteGifListAdapter
import com.hazetest.hazegiphy.data.db.FavoriteGifDatabase
import com.hazetest.hazegiphy.databinding.FragmentFavoriteBinding
import com.hazetest.hazegiphy.extension.showGrid
import com.hazetest.hazegiphy.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(R.layout.fragment_favorite) {
    private val mainViewModel by activityViewModels<MainViewModel>()
    private lateinit var favAdapter : FavoriteGifListAdapter


    override fun init() {
        favAdapter = FavoriteGifListAdapter(mainViewModel, requireContext())
        observeViewModel()
        getFavoriteGifList()
    }

    private fun getFavoriteGifList(){
        val db = FavoriteGifDatabase.getInstance(requireContext())
        lifecycleScope.launch(Main) {
            withContext(IO){
               mainViewModel.favoriteGifList.postValue(db!!.favoriteGifDao().favoriteGifAllSelectAndGet())
            }
            initRecyclerView()
        }
    }

    private fun observeViewModel(){
        mainViewModel.favoriteGifList.observe(this, Observer{
            favAdapter.submitList(it)
        })
    }

    private fun initRecyclerView(){
        binding.favoriteRecyclerView.apply {
            adapter= favAdapter
            showGrid(requireContext())
            favAdapter.submitList(mainViewModel.favoriteGifList.value)
            setHasFixedSize(true)
        }
    }
}