package com.example.galleryapp.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Image(
	val author: String? = null,
	val width: Int? = null,
	@SerialName("download_url")
	val downloadUrl: String? = null,
	val id: String? = null,
	val url: String? = null,
	val height: Int? = null
)

