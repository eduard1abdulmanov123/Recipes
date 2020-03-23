package abdulmanov.eduard.recipes.presentation.ui.activities

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.app.BaseApp
import abdulmanov.eduard.recipes.presentation.navigation.BackButtonListener
import abdulmanov.eduard.recipes.presentation.navigation.RouterProvide
import abdulmanov.eduard.recipes.presentation.navigation.Screens
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Replace
import javax.inject.Inject

class MainActivity : AppCompatActivity(), RouterProvide {

    @Inject
    lateinit var mainRouter: Router

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private val navigator = SupportAppNavigator(this,R.id.main_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as BaseApp).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null){
            navigator.applyCommands(arrayOf<Command>(Replace(Screens.RecipesContainer)))
        }
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.main_container)
        if(fragment!=null&&(fragment is BackButtonListener)&&fragment.onBackPressed()){
            return
        }else {
            super.onBackPressed()
        }
    }

    override fun getRouter(): Router {
        return mainRouter
    }
}
