package abdulmanov.eduard.recipes.data.network

import abdulmanov.eduard.recipes.domain.models.Recipe
import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class RecipesService {

    fun getRecipes(category:String,page:Int):List<Recipe>{
        return Jsoup.connect("https://eda.ru/recepty/$category?page=$page").get().run {
            select("div.js-updated-page__content.js-load-more-content")[0]
                .children()
                .filter{
                    it.className() == "tile-list__horizontal-tile horizontal-tile js-portions-count-parent js-bookmark__obj"
                }
                .map {
                    Recipe(
                        id = it.getId(),
                        link = it.getLink(),
                        name = it.getTitle(),
                        image = it.getImage(),
                        countIngredient = it.getCountIngredient(),
                        countPortion = it.getCountPortion(),
                        time = it.getTime(),
                        countLike = it.getCountLike(),
                        countDislike = it.getCountDislike()
                    )
                }
        }
    }

    fun getBestRecipesOfTheDay():List<Recipe>{
        return Jsoup.connect("https://eda.ru/avtory").get().run {
            select("div.widgets-gallery_content-pad")[0]
                .select("ul.inner-gallery__scroller.js-gall-scroller")[0]
                .children()
                .map {
                    Recipe(
                        id = it.getId(),
                        link = it.getLink(),
                        name = it.getTitle(),
                        image = it.getImage(),
                        countIngredient = it.getCountIngredient(),
                        countPortion = it.getCountPortion(),
                        time = it.getTime(),
                        countLike = it.getCountLike(),
                        countDislike = it.getCountDislike()
                    )
                }
        }
    }

    private fun Element.getId():Long{
        return select("div.bookmark.js-tooltip.js-bookmark__label")
            .attr("data-id")
            .apply {
                Log.d("RecipesService",this)
            }
            .toLong()
    }

    private fun Element.getTitle():String{
        return select("h3.horizontal-tile__item-title.item-title")[0]
            .child(0)
            .child(0)
            .html()
            .replace("&nbsp;"," ")
    }

    private fun Element.getLink():String{
        return select("h3.horizontal-tile__item-title.item-title")[0]
            .child(0)
            .attr("href")
    }

    private fun Element.getImage():String{
        return select("div.horizontal-tile__preview")[0]
            .child(0)
            .child(1)
            .attr("data-src")
    }

    private fun Element.getCountIngredient():String{
        return select("div.horizontal-tile__item-specifications")[0]
            .child(0)
            .text()
    }

    private fun Element.getCountPortion():String{
        return select("div.horizontal-tile__item-specifications")[0]
            .child(1)
            .text()
    }

    private fun Element.getTime():String{
        return select("span.prep-time")
            .text()
    }

    private fun Element.getCountLike():String{
        return select("span.widget-list__like-count")[0]
            .child(1)
            .text()
    }

    private fun Element.getCountDislike():String{
        return select("span.widget-list__like-count.widget-list__like-count_dislike")[0]
            .child(1)
            .text()
    }
}