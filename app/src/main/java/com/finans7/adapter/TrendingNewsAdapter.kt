package com.finans7.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.finans7.databinding.TrendItemBinding
import com.finans7.model.categorynews.PostListModel
import com.finans7.util.AppUtil
import com.finans7.view.MainFragmentDirections

class TrendingNewsAdapter(val newsList: ArrayList<PostListModel>, val vV: View) : RecyclerView.Adapter<TrendingNewsAdapter.TrendingNewsHolder>() {
    private lateinit var v: TrendItemBinding
    private lateinit var navDirections: NavDirections
    private var aPos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingNewsHolder {
        v = TrendItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrendingNewsHolder(v)
    }

    override fun onBindViewHolder(holder: TrendingNewsHolder, position: Int) {
        holder.tV.newsdata = newsList.get(position)

        holder.itemView.setOnClickListener {
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION)
                goToNewsDetailPage(newsList.get(aPos))
        }
    }

    override fun getItemCount() = newsList.size

    inner class TrendingNewsHolder(var tV: TrendItemBinding) : RecyclerView.ViewHolder(tV.root)

    private fun goToNewsDetailPage(newsData: PostListModel){
        navDirections = MainFragmentDirections.actionMainFragmentToNewsFragment(AppUtil.getPostIdList(arrayListOf(newsData)), 0)
        Navigation.findNavController(vV).navigate(navDirections)
    }
}