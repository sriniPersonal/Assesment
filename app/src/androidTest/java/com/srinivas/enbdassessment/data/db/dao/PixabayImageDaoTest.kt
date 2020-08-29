package com.srinivas.enbdassessment.data.db.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.srinivas.enbdassessment.data.FakeImageData
import com.srinivas.enbdassessment.data.db.AppDataBase
import com.srinivas.enbdassessment.data.db.entities.Hit
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class PixabayImageDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDataBase
    private lateinit var dao: PixabayImageDao
    private val LIST_ITEM_IN_TEST = 1
    private val IMAGE_IN_TEST =FakeImageData.images[2]

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDataBase::class.java
        ).allowMainThreadQueries().build()
        dao = database.getPixabayImageDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertImageItem() = runBlockingTest {
        val imageList = FakeImageData.images
        dao.upsert(imageList)

        val allImages = dao.getImages("apple")

        assertThat(allImages).contains(IMAGE_IN_TEST)
    }

    @Test
    fun deleteAllImages() = runBlockingTest {
        val imageList = FakeImageData.images
        dao.upsert(imageList)
        dao.deleteAllImages()
        val allImages = dao.getImages("apple")
        assertThat(allImages).doesNotContain(IMAGE_IN_TEST)
    }

    fun checkOfflineItems()= runBlockingTest {
        val imageList = FakeImageData.images
        dao.upsert(imageList)

    }


}