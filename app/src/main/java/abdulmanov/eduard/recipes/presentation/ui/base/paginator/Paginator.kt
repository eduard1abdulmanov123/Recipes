package abdulmanov.eduard.recipes.presentation.ui.base.paginator

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class Paginator<E,VM>(
    private val requestFactory:(page:Int) -> Single<List<E>>,
    private val mapper:(List<E>) -> List<VM>,
    private val controller:Controller<VM>
) {

    private val FIRST_PAGE = 1
    private var currentPage = 0
    private val data = mutableListOf<VM>()
    private var requestDisposable: Disposable? = null

    private var currentState: State<VM> = StartState()
        set(value) {
            field = value
            controller.changeState(field)
        }

    fun refresh() {
        currentState.refresh()
    }

    fun loadNewPage() {
        currentState.loadNewPage()
    }

    fun release() {
        requestDisposable?.dispose()
    }

    private fun loadPage(page: Int, swap: Boolean = false) {
        requestDisposable?.dispose()
        requestDisposable = requestFactory.invoke(page)
            .map(mapper)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (swap)
                        data.clear()
                    data.addAll(it)
                    currentState.newData(data)
                },
                {
                    currentState.fail(it)
                }
            )
    }

    inner class StartState :
        State<VM> {

        override fun refresh() {
            currentState = StartProgressState()
            loadPage(FIRST_PAGE)
        }

    }

    inner class StartProgressState :
        State<VM> {

        override fun newData(data: List<VM>) {
            if (data.isEmpty()) {
                currentState = StartEmptyState()
            } else {
                currentState = DataState(data)
                currentPage = FIRST_PAGE
            }
        }

        override fun fail(error: Throwable) {
            currentState = StartErrorState(error)
        }

    }

    inner class StartErrorState(val error: Throwable) :
        State<VM> {

        override fun refresh() {
            currentState = StartProgressAfterErrorState()
            loadPage(FIRST_PAGE)
        }

    }

    inner class StartProgressAfterErrorState :
        State<VM> {

        override fun newData(data: List<VM>) {
            if (data.isEmpty()) {
                currentState = StartEmptyState()
            } else {
                currentState = DataState(data)
                currentPage =
                    FIRST_PAGE
            }
        }

        override fun fail(error: Throwable) {
            currentState = StartErrorState(error)
        }

    }

    inner class StartEmptyState : State<VM>

    inner class DataState(val data: List<VM>) : State<VM>

}