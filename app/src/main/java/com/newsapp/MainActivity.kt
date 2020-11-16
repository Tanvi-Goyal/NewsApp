package com.newsapp

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.newsapp.data.entities.News
import com.newsapp.databinding.ActivityMainBinding
import com.newsapp.ui.main.headlines.HeadlinesViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: HeadlinesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel.getHeadlines()

        viewModel.getNewsResult().observe(this, Observer {
            Log.wtf("NEWS", it[0].content)
        })

        viewModel.getNewsError().observe(this, Observer<String>{
            Log.wtf("NEWS", it)
        })
    }

    override fun onDestroy() {
        viewModel.disposeElements()
        super.onDestroy()
    }
}