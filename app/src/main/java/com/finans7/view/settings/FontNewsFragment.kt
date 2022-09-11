package com.finans7.view.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.navigation.Navigation
import com.finans7.R
import com.finans7.databinding.FragmentFontNewsBinding
import com.finans7.util.SharedPreferences

class FontNewsFragment : Fragment() {
    private lateinit var v: View
    private lateinit var fontNewsBinding: FragmentFontNewsBinding

    private lateinit var sharedPreferences: SharedPreferences

    private fun init(){
        sharedPreferences = SharedPreferences(v.context)
        setNewsContentTextSize((sharedPreferences.getFontSize().toFloat() / 6.5f))
        fontNewsBinding.fontNewsFragmentSeekBar.progress = sharedPreferences.getFontSize()

        fontNewsBinding.fontNewsFragmentImgBack.setOnClickListener {
            Navigation.findNavController(v).popBackStack()
        }

        fontNewsBinding.fontNewsFragmentSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                setNewsContentTextSize((p1.toFloat() / 6.5f))
                sharedPreferences.saveFontSize(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fontNewsBinding = FragmentFontNewsBinding.inflate(inflater)
        return fontNewsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    private fun setNewsContentTextSize(newsSize: Float){
        fontNewsBinding.fontNewsFragmentTxtNewsContent.textSize = newsSize
    }
}