package com.droibit.moshimoshi.entity

import com.squareup.moshi.Json

/**
 * @author kumagai
 */
data class Author(val id: String, val name: String)

data class NamedAuthor(@Json(name = "authorId") val id: String, val name: String)