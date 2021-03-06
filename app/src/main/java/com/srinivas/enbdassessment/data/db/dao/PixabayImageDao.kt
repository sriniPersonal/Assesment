package com.srinivas.enbdassessment.data.db.dao

import androidx.room.*
import com.srinivas.enbdassessment.data.db.entities.Hit

@Dao
interface PixabayImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(images: kotlin.collections.List<com.srinivas.enbdassessment.data.db.entities.Hit>)

    // return result set where query matches with tags column
    @Query("SELECT * FROM images_table WHERE tags LIKE '%' || :query || '%'")
    fun getImages(query:String): List<Hit>

    @Query("Delete from images_table")
    suspend fun deleteAllImages()
}