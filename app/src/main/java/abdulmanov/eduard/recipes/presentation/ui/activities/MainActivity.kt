package abdulmanov.eduard.recipes.presentation.ui.activities

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.navigation.BackButtonListener
import abdulmanov.eduard.recipes.presentation.ui.fragments.container.RecipesContainerFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().apply {
            val fragment = RecipesContainerFragment()
            replace(R.id.mainContainer,fragment)
        }.commit()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.mainContainer)
        if(fragment!=null&&(fragment is BackButtonListener)&&fragment.onBackPressed()){
            return
        }else {
            super.onBackPressed()
        }
    }
}
