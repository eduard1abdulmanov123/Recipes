package abdulmanov.eduard.recipes.dagger.module

import abdulmanov.eduard.recipes.data.network.CategoriesService
import abdulmanov.eduard.recipes.data.network.DetailsRecipeService
import abdulmanov.eduard.recipes.data.network.RecipesService
import dagger.Module
import dagger.Provides
import okhttp3.MediaType
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideCategoriesService(): CategoriesService {
        return CategoriesService()
    }

    @Singleton
    @Provides
    fun provideRecipesService(): RecipesService {
        return RecipesService()
    }

    @Singleton
    @Provides
    fun provideDetailsRecipesService(client: OkHttpClient, mediaType: MediaType): DetailsRecipeService {
        return DetailsRecipeService(client, mediaType)
    }
}