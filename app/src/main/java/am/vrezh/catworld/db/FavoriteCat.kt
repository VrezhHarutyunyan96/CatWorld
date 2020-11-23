package am.vrezh.catworld.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteCat(
    @ColumnInfo(name = "image_local_url") val imageLocalUrl: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
