package am.vrezh.catworld.api.models

import java.io.Serializable

data class Cat(
    val id: String,
    val breedName: String,
    val imageUrl: String
) : Serializable

data class Cats(var elements: List<Cat>)
