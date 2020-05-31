package abdulmanov.eduard.recipes.dagger.module

import abdulmanov.eduard.recipes.presentation.ui.base.ViewModelFactory
import abdulmanov.eduard.recipes.presentation.ui.recipes.category.CategoryViewModel
import abdulmanov.eduard.recipes.presentation.ui.recipes.details.DetailsRecipeViewModel
import abdulmanov.eduard.recipes.presentation.ui.recipes.tape.TapeViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TapeViewModel::class)
    abstract fun bindTapeViewModel(tapeViewModel: TapeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    abstract fun bindCategoryViewModel(categoryViewModel: CategoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsRecipeViewModel::class)
    abstract fun bindDetailsRecipeViewModel(detailsRecipeViewModel: DetailsRecipeViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}