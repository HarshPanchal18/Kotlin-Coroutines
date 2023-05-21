package com.example.coroutines.network.http

import androidx.lifecycle.LiveData
import com.example.coroutines.network.response.Data

interface DataNetworkInterface {
    val downLoadData: LiveData<Data>
    suspend fun fetchData()
}
