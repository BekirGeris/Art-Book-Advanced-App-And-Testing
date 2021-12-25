package com.begers.artbooktesting.repository

import androidx.lifecycle.LiveData
import com.begers.artbooktesting.api.RetrofitApi
import com.begers.artbooktesting.model.Art
import com.begers.artbooktesting.model.ImageResponse
import com.begers.artbooktesting.roomdb.ArtDao
import com.begers.artbooktesting.util.Resource
import java.lang.Exception
import javax.inject.Inject

class ArtRepository @Inject constructor(
    private val artDao: ArtDao,
    private val retrofitApi: RetrofitApi
) : ArtRepositoryInterface{

    override suspend fun insertArt(art: Art) {
        artDao.insertArt(art)
    }

    override suspend fun deleteArt(art: Art) {
        artDao.deleteArt(art)
    }

    override fun getAll(): LiveData<List<Art>> {
        return artDao.observeArts()
    }

    override suspend fun searchImage(imageString: String): Resource<ImageResponse> {
        return try {
            val response = retrofitApi.imageSearch(imageString)
            if (response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Error!", null)
            }else{
                Resource.error("Error!", null)
            }
        }catch (e: Exception){
            Resource.error("no data!", null)
        }
    }

}