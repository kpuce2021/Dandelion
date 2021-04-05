package kr.ac.kpu.itemfinder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_help_end.view.*

class HelpFragment_end : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_help_end, container, false)
        view.help_end_button.setOnClickListener {
            val sPreferencesEditor = context?.getSharedPreferences("help_flag", AppCompatActivity.MODE_PRIVATE)?.edit()
            sPreferencesEditor?.putInt("help", 1)
            sPreferencesEditor?.apply()
            requireActivity().finish()
        }
        // Inflate the layout for this fragment
        return view
    }
}