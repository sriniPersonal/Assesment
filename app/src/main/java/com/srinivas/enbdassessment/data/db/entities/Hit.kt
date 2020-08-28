package com.srinivas.enbdassessment.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "images_table")
data class Hit(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val comments: Int,
    val downloads: Int,
    val favorites: Int,
    val largeImageURL: String,
    val likes: Int,
    val previewURL: String,
    val tags: String,
    val type: String,
    val user: String,
    val userImageURL: String,
    val user_id: Int,
    val views: Int
):Serializable