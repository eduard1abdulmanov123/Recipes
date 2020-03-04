package abdulmanov.eduard.recipes.presentation.dagger.component

import abdulmanov.eduard.recipes.presentation.dagger.module.*
import abdulmanov.eduard.recipes.presentation.ui.fragments.list.RecipeListFragment
import abdulmanov.eduard.recipes.presentation.ui.fragments.tape.TapeFragment
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class,DataModule::class,DomainModule::class,ViewModelModule::class,MapperModule::class])
interface AppComponent{

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(recipeListFragment: RecipeListFragment)

    fun inject(tapeFragment: TapeFragment)
}