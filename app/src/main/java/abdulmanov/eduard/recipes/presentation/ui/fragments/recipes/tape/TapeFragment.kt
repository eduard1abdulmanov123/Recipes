package abdulmanov.eduard.recipes.presentation.ui.fragments.recipes.tape

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.domain.interactors.recipes.GetTapeUseCase
import abdulmanov.eduard.recipes.presentation.app.BaseApp
import abdulmanov.eduard.recipes.presentation.ui.adapters.BestRecipesAdapter
import abdulmanov.eduard.recipes.presentation.ui.base.HorizontalItemDecoration
import abdulmanov.eduard.recipes.presentation.ui.mapper.RecipesViewModelMapperImpl
import android.content.Context
import android.os.SystemClock
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_recipes.*
import kotlinx.android.synthetic.main.fragment_tape.*
import javax.inject.Inject

class TapeFragment : Fragment() {

    @Inject
    lateinit var tapeUseCase: GetTapeUseCase

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity!!.application as BaseApp).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tape, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUI()

        val current = System.currentTimeMillis()
        tapeUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d("TapeFragment","${System.currentTimeMillis() - current}")
                    Log.d("TapeFragment","bestRecipes")
                    it.bestRecipes.forEach {
                        Log.d("TapeFragment",it.toString())
                    }
                    Log.d("TapeFragment","recipesByCategory")
                    it.recipesByCategory.forEach {
                        Log.d("TapeFragment",it.category.value + " " + it.recipes.toString())
                    }
                    val mapper = RecipesViewModelMapperImpl()
                    (tape_best_recipes_recycler_view.adapter as BestRecipesAdapter).updateItems(
                        mapper.mapRecipesToViewModels(it.bestRecipes)
                    )
                },
                {
                    Log.d("TapeFragment","error = ${it.message.toString()}")
                }
            )
    }

    private fun initUI(){
        initToolbar()
        initBestRecipesRecyclerView()
    }

    private fun initToolbar(){
        tape_toolbar.run {
            setTitle(R.string.tape_title)
        }
    }

    private fun initBestRecipesRecyclerView(){
        tape_best_recipes_recycler_view.run {
            setHasFixedSize(true)
            addItemDecoration(HorizontalItemDecoration(8,8,context))
            PagerSnapHelper().attachToRecyclerView(this)
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter = BestRecipesAdapter()
            tape_best_recipes_recycler_view_indicator.attachToRecyclerView(this)
        }
    }
}
