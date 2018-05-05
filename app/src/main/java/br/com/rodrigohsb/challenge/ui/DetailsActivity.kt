package br.com.rodrigohsb.challenge.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.view.View
import br.com.rodrigohsb.challenge.adapter.MyImageDetailsAdapter
import br.com.rodrigohsb.challenge.entry.Photo
import br.com.rodrigohsb.challenge.entry.PhotoDetails
import br.com.rodrigohsb.joao_challenge.R
import kotlinx.android.synthetic.main.activity_details.*

/**
 * @rodrigohsb
 */
class DetailsActivity: AppCompatActivity() {

    companion object {
        fun start(activity: Activity, position: Int, photoDetails: ArrayList<PhotoDetails>) {

            val intent = Intent(activity,DetailsActivity::class.java)

            val bundle = Bundle()
            bundle.putParcelableArrayList("photoDetails", photoDetails)
            bundle.putInt("position",position)

            activity.startActivity(intent.putExtras(bundle))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val listPhotoDetails = intent.extras.getParcelableArrayList<PhotoDetails>("photoDetails")
        val position = intent.extras.getInt("position")

        val items = arrayListOf<String>()

        for(item in listPhotoDetails){
            items.add(item.url)
        }

        val myAdapter = MyImageDetailsAdapter(items)

        LinearSnapHelper().attachToRecyclerView(detailsRecyclerView)

        with(detailsRecyclerView){
            adapter = myAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DetailsActivity, LinearLayoutManager.HORIZONTAL, false)
            visibility = View.VISIBLE
        }
        detailsRecyclerView.layoutManager.scrollToPosition(position)
    }
}