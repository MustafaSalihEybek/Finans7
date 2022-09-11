package com.finans7.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.finans7.R
import com.finans7.adapter.AvatarsAdapter
import com.finans7.adapter.decoration.GridManagerDecoration
import com.finans7.databinding.AvatarsFragmentBinding
import com.finans7.model.Avatar
import com.finans7.util.Singleton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AvatarsFragment(val avatarList: List<Avatar>) : BottomSheetDialogFragment() {
    private lateinit var v: AvatarsFragmentBinding
    private lateinit var avatarsAdapter: AvatarsAdapter
    private lateinit var listener: AvatarItemListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = AvatarsFragmentBinding.inflate(inflater)
        return v.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v.avatarsFragmentRecyclerView.setHasFixedSize(true)
        v.avatarsFragmentRecyclerView.layoutManager = GridLayoutManager(view.context, 3)
        avatarsAdapter = AvatarsAdapter(avatarList)
        v.avatarsFragmentRecyclerView.addItemDecoration(GridManagerDecoration(Singleton.V_SIZE, Singleton.H_SIZE))
        v.avatarsFragmentRecyclerView.adapter = avatarsAdapter

        avatarsAdapter.setOnAvatarItemClickListener(object : AvatarsAdapter.AvatarOnItemClickListener{
            override fun onItemClick(avatar: Avatar) {
                listener.onListener(avatar)
            }
        })
    }

    interface AvatarItemListener {
        fun onListener(avatar: Avatar)
    }

    fun setOnAvatarItemListener(listener: AvatarItemListener){
        this.listener = listener
    }
}