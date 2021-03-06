package abdulmanov.eduard.recipes.dagger.module

import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

@Module
class NavigationModule() {

    private val cicerone: Cicerone<Router> = Cicerone.create()

    @Singleton
    @Provides
    fun provideRouter(): Router {
        return cicerone.router
    }

    @Singleton
    @Provides
    fun provideNavigationHolder(): NavigatorHolder {
        return cicerone.navigatorHolder
    }
}