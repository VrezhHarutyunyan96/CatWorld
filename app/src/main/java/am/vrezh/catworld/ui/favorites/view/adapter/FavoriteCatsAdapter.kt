package am.vrezh.catworld.ui.favorites.view.adapter

import am.vrezh.catworld.R
import am.vrezh.catworld.db.FavoriteCat
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.item_cat.view.*

class FavoriteCatsAdapter :
    RecyclerView.Adapter<FavoriteCatsAdapter.FavoriteCatsViewHolder>() {

    private var favoriteCatsList: MutableList<FavoriteCat> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteCatsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.item_cat, parent, false)
        return FavoriteCatsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavoriteCatsViewHolder, position: Int) {
        val favoriteCat = favoriteCatsList[position]
        holder.bind(favoriteCat)
    }

    override fun getItemCount(): Int = favoriteCatsList.size

    fun addData(favoriteCatsList: List<FavoriteCat>) {
        this.favoriteCatsList.clear()
        this.favoriteCatsList.addAll(favoriteCatsList)
        notifyDataSetChanged()
    }

    inner class FavoriteCatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(favoriteCat: FavoriteCat) {

            Glide.with(itemView)
                .load(favoriteCat.imageLocalUrl)
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        itemView.cat_pb.visibility = View.GONE
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        itemView.cat_pb.visibility = View.GONE
                        return false
                    }
                })
                .into(itemView.cat_iv)

        }

    }

}