package com.example.openinapp_assignment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.example.openinapp_assignment.api.RetrofitInstance
import com.example.openinapp_assignment.databinding.ActivityMainBinding
import com.example.openinapp_assignment.model.MainDataClass
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalTime

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //API Call Section:
        getData()
        binding.btRetry.setOnClickListener {
            getData()
        }

        //Greetings Section:

        val tvGreeting = binding.tvGreeting

        when (LocalTime.now().hour) {
            in 2..11 -> {
                tvGreeting.text = "Good Morning,"
            }
            in 12..17 -> {
                tvGreeting.text = "Good Afternoon,"
            }
            else -> {
                tvGreeting.text = "Good Evening,"
            }
        }

        //Top/Recent Links Section:

        val tabLayoutLinks = binding.tabLayoutLinks
        val viewPager2Links = binding.viewPager2Links

        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        tabLayoutLinks.addTab(tabLayoutLinks.newTab().setText("Top Links"))
        tabLayoutLinks.addTab(tabLayoutLinks.newTab().setText("Recent Links"))

        viewPager2Links.adapter = viewPagerAdapter

        tabLayoutLinks.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2Links.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        viewPager2Links.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayoutLinks.selectTab(tabLayoutLinks.getTabAt(position))
            }
        })
    }

    private fun getData(){
        val accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI"
        val pbApiCall = binding.pbApiCall
        val btRetry = binding.btRetry
        btRetry.isVisible = false
        pbApiCall.isVisible = true

        RetrofitInstance.apiService.getData(accessToken).enqueue(object : Callback<MainDataClass?> {
            override fun onResponse(
                call: Call<MainDataClass?>,
                response: Response<MainDataClass?>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        pbApiCall.isVisible = false
                        binding.tvName.text = data.status.toString()
                    }
                }
                else {
                    pbApiCall.isVisible = false
                    btRetry.isVisible = true
                    Toast.makeText(this@MainActivity,"Response is unsuccessful",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MainDataClass?>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.localizedMessage,Toast.LENGTH_SHORT).show()
                pbApiCall.isVisible = false
                btRetry.isVisible = true

            }
        })
    }
}