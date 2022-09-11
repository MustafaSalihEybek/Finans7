package com.finans7.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.finans7.databinding.NavCategoryItemBinding
import com.finans7.model.category.CategoryModel
import com.finans7.view.MainFragmentDirections

class NavCategoriesAdapter(var categoryList: List<CategoryModel>, val vV: View) : RecyclerView.Adapter<NavCategoriesAdapter.NavCategoriesHolder>() {
    private lateinit var v: NavCategoryItemBinding
    private lateinit var navDirections: NavDirections
    private var aPos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavCategoriesHolder {
        v = NavCategoryItemBinding.inflate(LayoutInflater.from(parent.context))
        return NavCategoriesHolder(v)
    }

    override fun onBindViewHolder(holder: NavCategoriesHolder, position: Int) {
        holder.nVI.category = categoryList.get(position)

        holder.itemView.setOnClickListener {
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION)
                goToNewsByCategoryPage(categoryList.get(aPos))
        }
    }

    override fun getItemCount() = categoryList.size

    inner class NavCategoriesHolder(var nVI: NavCategoryItemBinding) : RecyclerView.ViewHolder(nVI.root)

    fun loadData(categories: List<CategoryModel>){
        categoryList = categories
        notifyDataSetChanged()
    }

    private fun goToNewsByCategoryPage(categoryData: CategoryModel){
        navDirections = MainFragmentDirections.actionMainFragmentToNewsByCategoryFragment(categoryData, null)
        Navigation.findNavController(vV).navigate(navDirections)
    }
}