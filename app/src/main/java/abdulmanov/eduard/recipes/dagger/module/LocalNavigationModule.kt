package abdulmanov.eduard.recipes.dagger.module

import abdulmanov.eduard.recipes.presentation.navigation.LocalCiceroneHolder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalNavigationModule {

    @Singleton
    @Provides
    fun provideLocalNavigationHolder():LocalCiceroneHolder{
        return LocalCiceroneHolder()
    }

}