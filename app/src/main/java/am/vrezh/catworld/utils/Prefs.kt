package am.vrezh.catworld.utils

import android.content.Context
import android.content.SharedPreferences

class Prefs {

    companion object {

        private const val PREFS_FILE = "cat_world_prefs"
        private const val FAVORITE_CHANGED = "favorite_changed"

        private lateinit var prefs: SharedPreferences

        fun init(context: Context) {
            prefs = getPrefs(context.applicationContext);
        }

        private fun getPrefs(context: Context): SharedPreferences {
            return context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)
        }

        fun getFavoriteChanged(): Boolean {
            return prefs.getBoolean(FAVORITE_CHANGED, false)
        }

        fun setFavoriteChanged(favoriteChanged: Boolean) {
            prefs.edit().putBoolean(FAVORITE_CHANGED, favoriteChanged).apply()
        }

    }

}