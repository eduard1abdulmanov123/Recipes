package abdulmanov.eduard.recipes.presentation.ui.fragments.container

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.app.BaseApp
import abdulmanov.eduard.recipes.presentation.navigation.BackButtonListener
import abdulmanov.eduard.recipes.presentation.navigation.LocalCiceroneHolder
import abdulmanov.eduard.recipes.presentation.navigation.RouterProvide
import abdulmanov.eduard.recipes.presentation.navigation.Screens
import android.content.Context
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import javax.inject.Inject

class RecipesContainerFragment : Fragment(R.layout.fragment_recipes_container),RouterProvide,BackButtonListener {

    @Inject
    lateinit var ciceroneHolder: LocalCiceroneHolder

    private val cicerone:Cicerone<Router> by lazy {
        ciceroneHolder.getCicerone(CONTAINER_NAME)
    }

    private val navigator:Navigator by lazy {
        SupportAppNavigator(requireActivity(),childFragmentManager,R.id.recipes_container)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as BaseApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(childFragmentManager.findFragmentById(R.id.recipes_container) == null){
            cicerone.router.replaceScreen(Screens.MainScreen)
        }
    }

    override fun onResume() {
        super.onResume()
        cicerone.navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        cicerone.navigatorHolder.removeNavigator()
    }

    override fun getRouter(): Router {
        return cicerone.router
    }

    override fun onBackPressed(): Boolean {
        val fragment = childFragmentManager.findFragmentById(R.id.recipes_container)
        return if (fragment != null && (fragment is BackButtonListener) && fragment.onBackPressed()) {
            true
        } else {
            (requireActivity() as RouterProvide).getRouter().exit()
            true
        }
    }

    companion object{
        private const val CONTAINER_NAME = "RECIPES"
    }
}
