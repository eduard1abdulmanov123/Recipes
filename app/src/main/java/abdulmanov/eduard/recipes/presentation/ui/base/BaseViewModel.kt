package abdulmanov.eduard.recipes.presentation.ui.base

import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

abstract class BaseViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    protected fun <T> Single<T>.safeSubscribe(onSuccess: (T) -> Unit, onError:(Throwable) -> Unit){
        val disposable = subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess,onError)
        compositeDisposable.add(disposable)
    }

    protected fun Disposable.connect() {
        compositeDisposable.add(this)
    }
}