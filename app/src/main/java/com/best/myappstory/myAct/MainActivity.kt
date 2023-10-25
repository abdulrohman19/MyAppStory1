package com.best.myappstory.myAct

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.best.myappstory.R
import com.best.myappstory.databinding.ActivityMainBinding
import com.best.myappstory.myResponse.ListStoryItem
import com.best.myappstory.myStories.Story
import com.best.myappstory.myStories.StoryAdapter
import com.best.myappstory.myViewModels.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvStories.layoutManager = LinearLayoutManager(this)
        val token = getSharedPreferences("LoginSession", Context.MODE_PRIVATE).getString("token", "")
        viewModel.getStories(token.toString())
        viewModel.isLoading.observe(this) {isLoading -> showLoading(isLoading)}
        viewModel.listStory.observe(this) {listStory -> setUserStories(listStory)}

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddAct::class.java)
            startActivity(intent)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_app, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_setting -> {
                getSharedPreferences("LoginSession", Context.MODE_PRIVATE).edit().clear().apply()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finishAffinity()
            }
        }
        return true
    }
    private fun setUserStories(stories: List<ListStoryItem>) {
        val list = ArrayList<Story>()
        for (story in stories) {
            val storyData = Story(story.id.toString(), story.name.toString(), story.photoUrl.toString(), story.description.toString())
            list.add(storyData)
        }

        val listStory = StoryAdapter(list)
        binding.rvStories.adapter = listStory

        listStory.setOnItemClickCallback(object: StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Story) {
                val storyDetail = Story(data.id, data.userName, data.avtUrl, data.desc)
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STORY, storyDetail)
                startActivity(intent)
            }
        })
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadMain.visibility = View.VISIBLE
        } else {
            binding.loadMain.visibility = View.INVISIBLE
        }
    }
}