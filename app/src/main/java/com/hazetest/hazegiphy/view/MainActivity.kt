package com.hazetest.hazegiphy.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.hazetest.hazegiphy.R
import com.hazetest.hazegiphy.adapter.MainViewPagerAdapter
import com.hazetest.hazegiphy.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val tabTitleArray = arrayOf(
        "Trending",
        "Favorite"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val mainViewPager = binding.viewPager
        val mainTabLayout = binding.tabLayout

        mainViewPager.adapter = MainViewPagerAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(mainTabLayout, mainViewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()
    }
}