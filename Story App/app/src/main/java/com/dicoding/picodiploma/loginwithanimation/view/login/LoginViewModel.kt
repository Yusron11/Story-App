package com.dicoding.picodiploma.loginwithanimation.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.api.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginSuccess = MutableLiveData<Unit>()
    val loginSuccess: LiveData<Unit> = _loginSuccess

    private val _loginError = MutableLiveData<String>()
    val loginError: LiveData<String> = _loginError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun loginUser(email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _loginSuccess.value = Unit

                    response.body()?.loginResult?.token?.let { token ->
                        saveSession(UserModel(email, token))
                    }
                } else {

                    val errorBody = response.errorBody()?.string()
                    Log.e("LoginViewModel", "Login failed: $errorBody")
                    _loginError.value = "Periksa Kembali Email dan Password Anda"
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("LoginViewModel", "Login failed", t)
                _loginError.value = "Periksa Kembali Email dan Password Anda"
            }
        })
    }
}
