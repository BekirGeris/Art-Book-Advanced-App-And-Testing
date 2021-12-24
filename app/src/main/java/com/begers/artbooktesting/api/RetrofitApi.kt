package com.begers.artbooktesting.api

import com.begers.artbooktesting.model.ImageResponse
import com.begers.artbooktesting.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {

    @GET("/api/")
    suspend fun imageSearch(
        @Query("q") searchQuery: String,
        @Query("key") key: String = API_KEY
    ): Response<ImageResponse>

}