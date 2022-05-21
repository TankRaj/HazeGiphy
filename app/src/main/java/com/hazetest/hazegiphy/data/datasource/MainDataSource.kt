package com.hazetest.hazegiphy.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hazetest.hazegiphy.data.remote.api.GiphyApi
import com.hazetest.hazegiphy.data.remote.model.Data

class MainDataSource(
    private val giphyApi: GiphyApi
): PagingSource<Int, Data>() {

    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        return try {
            val page = params.key?: 1
            val results = giphyApi.getTrendingGiphyGifs(offset = page*20)
            val resultsDataList = mutableListOf<Data>()
            val resultsData = results.body()?.data ?: emptyList()
            resultsDataList.addAll(resultsData)
            LoadResult.Page(data = resultsData, nextKey = page.plus(1), prevKey = if (page == 1) null else -1)
        }
        catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}