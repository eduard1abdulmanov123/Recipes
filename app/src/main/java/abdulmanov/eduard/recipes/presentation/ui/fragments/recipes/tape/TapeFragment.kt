package abdulmanov.eduard.recipes.presentation.ui.fragments.recipes.tape

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.domain.interactors.recipes.GetTapeUseCase
import abdulmanov.eduard.recipes.presentation.app.BaseApp
import abdulmanov.eduard.recipes.presentation.ui.adapters.*
import abdulmanov.eduard.recipes.presentation.ui.base.VerticalItemDecoration
import abdulmanov.eduard.recipes.presentation.ui.mapper.RecipesViewModelMapper
import abdulmanov.eduard.recipes.presentation.ui.mapper.RecipesViewModelMapperImpl
import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.delegateadapter.delegate.CompositeDelegateAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_tape.*
import kotlinx.android.synthetic.main.fragment_tape.tape_toolbar
import javax.inject.Inject

class TapeFragment : Fragment() {

    @Inject
    lateinit var tapeUseCase: GetTapeUseCase

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity!!.application as BaseApp).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tape, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUI()

        val current = System.currentTimeMillis()
        val mapper:RecipesViewModelMapper = RecipesViewModelMapperImpl()
        tapeUseCase.execute()
            .map(mapper::mapTapeToViewModel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    val list = mutableListOf<IItem>()
                    list.add(it.bestRecipes)
                    list.addAll(it.recipesByCategory)
                    (tape_category_with_recipes_recycler_view.adapter as CompositeDelegateAdapter<IItem>).swapData(list)
                },
                {
                    Log.d("TapeFragment","error = ${it.message.toString()}")
                }
            )
    }

    private fun initUI(){
        initToolbar()
        initRecyclerView()
    }

    private fun initToolbar(){
        tape_toolbar.run {
            setTitle(R.string.tape_title)
        }
    }

    private fun initRecyclerView(){
        tape_category_with_recipes_recycler_view.run {
            addItemDecoration(VerticalItemDecoration(0,6,context))
            layoutManager = LinearLayoutManager(context)
            adapter = CompositeDelegateAdapter.Builder<IItem>()
                .add(BestRecipesDelegateAdapter{})
                .add(CategoryWithRecipesDelegateAdapter({},{}))
                .build()
        }
    }
}
