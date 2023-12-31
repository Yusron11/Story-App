package com.dicoding.picodiploma.loginwithanimation.view.main

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.camera.CameraActivity
import com.dicoding.picodiploma.loginwithanimation.view.detail.DetailActivity
import com.dicoding.picodiploma.loginwithanimation.view.maps.MapsActivity
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                setupView()
                setupRecyclerView()
                setupFab()

            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.apply {
            title = "MY STORY APP"
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@MainActivity, R.color.bluesky)))
        }
    }

    private fun setupRecyclerView() {
        storyAdapter = StoryAdapter { story ->
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_STORY, story as Parcelable)
            startActivity(intent)
        }

        binding.rvListStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter
            viewModel.story.observe(this@MainActivity) {
                storyAdapter.submitData(lifecycle, it)
            }
        }

    }


    private fun setupFab() {
        binding.btnAddNewStory.setOnClickListener {
            startActivity(Intent(this@MainActivity, CameraActivity::class.java))
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                viewModel.logout()
                return true
            }
            R.id.mapsIcon -> {
                startActivity(Intent(this, MapsActivity::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


}
