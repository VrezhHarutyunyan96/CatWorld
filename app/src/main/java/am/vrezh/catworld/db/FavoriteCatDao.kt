package am.vrezh.catworld.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteCatDao {

    @Query("SELECT * FROM favoritecat")
    fun getAll(): List<FavoriteCat>

    @Insert
    fun insert(favoriteCat: FavoriteCat)

    @Insert
    fun insertAll(favoriteCats: List<FavoriteCat>)

    @Delete
    fun delete(favoriteCat: FavoriteCat)

}