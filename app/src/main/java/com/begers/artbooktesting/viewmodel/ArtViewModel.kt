package com.begers.artbooktesting.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.begers.artbooktesting.model.Art
import com.begers.artbooktesting.model.ImageResponse
import com.begers.artbooktesting.repository.ArtRepositoryInterface
import com.begers.artbooktesting.util.Resource
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class ArtViewModel @Inject constructor(
    private var repository: ArtRepositoryInterface
) : ViewModel() {

    val artList = repository.getAll()

    private val images = MutableLiveData<Resource<ImageResponse>>()
    val imageList: LiveData<Resource<ImageResponse>>
        get() = images

    private val selectedImage = MutableLiveData<String>()
    val selectedImageUrl: LiveData<String>
        get() = selectedImage

    private var insertArtMessage = MutableLiveData<Resource<Art>>()
    val insertArtMsg: LiveData<Resource<Art>>
        get() = insertArtMessage

    fun resetArtMessage() {
        insertArtMessage = MutableLiveData<Resource<Art>>()
    }

    fun setSelectedImage(url: String) {
        selectedImage.postValue(url)
    }

    fun deleteArt(art: Art) = viewModelScope.launch {
        repository.deleteArt(art)
    }

    fun insertArt(art: Art) = viewModelScope.launch {
        repository.insertArt(art)
    }

    fun searchImage(searchString: String) {
        if (searchString.isEmpty()){
            return
        }

        images.value = Resource.loading(null)

        viewModelScope.launch {
            val response = repository.searchImage(imageString = searchString)
            images.value = response
        }
    }

    fun makeArt(name: String, artistName: String,year: String){
        if (name.isEmpty() || artistName.isEmpty() || year.isEmpty()){
            insertArtMessage.postValue(Resource.error("Enter name, artits name, year", null))
            return
        }

        val yearInt = try {
            year.toInt()
        }catch (e: Exception){
            insertArtMessage.postValue(Resource.error("Year should be number", null))
            return
        }

        val art = Art(name, artistName, yearInt,  selectedImage.value ?: "")
        insertArt(art)
        setSelectedImage("")
        insertArtMessage.postValue(Resource.success(art))
    }


}