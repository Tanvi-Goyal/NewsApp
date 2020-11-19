package com.newsapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.newsapp.data.local.AppDatabase
import com.newsapp.databinding.ActivityMainBinding
import com.newsapp.ui.main.viewmodel.HeadlinesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: HeadlinesViewModel by viewModels()

    companion object {
        val tagList = arrayListOf<String>(
            "technology", "sports", "science", "health", "general", "entertainment", "business"
        )
    }

    fun getTagList(): ArrayList<String> = tagList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onDestroy() {
        viewModel.disposeElements()
        super.onDestroy()
    }
}