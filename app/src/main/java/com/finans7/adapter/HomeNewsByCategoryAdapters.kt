package com.finans7.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finans7.databinding.HomeNewsByCategoryItemBinding
import com.finans7.model.categorynews.PostListModel
import com.finans7.model.homepage.News
import com.finans7.view.MainFragmentDirections

class HomeNewsByCategoryAdapters(val categoryNewsPair: Pair<ArrayList<String>, ArrayList<ArrayList<PostListModel>>>, val vV: View) : RecyclerView.Adapter<HomeNewsByCategoryAdapters.HomeNewsByCategoryHolder>() {
    private lateinit var v: HomeNewsByCategoryItemBinding
    private lateinit var homeCategoryNewsAdapter: HomeCategoryNewsAdapters
    private lateinit var navDirections: NavDirections
    private var aPos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeNewsByCategoryHolder {
        v = HomeNewsByCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeNewsByCategoryHolder(v)
    }

    override fun onBindViewHolder(holder: HomeNewsByCategoryHolder, position: Int) {
        holder.nCI.homeNewsByCategoryItemTxtCategory.text = categoryNewsPair.first.get(position)

        holder.nCI.homeNewsByCategoryItemTxtAllCategory.setOnClickListener {
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION)
                goToNewsByCategoryPage(categoryNewsPair.first.get(aPos))
        }

        holder.nCI.homeNewsByCategoryItemRecyclerView.setHasFixedSize(true)
        holder.nCI.homeNewsByCategoryItemRecyclerView.layoutManager = LinearLayoutManager(vV.context, LinearLayoutManager.VERTICAL, false)
        homeCategoryNewsAdapter = HomeCategoryNewsAdapters(categoryNewsPair.second.get(position), vV)
        holder.nCI.homeNewsByCategoryItemRecyclerView.adapter = homeCategoryNewsAdapter
    }

    override fun getItemCount() = categoryNewsPair.first.size

    inner class HomeNewsByCategoryHolder(var nCI: HomeNewsByCategoryItemBinding) : RecyclerView.ViewHolder(nCI.root)

    private fun goToNewsByCategoryPage(category: String){
        navDirections = MainFragmentDirections.actionMainFragmentToNewsByCategoryFragment(null, category)
        Navigation.findNavController(vV).navigate(navDirections)
    }
}