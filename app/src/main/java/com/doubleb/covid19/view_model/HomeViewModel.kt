package com.doubleb.covid19.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doubleb.covid19.model.BaseData
import com.doubleb.covid19.repository.CountryRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class HomeViewModel(
    private val countryRepository: CountryRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {
    private var subscription: Disposable? = null

    val liveData = MutableLiveData<DataSource<List<BaseData>>>()

    private var baseDataList : List<BaseData>? = null

    fun getByCountry(target: String) {
        cancelPolling()
        subscription = Observable.interval(0, 2, TimeUnit.MINUTES).map {
            compositeDisposable.add(
                countryRepository.getCountry(target)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        liveData.postValue(DataSource(DataState.LOADING))
                    }
                    .subscribeWith(object : DisposableObserver<List<BaseData>>() {
                        override fun onComplete() {
                            liveData.postValue(DataSource(DataState.SUCCESS, baseDataList))
                        }

                        override fun onNext(t: List<BaseData>?) {
                            baseDataList = t
                        }

                        override fun onError(e: Throwable?) {
                            liveData.postValue(DataSource(DataState.ERROR, throwable = e))
                        }

                    })
            )
        }.subscribe()
    }

    fun clearViewModel() {
        compositeDisposable.clear()
        cancelPolling()
    }

    override fun onCleared() {
        super.onCleared()
        clearViewModel()
    }

    private fun cancelPolling(){
        subscription?.dispose()
    }
}