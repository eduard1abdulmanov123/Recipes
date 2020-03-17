package abdulmanov.eduard.recipes.presentation.ui.base

import abdulmanov.eduard.recipes.R
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

abstract class BaseViewModel: ViewModel() {

    companion object{
        private const val NETWORK_ERROR = "Unable to resolve host \"eda.ru\": No address associated with hostname"
    }

    val disposable = CompositeDisposable()

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

    protected fun handleError(error:Throwable):Int{
        if(error.message.toString() == NETWORK_ERROR){
            return R.string.error_network
        }else{
            throw Exception("This error was not expected to be sorted out")
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}