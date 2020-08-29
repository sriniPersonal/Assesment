package com.srinivas.enbdassessment.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.srinivas.enbdassessment.data.db.AppDataBase
import com.srinivas.enbdassessment.data.network.ApiInterface
import com.srinivas.enbdassessment.data.network.models.PixabayResponse
import com.srinivas.enbdassessment.data.repositories.PixabayRepository
import com.srinivas.enbdassessment.util.Constants
import com.srinivas.enbdassessment.util.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PixabayViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var apiHelper: ApiInterface


    private lateinit var repository: PixabayRepository

    @Mock
    private lateinit var appDataBase: AppDataBase

    @Mock
    private lateinit var apiUsersObserver: Observer<Resource<PixabayResponse>>
    @Before
    fun setUp() {

    }
  /*  @Test
    fun givenServerResponse200_whenFetch_shouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            doReturn(null)
                .`when`(repository)
                .getImages("apple",1)
            val viewModel = PixabayViewModel(repository)
            viewModel.searchImages.observeForever(apiUsersObserver)
            verify(apiUsersObserver).onChanged(Resource.Success(verify(repository).getImages("apple",1)))
        }
    }*/






}