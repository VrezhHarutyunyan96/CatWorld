package am.vrezh.catworld.api.models

import java.io.Serializable

data class Cat(
    val image: String,
    val title: String,
    val year: String,
    val text: String,
    val color: String
) : Serializable

data class Cats(var elements: List<Cat>)
