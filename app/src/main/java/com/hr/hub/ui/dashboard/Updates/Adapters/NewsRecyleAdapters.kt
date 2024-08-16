package com.hr.hub.ui.dashboard.Updates.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hr.hub.R
import com.hr.hub.ui.dashboard.Updates.NewsFullview
import com.kwabenaberko.newsapilib.models.Article
import com.squareup.picasso.Picasso

class NewsRecycleAdapter(
    private val context: Context,
    private val arrnews: ArrayList<NewsModel>
) : RecyclerView.Adapter<NewsRecycleAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgNews: ImageView = itemView.findViewById(R.id.img_news)
        val titleNews: TextView = itemView.findViewById(R.id.article_title)
        val sourceNews: TextView = itemView.findViewById(R.id.article_src)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.news_recycler, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrnews.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsItem = arrnews[position]
        holder.titleNews.text = newsItem.tittle
        holder.sourceNews.text = newsItem.source

        holder.itemView.setOnClickListener{v->
            val intent=Intent(v.context,NewsFullview::class.java)

            intent.putExtra("url",newsItem.urlnews)
            v.context.startActivity(intent)
        }

        if(newsItem.urlToImage.isNotEmpty()){
            Picasso.get()
                .load(newsItem.urlToImage)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.baseline_broken_image_24)
                .into(holder.imgNews)

            /*
            Glide.with(holder.itemView.context)
            .load(newsItem.urlToImage)
            .placeholder(R.drawable.placeholder)
            .into(holder.imageView)
             */
        }
        else{
            holder.imgNews.setImageResource(R.drawable.baseline_broken_image_24)
        }
    }

    fun updateData(newArticles: List<Article>) {
        arrnews.clear()
        newArticles.forEach { article ->
            arrnews.add(
                NewsModel(
                    urlToImage = article.urlToImage ?: "",    // Handle potential null values
                    tittle = article.title ?: "No Title",     // Handle potential null values
                    source = article.source?.name ?: "Unknown Source", // Handle potential null values
                    urlnews = article.url ?: ""               // Assign the URL of the news article
                )
            )
        }
        notifyDataSetChanged()
    }

}
