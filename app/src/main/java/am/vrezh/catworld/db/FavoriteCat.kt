package am.vrezh.catworld.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteCat(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "breed_name") val breedName: String?,
    @ColumnInfo(name = "image_url") val imageUrl: String?
)