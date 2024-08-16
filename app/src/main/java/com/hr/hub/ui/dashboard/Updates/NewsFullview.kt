package com.hr.hub.ui.dashboard.Updates

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.hr.hub.R

class NewsFullview :AppCompatActivity(){

    private lateinit var webnews:WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_fullview)

        webnews = findViewById(R.id.web_fullnews)

// Retrieve the URL from the Intent
        val url: String? = intent.getStringExtra("url")

// Get the WebSettings of the WebView and enable JavaScript
        val webSettings: WebSettings = webnews.settings
        webSettings.javaScriptEnabled = true

// Set a WebViewClient to handle loading web content within the WebView
        webnews.webViewClient = WebViewClient()

// Load the URL in the WebView, ensuring the URL is not null
        url?.let {
            webnews.loadUrl(it)
        }
    }

    override fun onBackPressed() {
        if(webnews.canGoBack()){
            webnews.goBack()
        }
        else
            super.onBackPressed()
    }

}