package am.vrezh.catworld.api.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Cat(
    val id: String,
    @SerializedName("url")
    val imageUrl: String,
    val breeds: ArrayList<Breed>,
    val height: Int,
    val width: Int
) : Serializable

data class Breed(
    val id: String,
    val name: String
) : Serializable