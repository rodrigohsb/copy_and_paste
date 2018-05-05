package br.com.rodrigohsb.challenge.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.rodrigohsb.joao_challenge.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_details_item.view.*

/**
 * @rodrigohsb
 */
class MyImageDetailsAdapter(private val urls: MutableList<String>)
    : RecyclerView.Adapter<MyImageDetailsAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.image_details_item, parent, false))
    }

    override fun getItemCount(): Int {
        return urls.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val url = urls[position]

        Picasso
            .get()
            .load(url)
            .into(holder.itemView.image)

    }
}