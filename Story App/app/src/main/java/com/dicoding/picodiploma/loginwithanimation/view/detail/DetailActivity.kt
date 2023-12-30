package com.dicoding.picodiploma.loginwithanimation.view.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailBinding
import com.dicoding.picodiploma.loginwithanimation.di.dateFormatter

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_STORY = "extra_story"
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<ListStoryItem>(EXTRA_STORY)

        if (story != null) {
            displayStoryDetails(story)
        } else {
            finish()
        }
    }

    private fun displayStoryDetails(story: ListStoryItem) {
        binding.apply {
            Glide.with(root)
                .load(story.photoUrl)
                .into(photoStory)

            tvName.text = story.name
            tvCreatedAt.text = story.createdAt?.let { dateFormatter(it) }
            tvDescStory.text = story.description
        }
    }
}
