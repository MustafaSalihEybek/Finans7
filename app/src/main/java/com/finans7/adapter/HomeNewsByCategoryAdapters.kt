package com.finans7.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finans7.databinding.HomeNewsByCategoryItemBinding
import com.finans7.model.homepage.News

class HomeNewsByCategoryAdapters(val categoryNewsPair: Pair<ArrayList<String>, ArrayList<ArrayList<News>>>, val vV: View) : RecyclerView.Adapter<HomeNewsByCategoryAdapters.HomeNewsByCategoryHolder>() {
    private lateinit var v: HomeNewsByCategoryItemBinding
    private lateinit var homeCategoryNewsAdapter: HomeCategoryNewsAdapters

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeNewsByCategoryHolder {
        v = HomeNewsByCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeNewsByCategoryHolder(v)
    }

    override fun onBindViewHolder(holder: HomeNewsByCategoryHolder, position: Int) {
        holder.nCI.homeNewsByCategoryItemTxtCategory.text = categoryNewsPair.first.get(position)

        holder.nCI.homeNewsByCategoryItemRecyclerView.setHasFixedSize(true)
        holder.nCI.homeNewsByCategoryItemRecyclerView.layoutManager = LinearLayoutManager(vV.context, LinearLayoutManager.VERTICAL, false)
        homeCategoryNewsAdapter = HomeCategoryNewsAdapters(categoryNewsPair.second.get(position))
        holder.nCI.homeNewsByCategoryItemRecyclerView.adapter = homeCategoryNewsAdapter
    }

    override fun getItemCount() = categoryNewsPair.first.size

    inner class HomeNewsByCategoryHolder(var nCI: HomeNewsByCategoryItemBinding) : RecyclerView.ViewHolder(nCI.root)
}