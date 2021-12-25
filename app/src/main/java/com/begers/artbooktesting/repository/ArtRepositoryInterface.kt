package com.begers.artbooktesting.repository

import androidx.lifecycle.LiveData
import com.begers.artbooktesting.model.Art
import com.begers.artbooktesting.model.ImageResponse
import com.begers.artbooktesting.util.Resource

interface ArtRepositoryInterface {

    suspend fun insertArt(art: Art)

    suspend fun deleteArt(art: Art)

    fun getAll() : LiveData<List<Art>>

    suspend fun searchImage(imageString: String) : Resource<ImageResponse>
}