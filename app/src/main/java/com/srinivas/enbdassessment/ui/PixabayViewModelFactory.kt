package com.srinivas.enbdassessment.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.srinivas.enbdassessment.data.repositories.PixabayRepository

@Suppress("UNCHECKED_CAST")
class PixabayViewModelFactory(
    private val repository: PixabayRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PixabayViewModel(repository) as T
    }
}