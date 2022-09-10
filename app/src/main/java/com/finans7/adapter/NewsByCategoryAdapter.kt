package com.finans7.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.finans7.databinding.NewsByCategoryItemBinding
import com.finans7.model.categorynews.PostListModel
import com.finans7.view.MainFragmentDirections
import com.finans7.view.NewsByCategoryFragmentDirections

class NewsByCategoryAdapter(var postList: ArrayList<PostListModel>, val vV: View, val fromMain: Boolean) : RecyclerView.Adapter<NewsByCategoryAdapter.NewsByCategoryHolder>() {
    private lateinit var v: NewsByCategoryItemBinding
    private lateinit var navDirections: NavDirections
    private var aPos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsByCategoryHolder {
        v = NewsByCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsByCategoryHolder(v)
    }

    override fun onBindViewHolder(holder: NewsByCategoryHolder, position: Int) {
        holder.nCI.postdata = postList.get(position)

        holder.itemView.setOnClickListener {
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION)
                goToNewsPage(postList.get(aPos))
        }
    }

    override fun getItemCount() = postList.size

    inner class NewsByCategoryHolder(var nCI: NewsByCategoryItemBinding) : RecyclerView.ViewHolder(nCI.root)

    fun loadData(posts: ArrayList<PostListModel>){
        postList.addAll(posts)
        notifyDataSetChanged()
    }

    private fun goToNewsPage(postData: PostListModel){
        if (!fromMain)
            navDirections = NewsByCategoryFragmentDirections.actionNewsByCategoryFragmentToNewsFragment(null, arrayOf(postData), 0, true)
        else
            navDirections = MainFragmentDirections.actionMainFragmentToNewsFragment(null, arrayOf(postData), 0, true)

        Navigation.findNavController(vV).navigate(navDirections)
    }
}