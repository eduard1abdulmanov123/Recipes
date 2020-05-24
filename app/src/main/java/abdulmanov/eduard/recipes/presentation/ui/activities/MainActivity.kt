package abdulmanov.eduard.recipes.presentation.ui.activities

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.navigation.BackButtonListener
import abdulmanov.eduard.recipes.presentation.ui.fragments.container.RecipesContainerFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_container, RecipesContainerFragment())
        }.commit()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.main_container)
        if (fragment != null && (fragment is BackButtonListener) && fragment.onBackPressed()) {
            return
        } else {
            super.onBackPressed()
        }
    }
}