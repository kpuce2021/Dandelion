package kr.ac.kpu.itemfinder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_tutorial_end.view.*

class TutorialFragmentEnd : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tutorial_end, container, false)
        /*
        val viewpager = requireActivity().findViewById<ViewPager2>(R.id.pager_tutorial)
        view.tutorial_end_prev_button.setOnClickListener {
            viewpager.currentItem = viewpager.currentItem - 1
        }
        view.tutorial_end_next_button.setOnClickListener {
            val sPreferencesEditor = context?.getSharedPreferences("tutorial_flag", AppCompatActivity.MODE_PRIVATE)?.edit()
            sPreferencesEditor?.putInt("tutorial", 1)
            sPreferencesEditor?.apply()
            requireActivity().finish()
        }
         */
        view.tutorial_end_text_layout.setOnClickListener {
            val sPreferencesEditor = context?.getSharedPreferences("tutorial_flag", AppCompatActivity.MODE_PRIVATE)?.edit()
            sPreferencesEditor?.putInt("tutorial", 1)
            sPreferencesEditor?.apply()
            requireActivity().finish()
        }
        return view
    }
}