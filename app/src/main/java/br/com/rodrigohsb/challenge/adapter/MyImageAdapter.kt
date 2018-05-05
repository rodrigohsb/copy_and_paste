package br.com.rodrigohsb.challenge.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.rodrigohsb.challenge.entry.Photo
import br.com.rodrigohsb.joao_challenge.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_item.view.*

/**
 * @rodrigohsb
 */
class MyImageAdapter(private val photos: MutableList<Photo>): RecyclerView.Adapter<MyImageAdapter.ViewHolder>() {
    
    companion object {
        const val TYPE_IMAGE = 0
        const val TYPE_FOOTER = 1
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.image_item, parent, false))
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionFooter(position))
            TYPE_FOOTER
               else
            TYPE_IMAGE
    }

    private fun isPositionFooter(position: Int): Boolean {
        return position == photos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val photo = photos[position]

        Picasso
            .get()
            .load(photo.smallUrl)
            .into(holder.itemView.image)
    }

    fun appendImages(newPhotos: List<Photo>) {
        photos.addAll(newPhotos)
        notifyDataSetChanged()
    }
}