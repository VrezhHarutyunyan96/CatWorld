package am.vrezh.catworld.ui.favorites.view

import android.os.Bundle
import androidx.fragment.app.Fragment

class FavoriteCatsFragment : Fragment() {

    companion object {

        @JvmStatic
        fun newInstance() = FavoriteCatsFragment().apply {
            arguments = Bundle().apply {}
        }

    }

}