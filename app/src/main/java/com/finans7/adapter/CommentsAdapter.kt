package com.finans7.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.finans7.databinding.CommentItemBinding
import com.finans7.model.comment.CommentModel

class CommentsAdapter(var commentList: List<CommentModel>)  : RecyclerView.Adapter<CommentsAdapter.CommentsHolder>() {
    private lateinit var v: CommentItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsHolder {
        v = CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentsHolder(v)
    }

    override fun onBindViewHolder(holder: CommentsHolder, position: Int) {
        holder.cI.comment = commentList.get(position)
    }

    override fun getItemCount() = commentList.size

    inner class CommentsHolder(var cI: CommentItemBinding) : RecyclerView.ViewHolder(cI.root)

    fun loadData(comments: List<CommentModel>){
        commentList = comments
        notifyDataSetChanged()
    }
}