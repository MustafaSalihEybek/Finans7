package com.finans7.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.finans7.databinding.CommentItemBinding
import com.finans7.model.comment.CommentModel

class CommentsAdapter(var commentList: ArrayList<CommentModel>)  : RecyclerView.Adapter<CommentsAdapter.CommentsHolder>() {
    private lateinit var v: CommentItemBinding
    private lateinit var listener: UpdateFavoriteClickListener
    private var aPos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsHolder {
        v = CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentsHolder(v)
    }

    override fun onBindViewHolder(holder: CommentsHolder, position: Int) {
        holder.cI.comment = commentList.get(position)

        holder.cI.commentItemLinearLike.setOnClickListener {
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION)
                listener.onUpdateClick(commentList.get(aPos), 4)
        }

        holder.cI.commentItemLinearDisLike.setOnClickListener {
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION)
                listener.onUpdateClick(commentList.get(aPos), 5)
        }
    }

    override fun getItemCount() = commentList.size

    inner class CommentsHolder(var cI: CommentItemBinding) : RecyclerView.ViewHolder(cI.root)

    fun loadData(comments: ArrayList<CommentModel>){
        commentList.addAll(comments)
        notifyDataSetChanged()
    }

    interface UpdateFavoriteClickListener{
        fun onUpdateClick(commentData: CommentModel, commentType: Int)
    }

    fun setOnUpdateFavoriteClickListener(listener: UpdateFavoriteClickListener){
        this.listener = listener
    }
}