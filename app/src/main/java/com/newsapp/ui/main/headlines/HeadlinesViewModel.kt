package com.newsapp.ui.main.headlines

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newsapp.data.entities.News
import com.newsapp.data.repositories.NewsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class HeadlinesViewModel @ViewModelInject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    var newsResult: MutableLiveData<List<News>> = MutableLiveData()
    var newsError: MutableLiveData<String> = MutableLiveData()
    lateinit var disposableObserver: DisposableObserver<List<News>>

    fun getNewsResult(): LiveData<List<News>> {
        return newsResult
    }

    fun getNewsError(): LiveData<String> {
        return newsError
    }

    fun getHeadlines(category: String) {
        disposableObserver = object : DisposableObserver<List<News>>() {
            override fun onComplete() {
            }

            override fun onNext(news: List<News>) {
                newsResult.postValue(news)
            }

            override fun onError(e: Throwable) {
                newsError.postValue(e.message)
            }
        }

        repository.getHeadlines(category)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, TimeUnit.MILLISECONDS)
            .subscribe(disposableObserver)
    }

    fun disposeElements() {
        if (null != disposableObserver && !disposableObserver.isDisposed) disposableObserver.dispose()
    }

    fun addToFavorites(id: Int, isFavorite: Int) {
        repository.addToFavorites(id, isFavorite).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}
