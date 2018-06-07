package com.droibit.moshimoshi.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class Author(
  val id: String = "none",
  val name: String = ""
)

data class NamedAuthor(
  @Json(name = "authorId") val id: String,
  val name: String
)

@JsonClass(generateAdapter = true)
data class CodegenAuthor(
  val id: String,
  val name: String? // null means unknown author
)