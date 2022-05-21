package com.hazetest.hazegiphy.data.remote.api

import com.hazetest.hazegiphy.data.remote.model.GiphyResponse
import com.hazetest.hazegiphy.utils.Utils.GIPHY_API_KEY
import com.hazetest.hazegiphy.utils.Utils.SEARCH_END_URL
import com.hazetest.hazegiphy.utils.Utils.TRENDING_END_URL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApi {

    @GET(TRENDING_END_URL)
    suspend fun getTrendingGiphyGifs(
        @Query("api_key") key: String = GIPHY_API_KEY,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int,
        @Query("lang") lang: String = "en",
        @Query("bundle") bundle: String = "clips_grid_picker"
    ): Response<GiphyResponse>

    @GET(SEARCH_END_URL)
    suspend fun searchGiphyGifByQueryString(
        @Query("api_key") key: String = GIPHY_API_KEY,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int,
        @Query("lang") lang: String = "en",
        @Query("q") queryString: String,
        @Query("bundle") bundle: String = "clips_grid_picker"
    ): Response<GiphyResponse>
}