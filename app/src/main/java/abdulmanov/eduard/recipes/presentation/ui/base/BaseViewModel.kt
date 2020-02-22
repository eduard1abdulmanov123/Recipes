package abdulmanov.eduard.recipes.presentation.ui.base

import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

abstract class BaseViewModel: ViewModel() {

    private val disposable = CompositeDisposable()

    protected fun <T> Single<T>.safeSubscribe(
        onSuccess: (T) -> Unit,
        onError: (error: Throwable) -> Unit
    ) {
        disposable.add(
            subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess, onError)
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}