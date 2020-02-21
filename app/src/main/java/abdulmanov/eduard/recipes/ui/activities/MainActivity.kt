package abdulmanov.eduard.recipes.ui.activities

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.network.DetailsRecipeService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    companion object{
        private val azu = "https://eda.ru/recepty/osnovnye-blyuda/azu-po-tatarski-21751"
        private val blin = "https://eda.ru/recepty/zavtraki/amerikanskie-bliny-30600"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recipesService = DetailsRecipeService()

        Thread {
            recipesService.getDetailsRecipe(azu)
        }.start()
    }
}
