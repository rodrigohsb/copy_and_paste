package br.com.rodrigohsb.challenge.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import br.com.rodrigohsb.challenge.adapter.MyImageDetailsAdapter
import br.com.rodrigohsb.challenge.extensions.visible
import br.com.rodrigohsb.joao_challenge.R
import kotlinx.android.synthetic.main.activity_details.*

/**
 * @rodrigohsb
 */
class DetailsActivity: AppCompatActivity() {

    companion object {

        private const val DETAILS = "details"
        private const val POSITION = "position"

        fun start(activity: Activity, position: Int, photoDetails: ArrayList<String>) {

            val intent = Intent(activity,DetailsActivity::class.java)

            val bundle = Bundle()
            bundle.putStringArrayList(DETAILS, photoDetails)
            bundle.putInt(POSITION,position)

            activity.startActivity(intent.putExtras(bundle))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val listPhotoDetails = intent.extras.getStringArrayList(DETAILS)
        val position = intent.extras.getInt(POSITION)

        LinearSnapHelper().attachToRecyclerView(detailsRecyclerView)

        with(detailsRecyclerView){
            adapter = MyImageDetailsAdapter(listPhotoDetails)
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DetailsActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false)
            layoutManager.scrollToPosition(position)
            visible()
        }
    }
}