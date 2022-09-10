package com.finans7.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.finans7.databinding.BottomSheetCategoryItemBinding
import com.finans7.model.category.CategoryModel

class CategoriesAdapter(var categoryList: ArrayList<CategoryModel>) : RecyclerView.Adapter<CategoriesAdapter.CategoriesHolder>() {
    private lateinit var v: BottomSheetCategoryItemBinding
    private lateinit var listener: CategoryItemClickListener
    private var aPos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesHolder {
        v = BottomSheetCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoriesHolder(v)
    }

    override fun onBindViewHolder(holder: CategoriesHolder, position: Int) {
        holder.cSHV.category = categoryList.get(position)

        holder.itemView.setOnClickListener {
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION)
                listener.onItemClick(categoryList.get(aPos))
        }
    }

    override fun getItemCount() = categoryList.size

    inner class CategoriesHolder(var cSHV: BottomSheetCategoryItemBinding) : RecyclerView.ViewHolder(cSHV.root)

    fun loadData(categories: ArrayList<CategoryModel>){
        categoryList = categories
        notifyDataSetChanged()
    }

    interface CategoryItemClickListener {
        fun onItemClick(categoryModel: CategoryModel)
    }

    fun setOnCategoryItemClickListener(listener: CategoryItemClickListener){
        this.listener = listener
    }
}