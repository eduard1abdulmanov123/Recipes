package abdulmanov.eduard.recipes.presentation.app

import android.app.Application
import com.squareup.picasso.LruCache
import com.squareup.picasso.Picasso

class BaseApp:Application() {

    companion object {
        private const val MAX_CACHE = 250_000_000
    }

    override fun onCreate() {
        super.onCreate()
        initPicasso()
    }

    private fun initPicasso() {
        val picasso = Picasso.Builder(this)
            .memoryCache(LruCache(MAX_CACHE))
            .build()
        Picasso.setSingletonInstance(picasso)
    }
}