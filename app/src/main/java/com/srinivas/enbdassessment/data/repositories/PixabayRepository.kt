package com.srinivas.enbdassessment.data.repositories

import com.srinivas.enbdassessment.data.db.AppDataBase
import com.srinivas.enbdassessment.data.network.ApiInterface
import com.srinivas.enbdassessment.data.network.SafeApiRequest
import com.srinivas.enbdassessment.data.db.entities.Hit
import com.srinivas.enbdassessment.util.Constants

class PixabayRepository(

    private val api: ApiInterface,
    private val db: AppDataBase
) : SafeApiRequest() {

    suspend fun getImages(query: String, pageNumber: Int) =
        apiRequest {
            api.getImageResults(
                Constants.PIX_BAY_API_KEY,
                query,
                pageNumber,
                Constants.QUERY_PAGE_SIZE
            )
        }

    /**
     * Save Images in image_table
     */
    suspend fun saveImages(images: List<Hit>) = db.getPixabayImageDao().upsert(images)

    /**
     * return Saved Images from image_table
     */
    fun getImageData(query: String) = db.getPixabayImageDao().getImages(query)

    /**
     * delete saved images if any exists.
     */
    suspend fun deleteImageData() = db.getPixabayImageDao().deleteAllImages()
}