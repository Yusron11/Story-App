package com.dicoding.picodiploma.loginwithanimation.view.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.api.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.response.NewStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CameraViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isMessage = MutableLiveData<String?>()
    val isMessage: LiveData<String?> = _isMessage

    private val _addNewStoryResult = MutableLiveData<NewStoryResponse?>()
    val result: LiveData<NewStoryResponse?> = _addNewStoryResult

    private val _loadingCompleted = MutableLiveData<Boolean>()
    val loadingCompleted: LiveData<Boolean> = _loadingCompleted

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun addStory(token: String, imageMultipart: MultipartBody.Part, description: RequestBody) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().uploadImage(
            "Bearer $token",
            imageMultipart,
            description
        )
        client.enqueue(object : Callback<NewStoryResponse> {
            override fun onResponse(
                call: Call<NewStoryResponse>,
                response: Response<NewStoryResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.error == false) {
                        _addNewStoryResult.value = responseBody
                        _isMessage.value = responseBody.message
                    } else if (responseBody != null && responseBody.error == true) {
                        _isMessage.value = responseBody.message
                        _addNewStoryResult.value = responseBody
                    }
                    _loadingCompleted.value = true
                }
            }

            override fun onFailure(call: Call<NewStoryResponse>, t: Throwable) {
                _isLoading.value = false
                _isMessage.value = "Network error: ${t.message}"
            }
        })
    }
}
