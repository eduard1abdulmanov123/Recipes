package abdulmanov.eduard.recipes.presentation.ui.activities

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.ui.fragments.tape.TapeFragment
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

        if(savedInstanceState==null) {
            supportFragmentManager.beginTransaction().apply {
                replace(
                    R.id.main_container_id,
                    TapeFragment()
                )
            }.commit()
        }
    }
}
