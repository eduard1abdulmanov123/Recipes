package abdulmanov.eduard.recipes.dagger.module

import abdulmanov.eduard.recipes.presentation.ui.mapper.RecipesViewModelMapper
import abdulmanov.eduard.recipes.presentation.ui.mapper.RecipesViewModelMapperImpl
import dagger.Binds
import dagger.Module

@Module
abstract class MapperModule {

    @Binds
    abstract fun bindRecipesViewModelMapper(recipesViewModelMapperImpl: RecipesViewModelMapperImpl): RecipesViewModelMapper

}