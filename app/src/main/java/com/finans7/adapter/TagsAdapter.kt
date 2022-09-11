package com.finans7.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.finans7.databinding.TagItemBinding
import com.finans7.model.Tag
import com.finans7.model.category.CategoryModel
import com.finans7.view.NewsFragmentDirections

class TagsAdapter(val tagList: ArrayList<Tag>, val vV: View) : RecyclerView.Adapter<TagsAdapter.TagsHolder>() {
    private lateinit var v: TagItemBinding
    private lateinit var navDirections: NavDirections
    private var aPos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsHolder {
        v = TagItemBinding.inflate(LayoutInflater.from(parent.context))
        return TagsHolder(v)
    }

    override fun onBindViewHolder(holder: TagsHolder, position: Int) {
        holder.tV.tag = tagList.get(position)

        holder.itemView.setOnClickListener {
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION)
                goToCategoryPage(tagList.get(aPos).tagName)
        }
    }

    override fun getItemCount() = tagList.size

    inner class TagsHolder(var tV: TagItemBinding) : RecyclerView.ViewHolder(tV.root)

    private fun goToCategoryPage(tagName: String){
        navDirections = NewsFragmentDirections.actionNewsFragmentToNewsByCategoryFragment(null, tagName)
        Navigation.findNavController(vV).navigate(navDirections)
    }
}