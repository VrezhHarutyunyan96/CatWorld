package am.vrezh.catworld.api.models

import java.io.Serializable

data class Cat(
    val id: String,
    val name: String,
    val temperament: String
) : Serializable

data class Cats(var elements: List<Cat>)
