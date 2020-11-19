package am.vrezh.catworld.ui.cats.view.adapter

import am.vrezh.catworld.R
import am.vrezh.catworld.api.models.Cat
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

class CatsAdapter : RecyclerView.Adapter<CatsAdapter.CatsViewHolder>() {

    private var catsList: List<Cat> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.item_cat, parent, false)
        return CatsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CatsViewHolder, position: Int) {
        val cat = catsList[position]
        holder.bind(cat)
    }

    override fun getItemCount(): Int = catsList.size

    fun setCatsList(catsList: List<Cat>) {
        this.catsList = catsList
        notifyDataSetChanged()
    }

    class CatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(cat: Cat) {

            Glide.with(itemView)
                .load(cat.imageUrl)
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

            itemView.like_ib.setOnClickListener {
                itemView.like_ib.setImageResource(R.drawable.ic_favorite_filled)
            }

        }

    }

}