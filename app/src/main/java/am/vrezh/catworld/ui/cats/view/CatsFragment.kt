package am.vrezh.catworld.ui.cats.view

import android.os.Bundle
import androidx.fragment.app.Fragment

class CatsFragment : Fragment() {

    companion object {

        @JvmStatic
        fun newInstance() = CatsFragment().apply {
            arguments = Bundle().apply {}
        }

    }

}