package com.droibit.moshimoshi.entity

import com.squareup.moshi.Json

data class Author(
  val id: String = "none",
  val name: String = ""
)

data class NamedAuthor(
  @Json(name = "authorId") val id: String,
  val name: String
)