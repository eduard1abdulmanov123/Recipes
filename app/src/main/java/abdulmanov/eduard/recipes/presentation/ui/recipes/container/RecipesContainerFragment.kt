package abdulmanov.eduard.recipes.presentation.ui.recipes.container

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.app.BaseApp
import abdulmanov.eduard.recipes.presentation.navigation.*
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import javax.inject.Inject

class RecipesContainerFragment : Fragment(R.layout.fragment_recipes_container), RouterProvide,
    BackButtonListener {

    @Inject
    lateinit var ciceroneHolder: LocalCiceroneHolder

    private val cicerone: Cicerone<Router> by lazy {
        ciceroneHolder.getCicerone(CONTAINER_NAME)
    }

    private val navigator:Navigator by lazy {
       object : ContainerNavigator(childFragmentManager,R.id.recipesContainer){
           override fun setupFragmentTransaction(command: Command, currentFragment: Fragment?, nextFragment: Fragment?, fragmentTransaction: FragmentTransaction) {
               if(command is Forward){
                   fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
               }else if(command is Back){
                   fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
               }
           }
       }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as BaseApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(childFragmentManager.findFragmentById(R.id.recipesContainer) == null){
            cicerone.router.navigateTo(Screens.Tape)
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
        return if(childFragmentManager.fragments.size>1){
            val fragment = childFragmentManager.findFragmentById(R.id.recipesContainer)
            fragment!=null && (fragment is BackButtonListener) && fragment.onBackPressed()
        }else{
            false
        }
    }

    companion object {
        private const val CONTAINER_NAME = "RECIPES"
    }
}