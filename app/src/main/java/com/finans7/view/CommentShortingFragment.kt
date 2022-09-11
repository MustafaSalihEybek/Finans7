package com.finans7.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.finans7.R
import com.finans7.databinding.CommentShortingFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommentShortingFragment(val lastSelectedIn: Int) : BottomSheetDialogFragment(), View.OnClickListener {
    private lateinit var v: CommentShortingFragmentBinding
    private lateinit var listener: CommentShortingOnItemClickListener
    private var shortingNameList: Array<String> = arrayOf("Yeniden Eskiye", "Eskiden Yeniye", "Popüler Yorumlar")
    private lateinit var radioList: Array<RadioButton>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = CommentShortingFragmentBinding.inflate(inflater)
        return v.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v.commentShortingFragmentLinearRadio1.setOnClickListener(this)
        v.commentShortingFragmentRadio1.setOnClickListener(this)
        v.commentShortingFragmentLinearRadio2.setOnClickListener(this)
        v.commentShortingFragmentRadio2.setOnClickListener(this)
        v.commentShortingFragmentLinearRadio3.setOnClickListener(this)
        v.commentShortingFragmentRadio3.setOnClickListener(this)

        if (lastSelectedIn == 1)
            v.commentShortingFragmentRadio1.isChecked = true
        else if (lastSelectedIn == 2)
            v.commentShortingFragmentRadio2.isChecked = true
        else
            v.commentShortingFragmentRadio3.isChecked = true

        radioList = arrayOf(v.commentShortingFragmentRadio1, v.commentShortingFragmentRadio2, v.commentShortingFragmentRadio3)
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.comment_shorting_fragment_linearRadio1 -> selectRadio(1, shortingNameList.get(0), radioList)
                R.id.comment_shorting_fragment_radio1 -> selectRadio(1, shortingNameList.get(0), radioList)
                R.id.comment_shorting_fragment_linearRadio2 -> selectRadio(2, shortingNameList.get(1), radioList)
                R.id.comment_shorting_fragment_radio2 -> selectRadio(2, shortingNameList.get(1), radioList)
                R.id.comment_shorting_fragment_linearRadio3 -> selectRadio(3, shortingNameList.get(2), radioList)
                R.id.comment_shorting_fragment_radio3 -> selectRadio(3, shortingNameList.get(2), radioList)
            }
        }
    }

    interface CommentShortingOnItemClickListener {
        fun onItemClick(shortIn: Int, shortName: String)
    }

    fun setOnCommentShortingItemClickListener(listener: CommentShortingOnItemClickListener){
        this.listener = listener
    }

    private fun selectRadio(shortIn: Int, shortName: String, radioList: Array<RadioButton>){
        for (r in radioList.indices)
                radioList.get(r).isChecked = (r == (shortIn - 1))

        listener.onItemClick(shortIn, shortName)
        dismiss()
    }
}