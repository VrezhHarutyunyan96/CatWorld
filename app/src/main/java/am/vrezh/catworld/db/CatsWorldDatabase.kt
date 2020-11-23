package am.vrezh.catworld.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteCat::class], version = 3)
abstract class CatsWorldDatabase : RoomDatabase() {
    abstract fun favoriteCatDao(): FavoriteCatDao
}