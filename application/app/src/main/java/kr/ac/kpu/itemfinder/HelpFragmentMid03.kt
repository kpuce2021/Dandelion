package kr.ac.kpu.itemfinder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_help_mid03.view.*

class HelpFragmentMid03 : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_help_mid03, container, false)
        val viewpager = requireActivity().findViewById<ViewPager2>(R.id.pager)
        view.help_mid03_prev_button.setOnClickListener {
            viewpager.currentItem = viewpager.currentItem - 1
        }
        view.help_mid03_next_button.setOnClickListener {
            viewpager.currentItem = viewpager.currentItem + 1
        }
        return view
    }
}