package com.example.openinapp_assignment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.example.openinapp_assignment.api.RetrofitInstance
import com.example.openinapp_assignment.databinding.ActivityMainBinding
import com.example.openinapp_assignment.model.MainDataClass
import com.example.openinapp_assignment.model.OverallUrlChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
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

        //Graph Section:

        val lineChart = binding.lineChart
        val overallUrlChart = OverallUrlChart(
            mapOf(
                "2023-05-19" to 9,
                "2023-05-20" to 4,
                "2023-05-21" to 1,
                "2023-05-22" to 10,
                "2023-05-23" to 7,
                "2023-05-24" to 9,
                "2023-05-25" to 0,
                "2023-05-26" to 2,
                "2023-05-27" to 2,
                "2023-05-28" to 1,
                "2023-05-29" to 0,
                "2023-05-30" to 2,
                "2023-05-31" to 1,
                "2023-06-01" to 1,
                "2023-06-02" to 5,
                "2023-06-03" to 1,
                "2023-06-04" to 2,
                "2023-06-05" to 19,
                "2023-06-06" to 0,
                "2023-06-07" to 3,
                "2023-06-08" to 5,
                "2023-06-09" to 11,
                "2023-06-10" to 4,
                "2023-06-11" to 9,
                "2023-06-12" to 4,
                "2023-06-13" to 20,
                "2023-06-14" to 2,
                "2023-06-15" to 7,
                "2023-06-16" to 24,
                "2023-06-17" to 2,
                "2023-06-18" to 2
            )
        )

// Convert the map data to Entry objects
        val entries = mutableListOf<Entry>()
        val data = overallUrlChart.mapData

        data.entries.forEachIndexed { index, entry ->
            val date = entry.key
            val value = entry.value

            entries.add(Entry(index.toFloat(), value.toFloat()))
        }

// Create a LineDataSet
        val dataSet = LineDataSet(entries, "URL Clicks")
        dataSet.color = Color.BLUE
        dataSet.setDrawCircles(true)
        dataSet.setDrawCircleHole(false)

// Create a LineData object and set it to the chart
        val lineData = LineData(dataSet)
        lineChart.data = lineData

// Customize the appearance of the chart
        lineChart.invalidate()
        lineChart.setNoDataText("No data available")
        lineChart.setNoDataTextColor(Color.BLACK)
        lineChart.setDrawGridBackground(false)
        lineChart.description.text = ""
        lineChart.legend.isEnabled = false

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
        val pbApiCall = binding.pbApiCall
        val btRetry = binding.btRetry
//        var mapDATA: HashMap<String, Int> = hashMapOf()
        btRetry.isVisible = false
        btRetry.text = "Try Again"
        pbApiCall.isVisible = true

        RetrofitInstance.apiService.getData().enqueue(object : Callback<MainDataClass?> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<MainDataClass?>,
                response: Response<MainDataClass?>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        pbApiCall.isVisible = false
                        binding.tvName.text = (data.support_whatsapp_number + " 👋")
                        btRetry.text = "Refresh"
                        btRetry.isVisible = true
                        Log.d("GetData_TAG",data.data.overall_url_chart.toString())
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