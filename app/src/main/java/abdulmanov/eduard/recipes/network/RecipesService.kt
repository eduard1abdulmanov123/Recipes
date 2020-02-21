package abdulmanov.eduard.recipes.network

import abdulmanov.eduard.recipes.network.model.*
import android.util.Log
import org.json.JSONObject
import org.jsoup.Jsoup

class RecipesService {

    companion object{
        private const val BASE_URL = "https://eda.ru/"
        private const val RECEPTY = "recepty"
    }

    fun getCategories():List<Category>{
        return Jsoup.connect(BASE_URL).get().run {
            select("div.tag-selector__selects-box")[0]
                .child(0)
                .select("ul.select-suggest__result.js-select-suggest__result")[0]
                .children()
                .map {
                    Category(
                        it.attr("data-select-suggest-value"),
                        it.html()
                    )
                }
        }
    }

    fun getRecipes(category:String,page:Int):List<ShortRecipe>{
        return Jsoup.connect("$BASE_URL/$RECEPTY/$category?page=$page").get().run {
            select("div.tile-list__horizontal-tile.horizontal-tile.js-portions-count-parent.js-bookmark__obj")
                .map {
                    val title = it.select("h3.horizontal-tile__item-title.item-title")[0]
                        .child(0)
                        .child(0)
                        .html().replace("&nbsp;"," ")

                    val link = it.select("h3.horizontal-tile__item-title.item-title")[0]
                        .child(0)
                        .attr("href")

                    val image = it.select("div.horizontal-tile__preview")[0]
                        .child(0)
                        .child(1)
                        .attr("data-src")

                    val countIngredients = it.select("div.horizontal-tile__item-specifications")[0]
                        .child(0)
                        .text()

                    val countPortion = it.select("div.horizontal-tile__item-specifications")[0]
                        .child(1)
                        .text()

                    val time:String = it.select("div.horizontal-tile__item-specifications")[0]
                        .child(2)
                        .text()

                    val countLike = it.select("span.widget-list__like-count")[0]
                        .child(1)
                        .text()

                    val countDislike = it.select("span.widget-list__like-count.widget-list__like-count_dislike")[0]
                        .child(1)
                        .text()

                    ShortRecipe(
                        title,
                        countIngredients,
                        countPortion,
                        time,
                        countLike,
                        countDislike,
                        link,
                        image
                    )
                }
        }
    }

    fun getRecipe(url:String){
        return Jsoup.connect(url).get().run {
            val title = select("h1.recipe__name.g-h1").text()
            val video = select("div.recipe__video.js-video-sticky.js-video-container")
                .select("iframe")
                .attr("src")
            val description = select("p.recipe__description.layout__content-col._default-mod").text()

            val naturalList = select("ul.nutrition__list")[0]
                .children()
                .map {
                    Natural(
                        it.select("p.nutrition__name").text(),
                        it.select("p.nutrition__weight").text(),
                        it.select("p.nutrition__percent").text()
                    )
                }

            val ingredientList = select("div.ingredients-list__content")[0]
                .children()
                .map {
                   /* val jsonObject = JSONObject(it.attr("data-ingredient-object"))
                    Ingredient(
                        jsonObject.getString("id"),
                        jsonObject.getString("name"),
                        jsonObject.getString("amount")
                    )*/
                }

            val time = select("div.instruction-controls")[0]
                .child(1)
                .text()

            val steps = select("ul.recipe__steps")[0]
                .children()
                .map {
                    Step(
                        it.attr("data-counter").toInt(),
                        it.select("img.g-print-visible").attr("src"),
                        it.select("span.instruction__description.js-steps__description").text().substring(3)
                    )
                }

            Log.d("RecipesService", "title = $title")
            Log.d("RecipesService", "video = $video")
            Log.d("RecipesService", "description = $description")
            Log.d("RecipesService", "naturalList = $naturalList")
            Log.d("RecipesService", "ingredientList = $ingredientList")
            Log.d("RecipesService", "time = $time")
            Log.d("RecipesService", "steps = $steps")
        }
    }
}