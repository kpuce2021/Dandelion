package kr.ac.kpu.itemfinder

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

private const val NUM_PAGES_TUTORIAL = 7

class TutorialActivity : FragmentActivity() {

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById<ViewPager2>(R.id.pager_tutorial)

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        // DotsIndicator
        val dotsIndicator = findViewById<DotsIndicator>(R.id.dots_indicator_tutorial)
        dotsIndicator.setViewPager2(viewPager)
    }

    /**
     * A simple pager adapter that represents 7 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES_TUTORIAL

        //override fun createFragment(position: Int): Fragment = fragment_screen_slide_page()
        override fun createFragment(position: Int): Fragment {
            when(position) {
                0 -> return TutorialFragmentStart()
                1 -> return TutorialFragmentMid01()
                2 -> return TutorialFragmentMid02()
                3 -> return TutorialFragmentMid03()
                4 -> return TutorialFragmentMid04()
                5 -> return TutorialFragmentMid05()
                6 -> return TutorialFragmentEnd()
            }
            return HelpFragmentErr()
        }
    }
}