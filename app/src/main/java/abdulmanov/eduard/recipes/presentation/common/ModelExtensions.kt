package abdulmanov.eduard.recipes.presentation.common

import abdulmanov.eduard.recipes.R
import java.lang.Exception

fun Throwable.handleError():Int{
    if(message.toString() == "Unable to resolve host \"eda.ru\": No address associated with hostname"){
        return R.string.error_network
    }else{
        throw Exception("This error was not expected to be sorted out = ${message.toString()}")
    }
}