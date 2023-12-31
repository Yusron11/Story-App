package com.dicoding.picodiploma.loginwithanimation.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.picodiploma.loginwithanimation.data.api.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.api.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.paging.StoryPagingSource
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext


class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun getStoriesPaging(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { StoryPagingSource(apiService, userPreference) }
        ).liveData
    }

    suspend fun getStoriesWithLocation(page: Int): List<ListStoryItem>? = withContext(Dispatchers.IO) {
        val token = userPreference.getSession().first().token

        try {
            val response = ApiConfig.getApiService().getStoriesWithLocation("Bearer $token", page).execute()
            if (response.isSuccessful) {
                response.body()?.listStory
            } else {
                Log.e("API_CALL_ERROR", "Error: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("API_CALL_ERROR", "Exception: ${e.message}")
            null
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        private const val PAGE_SIZE = 10

        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}
