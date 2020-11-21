package com.newsapp.ui.main.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.newsapp.data.entities.NewsModel
import com.newsapp.data.repositories.GetNewsRepository
import com.newsapp.data.repositories.GetNewsRepositoryImpl
import io.reactivex.Flowable

class HeadlinesViewModel @ViewModelInject constructor(
    private val repository: GetNewsRepositoryImpl
) : ViewModel() {

//    var newsResult: MutableLiveData<List<News>> = MutableLiveData()
//    var newsError: MutableLiveData<String> = MutableLiveData()
//    lateinit var disposableObserver: DisposableObserver<List<News>>
//
//    fun getNewsResult(): LiveData<List<News>> {
//        return newsResult
//    }
//
//    fun getNewsError(): LiveData<String> {
//        return newsError
//    }
//
//    fun getHeadlines(category: String) {
//        disposableObserver = object : DisposableObserver<List<News>>() {
//            override fun onComplete() {
//            }
//
//            override fun onNext(news: List<News>) {
//                newsResult.postValue(news)
//            }
//
//            override fun onError(e: Throwable) {
//                newsError.postValue(e.message)
//            }
//        }
//
//        repository.getHeadlines(category)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .debounce(400, TimeUnit.MILLISECONDS)
//            .subscribe(disposableObserver)
//    }
//
//    fun disposeElements() {
//        if (null != disposableObserver && !disposableObserver.isDisposed) disposableObserver.dispose()
//    }

    fun getNews(category: String): Flowable<PagingData<NewsModel.News>> {
        return repository
            .getNews(category)
            .cachedIn(viewModelScope)
    }

//    fun addToFavorites(id: Int, isFavorite: Int) {
//        repository.addToFavorites(id, isFavorite).subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe()
//    }
//
//    fun deleteDB() {
//        repository.deleteDB().observeOn(AndroidSchedulers.mainThread()).subscribe()
//    }
}
