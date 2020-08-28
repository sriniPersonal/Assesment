package com.srinivas.enbdassessment.util

import android.content.Context

class Constants {

    companion object{
        const val PIX_BAY_API_KEY:String="18069779-859ee62e1c9ab5d300e865cab"
        const val BASE_URL = "https://pixabay.com"
        const val SEARCH_IMAGES_TIME_DELAY = 500L
        const val QUERY_PAGE_SIZE = 20

        fun initStetho(context: Context?) {
                try {
                    val stethoClazz =
                        Class.forName("com.facebook.stetho.Stetho")
                    val method = stethoClazz.getMethod(
                        "initializeWithDefaults",
                        Context::class.java
                    )
                    method.invoke(null, context)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

        }
    }
}