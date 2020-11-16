package am.vrezh.catworld.ui.cats.view.adapter

import am.vrezh.catworld.R
import am.vrezh.catworld.api.models.Cat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CatsAdapter : RecyclerView.Adapter<CatsAdapter.CatsViewHolder>() {

    public var catsList: List<Cat> = listOf()

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

    class CatsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(cat: Cat) {

        }

    }

}