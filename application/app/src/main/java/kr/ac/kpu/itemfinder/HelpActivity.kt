package kr.ac.kpu.itemfinder

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

private const val NUM_PAGES = 5

class HelpActivity : FragmentActivity() {

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById<ViewPager2>(R.id.pager)

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        // DotsIndicator
        val dotsIndicator = findViewById<DotsIndicator>(R.id.dots_indicator)
        dotsIndicator.setViewPager2(viewPager)
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        //override fun createFragment(position: Int): Fragment = fragment_screen_slide_page()
        override fun createFragment(position: Int): Fragment {
            when(position) {
                0 -> return HelpFragment_01()
                1 -> return HelpFragment_02()
                2 -> return HelpFragment_03()
                3 -> return HelpFragment_04()
                4 -> return HelpFragment_05()
            }
            return HelpFragment_err()
        }
    }
}