package com.hazetest.hazegiphy.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hazetest.hazegiphy.data.datasource.MainDataSource
import com.hazetest.hazegiphy.data.datasource.SearchDataSource
import com.hazetest.hazegiphy.data.remote.api.GiphyApi
import com.hazetest.hazegiphy.data.remote.model.Data
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val giphyApi: GiphyApi
) {

    fun getGiphyGifs(queryString:String?=null): Flow<PagingData<Data>> {
        return Pager(
            PagingConfig(pageSize = 20)) {
            MainDataSource(giphyApi)
        }.flow
    }
    fun searchGiphyGifs(queryString:String): Flow<PagingData<Data>> {
        return Pager(
            PagingConfig(pageSize = 20)) {
            SearchDataSource(giphyApi, queryString)
        }.flow
    }

}