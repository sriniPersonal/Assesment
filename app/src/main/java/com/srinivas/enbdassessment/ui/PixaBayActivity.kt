package com.srinivas.enbdassessment.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srinivas.enbdassessment.R
import com.srinivas.enbdassessment.data.db.entities.Hit
import com.srinivas.enbdassessment.databinding.ActivityPixabayBinding
import com.srinivas.enbdassessment.util.Constants
import com.srinivas.enbdassessment.util.Constants.Companion.SEARCH_IMAGES_TIME_DELAY
import com.srinivas.enbdassessment.util.toast
import kotlinx.android.synthetic.main.activity_pixabay.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class PixaBayActivity : AppCompatActivity(), ImageAdapter.ImageInterface, KodeinAware {

    override val kodein by kodein()
    private lateinit var viewModel: PixabayViewModel
    private lateinit var imageAdapter: ImageAdapter
    private val factory: PixabayViewModelFactory by instance()
    private lateinit var binding: ActivityPixabayBinding
    private var searchMenuItem: MenuItem? = null
    private var searchQuery: String = "apple"
    var job: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pixabay)
        viewModel = ViewModelProvider(this, factory).get(PixabayViewModel::class.java)
        setSupportActionBar(activity_main_toolbar)
        setupRecyclerView()



        lifecycleScope.launch(Dispatchers.Main) {
            updateUI()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        searchMenuItem = menu.findItem(R.id.action_search)
        val searchView = searchMenuItem?.actionView as SearchView
        searchView.setOnQueryTextListener(searchListener)
        return true
    }


    private suspend fun updateUI() {
        viewModel.localImages.observe(this, Observer { response ->
            hideProgressBar()
            rv_image_list.visibility = View.VISIBLE
            tv_no_result.visibility = View.GONE
            imageAdapter.differ.submitList(response.toList())
        })

        viewModel.searchImages.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { imageResponse ->
                        if (imageAdapter.differ.currentList.size == 0 && response.data.hits.size == 0) {
                            rv_image_list.visibility = View.GONE
                            tv_no_result.visibility = View.VISIBLE
                        } else {
                            rv_image_list.visibility = View.VISIBLE
                            tv_no_result.visibility = View.GONE
                            imageAdapter.differ.submitList(imageResponse.hits.toList())
                            val totalPages = imageResponse.totalHits / Constants.QUERY_PAGE_SIZE + 2
                            isLastPage = viewModel.searchImagesPage == totalPages
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    if (viewModel.searchImagesPage == 1) {
                        response.message?.let { message ->
                            rv_image_list.visibility = View.GONE
                            tv_no_result.visibility = View.VISIBLE
                            tv_no_result.text = message
                        }
                    } else {
                        response.message?.let { toast(it) }
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }


    private fun hideProgressBar() {
        progress_bar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        progress_bar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun setupRecyclerView() {
        imageAdapter = ImageAdapter(this)
        rv_image_list.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(this@PixaBayActivity, 2)
            addOnScrollListener(this@PixaBayActivity.scrollListener)
        }
    }

    private val searchListener: SearchView.OnQueryTextListener =
        object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchMenuItem!!.collapseActionView()
                job?.cancel()
                job = lifecycleScope.launch {
                    delay(SEARCH_IMAGES_TIME_DELAY)
                    query.let {
                        if (query.isNotEmpty()) {
                            searchQuery = query
                            viewModel.initializeSearchImages()
                            if (imageAdapter.differ.currentList.size > 0) {
                                val list = ArrayList<Hit>()
                                imageAdapter.differ.submitList(list)
                            }
                            viewModel.getImages(query)
                        } else {
                            toast("please enter search tag")
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        }


    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                searchQuery.let { viewModel.getImages(it) }
                isScrolling = false
            } else {
                rv_image_list.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    /**
     *  to Launch Detailed Activity on click on Image
     */
    override fun onItemclick(view: View, hit: Hit) {
        Bundle().apply {
            putSerializable("image", hit)
            val intent = Intent(this@PixaBayActivity, DetailedActivity::class.java)
            intent.putExtras(this)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@PixaBayActivity, view, ViewCompat.getTransitionName(view)!!

            )
            startActivity(intent, options.toBundle())
        }
    }
}


