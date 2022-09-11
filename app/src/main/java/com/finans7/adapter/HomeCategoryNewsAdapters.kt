package com.finans7.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.finans7.databinding.HomeCategoryNewsItemBinding
import com.finans7.model.categorynews.PostListModel
import com.finans7.model.homepage.News

class HomeCategoryNewsAdapters(val newsList: List<PostListModel>) : RecyclerView.Adapter<HomeCategoryNewsAdapters.HomeCategoryNewsHolder>() {
    private lateinit var v: HomeCategoryNewsItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCategoryNewsHolder {
        v = HomeCategoryNewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeCategoryNewsHolder(v)
    }

    override fun onBindViewHolder(holder: HomeCategoryNewsHolder, position: Int) {
        holder.hCV.newsdata = newsList.get(position)
    }

    override fun getItemCount() = newsList.size

    inner class HomeCategoryNewsHolder(var hCV: HomeCategoryNewsItemBinding) : RecyclerView.ViewHolder(hCV.root)
}