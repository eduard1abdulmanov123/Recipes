package abdulmanov.eduard.recipes.dagger.module

import abdulmanov.eduard.recipes.presentation.ui.recipes.mappers.RecipesPresentationModelMapper
import abdulmanov.eduard.recipes.presentation.ui.recipes.mappers.RecipesPresentationModelMapperImpl
import dagger.Binds
import dagger.Module

@Module
abstract class MapperModule {

    @Binds
    abstract fun bindRecipesViewModelMapper(recipesViewModelMapperImpl: RecipesPresentationModelMapperImpl): RecipesPresentationModelMapper
}