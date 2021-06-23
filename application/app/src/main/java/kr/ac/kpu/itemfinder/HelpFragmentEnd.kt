package kr.ac.kpu.itemfinder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_help_end.view.*

class HelpFragmentEnd : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_help_end, container, false)
        val viewpager = requireActivity().findViewById<ViewPager2>(R.id.pager)
        view.help_end_prev_button.setOnClickListener {
            viewpager.currentItem = viewpager.currentItem - 1
        }
        view.help_end_next_button.setOnClickListener {
            val sPreferencesEditor = context?.getSharedPreferences("help_flag", AppCompatActivity.MODE_PRIVATE)?.edit()
            sPreferencesEditor?.putInt("help", 1)
            sPreferencesEditor?.apply()
            requireActivity().finish()
        }
        // Inflate the layout for this fragment
        return view
    }
}