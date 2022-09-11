package com.finans7.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.finans7.databinding.TrendItemBinding
import com.finans7.model.categorynews.PostListModel
import com.finans7.model.homepage.News

class TrendingNewsAdapter(val newsList: ArrayList<PostListModel>) : RecyclerView.Adapter<TrendingNewsAdapter.TrendingNewsHolder>() {
    private lateinit var v: TrendItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingNewsHolder {
        v = TrendItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrendingNewsHolder(v)
    }

    override fun onBindViewHolder(holder: TrendingNewsHolder, position: Int) {
        holder.tV.newsdata = newsList.get(position)
    }

    override fun getItemCount() = newsList.size

    inner class TrendingNewsHolder(var tV: TrendItemBinding) : RecyclerView.ViewHolder(tV.root)
}