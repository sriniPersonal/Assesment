package com.srinivas.enbdassessment.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srinivas.enbdassessment.data.db.entities.Hit
import com.srinivas.enbdassessment.data.network.models.PixabayResponse
import com.srinivas.enbdassessment.data.repositories.PixabayRepository
import com.srinivas.enbdassessment.util.ApiException
import com.srinivas.enbdassessment.util.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PixabayViewModel(
    private val repository: PixabayRepository
) : ViewModel() {
    val searchImages: MutableLiveData<Resource<PixabayResponse>> = MutableLiveData()
    val localImages: MutableLiveData<List<Hit>> = MutableLiveData()
    var searchImagesPage = 1
    var searchImagesResponse: PixabayResponse? = null
    var previousQuery: String? = null

    init {
        initializeSearchImages()
        getImages("apple")
    }

    fun initializeSearchImages() {
        searchImagesPage = 1;
        searchImagesResponse = null
    }

    fun getImages(searchQuery: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            searchImages.postValue(Resource.Loading())
            val response = repository.getImages(searchQuery, searchImagesPage)
            searchImages.postValue(handleSearchNewsResponse(response, searchQuery))
        } catch (e: ApiException) {
            getLocalData(searchQuery);
        } catch (e: NoInternetException) {
            getLocalData(searchQuery);
        }
    }

    private fun getLocalData(query: String) {
        val imageList: List<Hit> = getSavedImages(query)
        if (imageList.isNotEmpty()) {
            localImages.postValue(imageList)
        } else {
            searchImages.postValue(Resource.Error("No Internet connection Please check"))
        }
    }

    private fun handleSearchNewsResponse(
        response: PixabayResponse,
        query: String
    ): Resource<PixabayResponse> {
        if (response.hits.size <= 0 && searchImagesPage == 1) {
            return Resource.Error("No Content Found")
        } else {
            previousQuery = query
            if (searchImagesPage == 1) {
                deleteSavedImages()
            }
            searchImagesPage++
            if (searchImagesResponse == null) {
                searchImagesResponse = response
                saveImages(response.hits)
            } else {
                val oldArticles = searchImagesResponse?.hits
                val newArticles = response.hits
                oldArticles?.addAll(newArticles)
                if (oldArticles != null) {
                    deleteSavedImages()
                    saveImages(oldArticles)
                }
            }
            return Resource.Success(searchImagesResponse ?: response)
        }

    }

    private fun saveImages(images: List<Hit>) = viewModelScope.launch {
        repository.saveImages(images)
    }

    fun getSavedImages(query: String) = repository.getImageData(query)

    private fun deleteSavedImages() = repository.deleteImageData()

    override fun onCleared() {
        super.onCleared()
    }

}