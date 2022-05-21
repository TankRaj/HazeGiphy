package com.hazetest.hazegiphy.view

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.hazetest.hazegiphy.R
import com.hazetest.hazegiphy.adapter.GiphyListAdapter
import com.hazetest.hazegiphy.databinding.FragmentMainBinding
import com.hazetest.hazegiphy.extension.showVertical
import com.hazetest.hazegiphy.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private val mainViewModel by activityViewModels<MainViewModel>()
    private lateinit var giphyListAdapter: GiphyListAdapter

    override fun init() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        giphyListAdapter = GiphyListAdapter(requireContext(), this, mainViewModel)
        binding.recyclerView.apply {
            adapter = giphyListAdapter
            showVertical(requireContext())
            setHasFixedSize(true)
        }
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank())
                    getTrendingGifs()
                else
                    searchGifs(s.toString())
            }
        })
        getTrendingGifs()
    }

    private fun getTrendingGifs() {

        lifecycleScope.launch {
            mainViewModel.getGiphyGifs().collect {
                giphyListAdapter.submitData(it)
            }
        }
    }

    private fun searchGifs(queryString: String) {
        lifecycleScope.launch {
            mainViewModel.searchGiphyGifs(queryString).collect {
                giphyListAdapter.submitData(it)
            }

        }
    }
}