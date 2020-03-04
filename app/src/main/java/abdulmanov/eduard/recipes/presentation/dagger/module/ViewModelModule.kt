package abdulmanov.eduard.recipes.presentation.dagger.module

import abdulmanov.eduard.recipes.presentation.ui.base.ViewModelFactory
import abdulmanov.eduard.recipes.presentation.ui.fragments.list.RecipeListViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RecipeListViewModel::class)
    abstract fun bindUserViewModel(recipeListViewModel: RecipeListViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}