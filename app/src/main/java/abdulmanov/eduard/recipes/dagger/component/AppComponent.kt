package abdulmanov.eduard.recipes.dagger.component

import abdulmanov.eduard.recipes.dagger.module.*
import abdulmanov.eduard.recipes.presentation.ui.fragments.category.CategoryScreenFragment
import abdulmanov.eduard.recipes.presentation.ui.fragments.main.MainScreenFragment
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

    fun inject(mainScreenFragment: MainScreenFragment)

    fun inject(categoryScreenFragment:CategoryScreenFragment)
}