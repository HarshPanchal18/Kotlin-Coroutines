package com.example.coroutines.network.http

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.coroutines.network.internal.NoConnectivityException
import com.example.coroutines.network.response.Data

class DataNetworkInterfaceImpl(private val apiServices: ApiServices) : DataNetworkInterface {
    private val _downloadData = MutableLiveData<Data>()
    override val downLoadData: LiveData<Data>
        get() = _downloadData

    override suspend fun fetchData() {
        try {
            val fetchData = apiServices.getDataItem().await()
            _downloadData.postValue(fetchData)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet Connections", e)
        }
    }
}
