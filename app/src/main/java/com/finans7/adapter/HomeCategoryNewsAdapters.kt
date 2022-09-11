package com.finans7.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.finans7.databinding.HomeCategoryNewsItemBinding
import com.finans7.model.categorynews.PostListModel
import com.finans7.model.homepage.News
import com.finans7.view.MainFragmentDirections

class HomeCategoryNewsAdapters(val newsList: List<PostListModel>, val vV: View) : RecyclerView.Adapter<HomeCategoryNewsAdapters.HomeCategoryNewsHolder>() {
    private lateinit var v: HomeCategoryNewsItemBinding
    private lateinit var navDirections: NavDirections
    private var aPos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCategoryNewsHolder {
        v = HomeCategoryNewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeCategoryNewsHolder(v)
    }

    override fun onBindViewHolder(holder: HomeCategoryNewsHolder, position: Int) {
        holder.hCV.newsdata = newsList.get(position)

        holder.itemView.setOnClickListener {
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION)
                goToNewsDetailPage(newsList.get(aPos))
        }
    }

    override fun getItemCount() = newsList.size

    inner class HomeCategoryNewsHolder(var hCV: HomeCategoryNewsItemBinding) : RecyclerView.ViewHolder(hCV.root)

    private fun goToNewsDetailPage(newsData: PostListModel){
        navDirections = MainFragmentDirections.actionMainFragmentToNewsFragment(arrayOf(newsData), 0)
        Navigation.findNavController(vV).navigate(navDirections)
    }
}