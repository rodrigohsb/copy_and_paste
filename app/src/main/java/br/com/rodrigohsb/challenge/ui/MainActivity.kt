package br.com.rodrigohsb.challenge.ui

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
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
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.error_state.*
import kotlinx.android.synthetic.main.loading_state.*

class MainActivity : AppCompatActivity() {

    private lateinit var myAdapter: MyImageAdapter

    private var photoList = arrayListOf<Photo>()

    private lateinit var customGridLayoutManager: GridLayoutManager

    private val CACHED_PHOTOS = "CACHED_PHOTOS"

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

        if (savedInstanceState != null && savedInstanceState.containsKey(CACHED_PHOTOS)) {
            photoList = savedInstanceState.getParcelableArrayList(CACHED_PHOTOS)
        }
    }

    override fun onStart() {
        super.onStart()
        loadContent()
    }

    private fun loadContent() {
        if(photoList.isNotEmpty()) {
            hideLoading()
            showImages(photoList)
            return
        }
        disposables += viewModel.listPhotos()
                .subscribe({
                    status: State ->

                    when(status){

                        is State.Loading -> showLoading()

                        is State.Success -> {
                            hideLoading()
                            photoList.addAll(status.photos)
                            showImages(status.photos)
                        }
                    }
                },{
                    t ->
                    clearView()
                    when (t) {
                        is TimeoutException -> showErrorView(R.string.timeout_error_message)
                        is Error4XXException -> showErrorView(R.string.client_error_message)
                        is NoNetworkException -> showErrorView(R.string.no_connection_error_message)
                        is BadRequestException -> showErrorView(R.string.bad_request_error_message)
                        is NoDataException -> showErrorView(R.string.empty_error_message)
                        is Error5XXException -> {
                            showErrorView(R.string.server_error_message)
                            errorButton.visibility = View.GONE
                        }
                        else -> showErrorView(R.string.generic_error_message)
                    }
                })
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        photoList.apply {
            outState?.clear()
            outState?.putParcelableArrayList(CACHED_PHOTOS, this)
            return
        }
        super.onSaveInstanceState(outState)
    }

    private fun loadMore(): Disposable {
        return viewModel.loadMore()
                .subscribe({
                    status: State ->
                    when(status){
                        is State.Success -> appendImages(status.photos)
                    }
                },{
                    t ->
                    clearView()
                    when (t) {
                        is TimeoutException -> showSnackBar(R.string.timeout_error_message)
                        is Error4XXException -> showSnackBar(R.string.client_error_message)
                        is NoNetworkException -> showSnackBar(R.string.no_connection_error_message)
                        is BadRequestException -> showSnackBar(R.string.bad_request_error_message)
                        is NoDataException -> showSnackBar(R.string.empty_error_message)
                        is Error5XXException -> showSnackBar(R.string.server_error_message)
                        else -> showSnackBar(R.string.generic_error_message)
                    }
                })
    }

    private fun appendImages(photos: List<Photo>){
        photoList.addAll(photos)
        myAdapter.appendImages(photos)
    }

    private fun showImages(photos: List<Photo>){

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
                    if (cannotScrollVertically(recyclerView)) disposables += loadMore()
                }
            })
        }
    }

    private fun clearView() {
        hideErrorView()
        hideLoading()
    }

    private fun hideErrorView() { errorRoot.visibility = View.GONE }

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

    private fun showErrorView(msgId: Int) {
        errorState.setText(msgId)
        RxView
            .clicks(errorButton)
            .subscribe({
                clearView()
                disposables += loadMore()
            })
        errorRoot.visibility = View.VISIBLE
    }

    private fun showSnackBar(msgId: Int) = Snackbar.make(root, "$msgId", Snackbar.LENGTH_LONG).show()

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    private fun cannotScrollVertically(recyclerView: RecyclerView) =
            !recyclerView.canScrollVertically(1)
}
