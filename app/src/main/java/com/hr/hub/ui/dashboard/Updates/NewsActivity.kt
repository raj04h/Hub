package com.hr.hub.ui.dashboard.Updates

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hr.hub.R
import com.hr.hub.ui.APIFile
import com.hr.hub.ui.dashboard.Updates.Adapters.NewsRecycleAdapter
import com.kwabenaberko.newsapilib.NewsApiClient
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest
import com.kwabenaberko.newsapilib.models.response.ArticleResponse
import java.util.Locale.Category

class NewsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var newsRecycleAdapter: NewsRecycleAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private lateinit var btnHeadline:Button
    private lateinit var btnBusiness: Button
    private lateinit var btnEntertainment: Button
    private lateinit var btnHealth: Button
    private lateinit var btnScience: Button
    private lateinit var btnSport: Button
    private lateinit var btnTechnology: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        getId()
        btnclicklistener()
        setUpRecyclerView()
        getNews("GENERAL")
    }
    private fun getId(){
        recyclerView = findViewById(R.id.recycle_news)
        progressBar = findViewById(R.id.progbar_news)

        btnHeadline=findViewById(R.id.btn_headlines)
        btnBusiness=findViewById(R.id.btn_business)
        btnEntertainment=findViewById(R.id.btn_entertainment)
        btnHealth=findViewById(R.id.btn_health)
        btnScience=findViewById(R.id.btn_science)
        btnSport=findViewById(R.id.btn_sport)
        btnTechnology=findViewById(R.id.btn_technology)
    }
    private fun btnclicklistener(){
        btnHeadline.setOnClickListener(this)
        btnBusiness.setOnClickListener(this)
        btnEntertainment.setOnClickListener(this)
        btnHealth.setOnClickListener(this)
        btnScience.setOnClickListener(this)
        btnSport.setOnClickListener(this)
        btnTechnology.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        if(v is Button){
            val Category=v.text.toString()
            getNews(Category)
        }
    }

    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        newsRecycleAdapter = NewsRecycleAdapter(this, ArrayList())
        recyclerView.adapter = newsRecycleAdapter
    }

    private fun changeInProgress(isVisible: Boolean) {
        progressBar.visibility =
            if (isVisible){
                View.VISIBLE
            }
            else{
            View.INVISIBLE
            }
    }

    private fun getNews(category: String) {
        changeInProgress(true)

        val newsApiClient = NewsApiClient(APIFile.newsAPI)
        newsApiClient.getTopHeadlines(
            TopHeadlinesRequest.Builder()
                .language("en")  // Hindi language code
                .category(category)
                .build(),

            object: NewsApiClient.ArticlesResponseCallback {
                override fun onSuccess(response: ArticleResponse) {
                    runOnUiThread {
                        changeInProgress(false)
                        response.articles?.let {
                            articleList -> newsRecycleAdapter.updateData(articleList)
                        }
                    }
                }

                override fun onFailure(throwable: Throwable) {
                    runOnUiThread {
                        changeInProgress(false)
                        Log.e("NewsActivity", "Error fetching news: ${throwable.message}")
                    }
                }
            }

        )

    }
}
