package com.finans7.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.finans7.databinding.AvatarItemBinding
import com.finans7.model.Avatar

class AvatarsAdapter(val avatarList: List<Avatar>) : RecyclerView.Adapter<AvatarsAdapter.AvatarsHolder>() {
    private lateinit var v: AvatarItemBinding
    private lateinit var listener: AvatarOnItemClickListener
    private var aPos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarsHolder {
        v = AvatarItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AvatarsHolder(v)
    }

    override fun onBindViewHolder(holder: AvatarsHolder, position: Int) {
        holder.aI.avatar = avatarList.get(position)

        holder.itemView.setOnClickListener {
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION)
                listener.onItemClick(avatarList.get(aPos))
        }
    }

    override fun getItemCount() = avatarList.size

    inner class AvatarsHolder(var aI: AvatarItemBinding) : RecyclerView.ViewHolder(aI.root)

    interface AvatarOnItemClickListener {
        fun onItemClick(avatar: Avatar)
    }

    fun setOnAvatarItemClickListener(listener: AvatarOnItemClickListener){
        this.listener = listener
    }
}