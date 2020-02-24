package abdulmanov.eduard.recipes.presentation.dagger.module

import abdulmanov.eduard.recipes.data.network.DetailsRecipeService
import abdulmanov.eduard.recipes.data.network.RecipesService
import abdulmanov.eduard.recipes.data.repository.RecipesRepositoryImpl
import abdulmanov.eduard.recipes.domain.interactors.recipes.GetDetailsRecipeUseCase
import abdulmanov.eduard.recipes.domain.interactors.recipes.GetRecipesUseCase
import abdulmanov.eduard.recipes.domain.repositories.RecipesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {

    @Singleton
    @Provides
    fun provideGetDetailsRecipeUseCase(repository: RecipesRepository):GetDetailsRecipeUseCase{
        return GetDetailsRecipeUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetRecipesUseCase(repository: RecipesRepository):GetRecipesUseCase{
        return GetRecipesUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideRecipesRepository(
        recipesService: RecipesService,
        detailsRecipeService: DetailsRecipeService
    ):RecipesRepository{
        return RecipesRepositoryImpl(recipesService,detailsRecipeService)
    }
}