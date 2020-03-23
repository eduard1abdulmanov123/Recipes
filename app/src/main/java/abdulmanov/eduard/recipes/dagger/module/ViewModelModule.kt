package abdulmanov.eduard.recipes.dagger.module

import abdulmanov.eduard.recipes.presentation.ui.base.ViewModelFactory
import abdulmanov.eduard.recipes.presentation.ui.fragments.category.CategoryViewModel
import abdulmanov.eduard.recipes.presentation.ui.fragments.tape.TapeViewModel
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
    abstract fun bindMainScreenViewModel(tapeViewModel: TapeViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    abstract fun bindCategoryScreenViewModel(categoryViewModel: CategoryViewModel):ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}