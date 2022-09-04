package com.finans7.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.finans7.databinding.FourNewItemBinding
import com.finans7.model.homepage.News

class FourNewsAdapter(private var newsList: List<News>) : RecyclerView.Adapter<FourNewsAdapter.FourNewsHolder>() {
    private lateinit var v: FourNewItemBinding
    private lateinit var listener: FourNewsItemClickListener
    private var aPos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FourNewsHolder {
        v = FourNewItemBinding.inflate(LayoutInflater.from(parent.context))
        return FourNewsHolder(v)
    }

    override fun onBindViewHolder(holder: FourNewsHolder, position: Int) {
        holder.fNV.newdata = newsList.get(position)

        holder.itemView.setOnClickListener {
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION)
                listener.onItemClick(newsList.get(aPos), aPos)
        }
    }

    override fun getItemCount() = newsList.size

    inner class FourNewsHolder(var fNV: FourNewItemBinding) : RecyclerView.ViewHolder(fNV.root)

    fun loadData(newsData: List<News>){
        newsList = newsData
        notifyDataSetChanged()
    }

    interface FourNewsItemClickListener{
        fun onItemClick(newsData: News, newsIn: Int)
    }

    fun setFourNewsOnItemClickListener(listener: FourNewsItemClickListener){
        this.listener = listener
    }
}