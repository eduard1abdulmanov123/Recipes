package abdulmanov.eduard.recipes.presentation.navigation

import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

class LocalCiceroneHolder {

    private val containers:HashMap<String, Cicerone<Router>> = hashMapOf()

    fun getCicerone(containerTag:String):Cicerone<Router>{
        if(!containers.containsKey(containerTag)){
            containers[containerTag] = Cicerone.create()
        }
        return containers.getValue(containerTag)
    }
}