package abdulmanov.eduard.recipes.dagger.component

import abdulmanov.eduard.recipes.dagger.module.*
import abdulmanov.eduard.recipes.presentation.ui.main.MainActivity
import abdulmanov.eduard.recipes.presentation.ui.recipes.category.CategoryFragment
import abdulmanov.eduard.recipes.presentation.ui.recipes.container.RecipesContainerFragment
import abdulmanov.eduard.recipes.presentation.ui.recipes.details.DetailsRecipeFragment
import abdulmanov.eduard.recipes.presentation.ui.recipes.tape.TapeFragment
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        DataModule::class,
        DomainModule::class,
        ViewModelModule::class,
        MapperModule::class,
        NavigationModule::class,
        LocalNavigationModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(mainActivity: MainActivity)

    fun inject(recipesContainerFragment: RecipesContainerFragment)

    fun inject(tapeFragment: TapeFragment)

    fun inject(categoryFragment: CategoryFragment)

    fun inject(detailsRecipeFragment: DetailsRecipeFragment)
}