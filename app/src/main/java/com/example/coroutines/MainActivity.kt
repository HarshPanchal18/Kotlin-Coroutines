package com.example.coroutines

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coroutines.adapter.DataAdapter
import com.example.coroutines.databinding.ActivityMainBinding
import com.example.coroutines.network.http.ApiServices
import com.example.coroutines.network.http.ConnectivityInterceptorImpl
import com.example.coroutines.network.http.DataNetworkInterfaceImpl
import com.example.coroutines.network.response.DataItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        stateLoading(false)
        getDataItem()
    }

    private fun getDataItem() {
        //Call ApiServices
        val apiServices = ApiServices(ConnectivityInterceptorImpl(this))
        val dataNetworkInterfaceImpl = DataNetworkInterfaceImpl(apiServices)
        dataNetworkInterfaceImpl.downLoadData.observe(this, Observer { data ->
            // State Loading
            stateLoading(true)

            val dataItemList = ArrayList<DataItem>()

            binding.recyclerView.adapter = DataAdapter(dataItemList)
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.recyclerView.setHasFixedSize(true)

            for (position in 0 until data.size) {
                val dataTitle = data[position].title
                val dataDescription = data[position].body
                val dataId = data[position].id
                val dataUserId = data[position].userId

                val dataItem = DataItem(
                    dataId, dataTitle, dataDescription, dataUserId,
                )
                dataItemList += dataItem
            }
        })

        GlobalScope.launch(Dispatchers.Main) {
            dataNetworkInterfaceImpl.fetchData()
        }
    }

    private fun stateLoading(state: Boolean): Boolean {
        if (state) binding.loadingBar.visibility = View.GONE
        else binding.loadingBar.visibility = View.VISIBLE
        return state
    }
}
