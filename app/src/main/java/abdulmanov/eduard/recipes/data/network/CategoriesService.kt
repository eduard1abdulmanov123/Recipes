package abdulmanov.eduard.recipes.data.network

import abdulmanov.eduard.recipes.domain.models.Category
import org.jsoup.Jsoup

class CategoriesService {

    fun getCategories():List<Category>{
        return Jsoup.connect("https://eda.ru/").get().run {
           select("section.seo-footer")
               .select("li.seo-footer__list-title")
               .map {element->
                   Category(
                       element.attr("href"),
                       element.text().getName(),
                       element.text().getCountRecipes()
                   )
               }
        }
    }

    private fun String.getName():String{
        var name = ""
        forEach {
            if(!it.isDigit())
                name+=it
        }
        return name.trim()
    }

    private fun String.getCountRecipes():Long{
        return split(" ").last().toLong()
    }
}