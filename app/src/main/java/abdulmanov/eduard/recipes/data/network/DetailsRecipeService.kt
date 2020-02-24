package abdulmanov.eduard.recipes.data.network

import abdulmanov.eduard.recipes.domain.models.DetailsRecipe
import abdulmanov.eduard.recipes.domain.models.Ingredient
import abdulmanov.eduard.recipes.domain.models.Natural
import abdulmanov.eduard.recipes.domain.models.Step
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject


class DetailsRecipeService(
    private val client:OkHttpClient,
    private val mediaType: MediaType
) {

    /*private val client = OkHttpClient()
    private val mediaType = "application/json; charset=utf-8".toMediaType()*/

    fun getDetailsRecipe(url:String):DetailsRecipe{
        return Jsoup.connect(url).get().run {

            val id = getRecipeId()
            val name = getNameRecipe()
            val image = getImageUrl()
            val video = getVideoUrl()
            val photo = getPhoto(id)
            val description = getDescription()
            val naturalList = getNaturals()
            val ingredients = getIngredients(id)
            val time = getTime()
            val steps = getSteps()

            return DetailsRecipe(
                id,
                name,
                image,
                video,
                photo,
                description,
                naturalList,
                ingredients,
                time,
                steps
            )
        }
    }

    private fun Document.getRecipeId():Long{
        return select("section.recipe.js-portions-count-parent.js-recipe")
            .attr("data-recipe-id")
            .toLong()
    }

    private fun Document.getNameRecipe():String{
        return select("h1.recipe__name.g-h1")
            .text()
    }

    private fun Document.getImageUrl():String{
        return select("div.photo-list-preview.js-preview-item.js-show-gallery.trigger-gallery")[0]
            .select("img")
            .attr("src")
            .replace("c88x88i","c250x250i")
    }

    private fun Document.getVideoUrl():String?{
        return select("div.recipe__video.js-video-sticky.js-video-container")
            .select("iframe")
            .attr("src")
            .run {
                ifEmptyReturnNull()
            }
    }

    private fun Document.getDescription():String?{
        return select("p.recipe__description.layout__content-col._default-mod")
            .text()
            .run {
                ifEmptyReturnNull()
            }
    }

    private fun Document.getNaturals():List<Natural>{
        return select("ul.nutrition__list")[0]
            .children()
            .map {
                Natural(
                    name = it.select("p.nutrition__name").text(),
                    weight = it.select("p.nutrition__weight").text(),
                    percent = it.select("p.nutrition__percent").text()
                )
            }
    }

    private fun Document.getIngredients(id:Long):List<Ingredient>{

        val portionsMap = mutableMapOf<Long,MutableList<String>>()

        for(portionCount in 1..12){

            for((key,value) in recalculatePortions(id,portionCount)){
                if(portionsMap.contains(key)){
                    portionsMap[key]!!.add(value)
                }else{
                    portionsMap[key] = mutableListOf(value)
                }
            }

        }

        return select("div.ingredients-list__content")[0]
            .children()
            .map {
                val jsonObject = JSONObject(it.attr("data-ingredient-object"))
                val ingredientId = jsonObject.getString("id").toLong()
                val name = jsonObject.getString("name")
                Ingredient(ingredientId,name,portionsMap[ingredientId]!!)
            }
    }


    private fun recalculatePortions(id:Long,portionCount:Int):Map<Long,String>{

        val content = "{recipeID:$id,portionsCount:$portionCount}"
        val body = content.toRequestBody(mediaType)
        val request = Request.Builder()
            .url("https://eda.ru/Recipe/RecalculatePortions")
            .post(body)
            .build()

        val response = client.newCall(request).execute()

        return if(response.isSuccessful){
            mutableMapOf<Long,String>().apply {
                val jsonObject = JSONObject(response.body!!.string())
                jsonObject.keys().forEach {
                    set(it.toLong(),jsonObject.getString(it))
                }
            }
        }else{
            mapOf()
        }
    }

    private fun Document.getTime():String{
        return select("div.instruction-controls")[0]
            .child(1)
            .text()
    }

    private fun Document.getSteps():List<Step>{
        return select("ul.recipe__steps")[0]
            .children()
            .map {
                Step(
                    counter = it.attr("data-counter").toInt(),
                    image = it.select("img.g-print-visible").attr("src"),
                    description = it.select("span.instruction__description.js-steps__description")
                        .text()
                        .substring(3)
                )
            }
    }

    private fun getPhoto(id:Long):List<String>{

        val request = Request.Builder()
            .url("https://eda.ru/RecipePhoto/List?recipeID=$id")
            .post("".toRequestBody())
            .build()

        val response = client.newCall(request).execute()

        return if(response.isSuccessful){
            mutableListOf<String>().apply {
                val jsonArray = JSONArray(response.body!!.string())
                for(index in 0 until jsonArray.length()){
                    val jsonObject = jsonArray.getJSONObject(index)
                    add(jsonObject.getString("img").replace("-x900i","-x200i"))
                }
            }
        }else{
            listOf()
        }
    }

    private fun String.ifEmptyReturnNull():String?{
        return if (isNotEmpty())
            this
        else
            null
    }
}