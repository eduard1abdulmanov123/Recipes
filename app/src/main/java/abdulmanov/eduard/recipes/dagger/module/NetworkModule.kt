package abdulmanov.eduard.recipes.dagger.module

import dagger.Module
import dagger.Provides
import okhttp3.MediaType
import okhttp3.OkHttpClient
import javax.inject.Singleton
import okhttp3.MediaType.Companion.toMediaType

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient():OkHttpClient{
        return OkHttpClient()
    }

    @Singleton
    @Provides
    fun provideMediaType():MediaType{
        return "application/json; charset=utf-8".toMediaType()
    }
}