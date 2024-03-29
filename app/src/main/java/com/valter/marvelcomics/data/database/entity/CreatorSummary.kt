package com.valter.marvelcomics.data.database.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreatorSummary(
    val name: String,
    val resourceURI: String,
    val role: String
)