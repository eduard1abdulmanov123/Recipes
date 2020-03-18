package abdulmanov.eduard.recipes.dagger.module

import abdulmanov.eduard.recipes.presentation.ui.base.ViewModelFactory
import abdulmanov.eduard.recipes.presentation.ui.fragments.category.CategoryScreenViewModel
import abdulmanov.eduard.recipes.presentation.ui.fragments.main.MainScreenViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainScreenViewModel::class)
    abstract fun bindMainScreenViewModel(mainScreenViewModel: MainScreenViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoryScreenViewModel::class)
    abstract fun bindCategoryScreenViewModel(categoryScreenViewModel: CategoryScreenViewModel):ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}