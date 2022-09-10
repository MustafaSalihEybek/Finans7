package com.finans7.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.finans7.databinding.TagItemBinding
import com.finans7.model.Tag

class TagsAdapter(val tagList: ArrayList<Tag>) : RecyclerView.Adapter<TagsAdapter.TagsHolder>() {
    private lateinit var v: TagItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsHolder {
        v = TagItemBinding.inflate(LayoutInflater.from(parent.context))
        return TagsHolder(v)
    }

    override fun onBindViewHolder(holder: TagsHolder, position: Int) {
        holder.tV.tag = tagList.get(position)
    }

    override fun getItemCount() = tagList.size

    inner class TagsHolder(var tV: TagItemBinding) : RecyclerView.ViewHolder(tV.root)
}