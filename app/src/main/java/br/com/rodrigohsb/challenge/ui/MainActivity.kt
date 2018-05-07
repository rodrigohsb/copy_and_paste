package br.com.rodrigohsb.challenge.ui

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import br.com.rodrigohsb.challenge.decorator.GridDividerItemDecoration
import br.com.rodrigohsb.challenge.adapter.MyImageAdapter
import br.com.rodrigohsb.challenge.entry.Photo
import br.com.rodrigohsb.challenge.State
import br.com.rodrigohsb.challenge.adapter.Listener
import br.com.rodrigohsb.challenge.extensions.gone
import br.com.rodrigohsb.challenge.extensions.visible
import br.com.rodrigohsb.challenge.viewModel.MyViewModel
import br.com.rodrigohsb.challenge.webservice.exceptions.*
import br.com.rodrigohsb.joao_challenge.R
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.android.appKodein
import com.github.salomonbrys.kodein.instance
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var myAdapter: MyImageAdapter

    private var photoList = mutableListOf<Photo>()

    private lateinit var customGridLayoutManager: GridLayoutManager

    private val disposables by lazy { CompositeDisposable() }

    val kodein by lazy {
        LazyKodein(appKodein)
    }

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {

        @Suppress("UNCHECKED_CAST")
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>) = kodein.value.instance<MyViewModel>() as T
        }
        ViewModelProviders.of(this, factory).get(MyViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        disposables += viewModel.listPhotos()
                .subscribe({
                    status: State ->

                    when(status){

                        is State.Loading -> showLoading()

                        is State.Error -> {
                            hideLoading()

                            textError.visibility = View.VISIBLE

                            when(status.exception){
                                is TimeoutException -> textError.text = "TimeoutException"
                                is Error4XXException -> textError.text = "Error4XXException"
                                is NoNetworkException -> textError.text = "NoNetworkException"
                                is BadRequestException -> textError.text = "BadRequestException"
                                is NoDataException -> textError.text = "NoDataException"
                                is Error5XXException -> textError.text = "Error5XXException"
                                else -> textError.text = "GenericError"
                            }
                        }

                        is State.Success -> {
                            hideLoading()
                            showImages(status.photos)
                        }
                    }
                },{
                    t -> t.printStackTrace()
                })

    }

    private fun loadMore(){
        disposables += viewModel.loadMore()
                .subscribe({
                    status: State ->
                    when(status){
                        is State.Error -> {
                            //TODO show message
                        }

                        is State.Success -> appendImages(status.photos)
                    }
                },{
                    t -> t.printStackTrace()
                })
    }

    private fun appendImages(photos: List<Photo>){
        photoList.addAll(photos)
        myAdapter.appendImages(photos)
    }

    private fun showImages(photos: List<Photo>){

        photoList.addAll(photos)

        myAdapter = MyImageAdapter(photos.toMutableList(), object : Listener{
            override fun onItemClickAtPosition(position: Int) {
                val photosDetailsList = arrayListOf<String>()
                photoList.mapTo(photosDetailsList){ it.regularUrl }
                DetailsActivity.startActivityForResult(this@MainActivity, position, photosDetailsList)
            }
        })

        customGridLayoutManager = GridLayoutManager(this, 3)
        customGridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

            override fun getSpanSize(position: Int): Int {

                return when (myAdapter.getItemViewType(position)) {
                    MyImageAdapter.TYPE_FOOTER -> 3
                    MyImageAdapter.TYPE_IMAGE -> 1
                    else -> 1
                }
            }
        }

        val dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.recycler_margin)

        with(recyclerView){
            addItemDecoration(GridDividerItemDecoration(dimensionPixelSize, 3))
            layoutManager = customGridLayoutManager
            adapter = myAdapter
            visible()
            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (cannotScrollVertically(recyclerView)) loadMore()
                }
            })
        }
    }

    private fun hideLoading() = progress.gone()

    private fun showLoading() = progress.visible()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == DetailsActivity.REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {

            val position = data?.getIntExtra(DetailsActivity.CURRENT_POSITION, 0)

            position?.let{
                customGridLayoutManager.scrollToPosition(it)
            }
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    private fun cannotScrollVertically(recyclerView: RecyclerView) =
            !recyclerView.canScrollVertically(1)
}
