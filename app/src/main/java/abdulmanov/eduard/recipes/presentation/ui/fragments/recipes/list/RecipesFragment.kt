package abdulmanov.eduard.recipes.presentation.ui.fragments.recipes.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.common.ListState
import abdulmanov.eduard.recipes.presentation.common.visibilityGone
import abdulmanov.eduard.recipes.presentation.ui.adapters.RecipesDelegateAdapter
import abdulmanov.eduard.recipes.presentation.ui.model.CategoryViewModel
import abdulmanov.eduard.recipes.presentation.ui.model.RecipeViewModel
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.delegateadapter.delegate.diff.DiffUtilCompositeAdapter
import kotlinx.android.synthetic.main.fragment_recipes.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_progress_bar.*
import kotlin.random.Random

class RecipesFragment : Fragment() {

    companion object{
        private const val CATEGORY = "CATEGORY"

        fun newInstance(category:CategoryViewModel):RecipesFragment{
            return RecipesFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CATEGORY,category)
                }
            }
        }
    }

    private lateinit var category:CategoryViewModel

    private lateinit var viewModel:RecipesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getParcelable(CATEGORY)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recipes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RecipesViewModel::class.java)
        viewModel.state.observe(viewLifecycleOwner, Observer {
            when(it){
                is ListState.EmptyProgressState->{
                    layout_progress_bar.visibilityGone(true)
                    layout_error.visibilityGone(false)
                    recipes_recycler_view.visibilityGone(false)
                }
                is ListState.EmptyErrorState->{
                    layout_error.visibilityGone(true)
                    layout_progress_bar.visibilityGone(false)
                    recipes_recycler_view.visibilityGone(false)
                    error_secondary_message.setText(it.message)
                }
                is ListState.DataState<*>->{
                    recipes_recycler_view.visibilityGone(true)
                    layout_progress_bar.visibilityGone(false)
                    layout_error.visibilityGone(false)
                    (recipes_recycler_view.adapter as DiffUtilCompositeAdapter).swapData(it.data as List<RecipeViewModel>)
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUI()
        viewModel.loadNewPage()
    }

    private fun initUI(){
        iniToolbar()
        initRecyclerView()
    }

    private fun iniToolbar(){
        recipes_toolbar.run {
            title = category.name
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private fun initRecyclerView(){
        recipes_recycler_view.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = DiffUtilCompositeAdapter.Builder()
                .add(RecipesDelegateAdapter{})
                .build()
        }
    }

    private fun getMockData():List<RecipeViewModel>{
        return mutableListOf<RecipeViewModel>().apply {
            for(i in 0..20){
               val type = Random.nextInt(0,3)
                add(when(type){
                    0 -> RecipeViewModel(
                        i.toLong(),
                        "Брауни (brownie)",
                        "https://img05.rl0.ru/eda/c285x285i/s1.eda.ru/StaticContent/Photos/120131082911/130725174021/p_O.jpg",
                        "6 ингредиентов",
                        "6 порций",
                        "40 минут",
                        "3038",
                        "250"
                    )

                    1 -> RecipeViewModel(
                        i.toLong(),
                        "Сырники из творога",
                        "https://img03.rl0.ru/eda/c285x285i/s2.eda.ru/StaticContent/Photos/120213175531/180415114517/p_O.jpg",
                        "5 ингредиентов",
                        "2 порции",
                        "30 минут",
                        "2858",
                        "280"
                    )

                    2 -> RecipeViewModel(
                        i.toLong(),
                        "Сырный суп по‑французски с курицей",
                        "https://img05.rl0.ru/eda/c285x285i/s2.eda.ru/StaticContent/Photos/120131083619/170816150250/p_O.jpg",
                        "12 ингредиентов",
                        "4 порции",
                        "1 час",
                        "2661",
                        "246"
                    )
                    else -> RecipeViewModel(
                        i.toLong(),
                        "Сырный суп по‑французски с курицей",
                        "https://img05.rl0.ru/eda/c285x285i/s2.eda.ru/StaticContent/Photos/120131083619/170816150250/p_O.jpg",
                        "12 ингредиентов",
                        "4 порции",
                        "1 час",
                        "2661",
                        "246"
                    )
                })
            }
        }
    }
}
