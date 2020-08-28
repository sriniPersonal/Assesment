package com.srinivas.enbdassessment.data.network.models

import com.srinivas.enbdassessment.data.db.entities.Hit

data class PixabayResponse(
    val hits: MutableList<Hit>,
    val total: Int,
    val totalHits: Int
)