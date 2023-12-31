package com.dicoding.picodiploma.loginwithanimation.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.picodiploma.loginwithanimation.data.api.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StoryPagingSource(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) : PagingSource<Int, ListStoryItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        try {
            val page = params.key ?: 1
            val token = userPreference.getSession().first().token

            Log.d("StoryPagingSource", "Token: $token")
            val response = withContext(Dispatchers.IO) {
                apiService.getStories("Bearer $token", page).execute()
            }

            Log.d("StoryPagingSource", "Response: ${response.body()}")
            val stories = response.body()?.listStory ?: emptyList()

            return LoadResult.Page(
                data = stories,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (stories.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Log.e("StoryPagingSource", "Error: ${e.message}")
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
