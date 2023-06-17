package com.example.openinapp_assignment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.openinapp_assignment.api.RetrofitInstance
import com.example.openinapp_assignment.model.MainDataClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var adapter : RecentLinkRecyclerViewAdapter
private lateinit var recyclerView : RecyclerView

/**
 * A simple [Fragment] subclass.
 * Use the [RecentLinksFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecentLinksFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recent_links, container, false)

        recyclerView = view.findViewById(R.id.rvRecentLinks)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        getData()

        // Inflate the layout for this fragment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RecentLinksFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecentLinksFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun getData(){
        RetrofitInstance.apiService.getData().enqueue(object : Callback<MainDataClass?> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<MainDataClass?>,
                response: Response<MainDataClass?>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        adapter = RecentLinkRecyclerViewAdapter(data.data.recent_links)
                        recyclerView.adapter = adapter
                    }
                }
                else {
                    Toast.makeText(requireContext(),"Response is unsuccessful", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MainDataClass?>, t: Throwable) {
                Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}