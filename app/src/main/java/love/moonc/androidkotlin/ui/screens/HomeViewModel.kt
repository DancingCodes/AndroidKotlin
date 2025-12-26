package love.moonc.androidkotlin.ui.screens

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import love.moonc.androidkotlin.data.NetworkManager

class HomeViewModel : ViewModel() {
    var isRefreshing by mutableStateOf(false)
//    var messageText by mutableStateOf("")
    var isMessageLoading by mutableStateOf(false)

    init {
        fetchAllData()
    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshing = true
            fetchMessage()
            kotlinx.coroutines.delay(500)
            isRefreshing = false
        }
    }

    fun fetchAllData() {
        fetchMessage()
    }

    fun fetchMessage() {
        viewModelScope.launch {
            isMessageLoading = true
            try {
                val response = NetworkManager.api.getProfile()
//                if (response.code == "200") {
//                    messageText = response.data
//                }
            } finally {
                isMessageLoading = false
            }
        }
    }
}