package am.vrezh.catworld.api.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Cat(
    val id: String,
    @SerializedName("url")
    val imageUrl: String,
    val categories: ArrayList<Category>,
    val breeds: ArrayList<Breed>
) : Serializable

data class Category(
    val id: Int,
    val name: String
) : Serializable

data class Breed(
    val id: Int,
    val name: String
) : Serializable