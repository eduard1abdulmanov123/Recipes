package abdulmanov.eduard.recipes.data.network

import abdulmanov.eduard.recipes.domain.models.Category
import org.jsoup.Jsoup

class CategoriesService {

    fun getCategories():List<Category>{
        return Jsoup.connect("https://eda.ru/").get().run {
            val categories = select("div.tag-selector__selects-box")[0]
                .child(0)
                .select("ul.select-suggest__result.js-select-suggest__result")[0]
                .children()
            categories.subList(1,categories.size)
                .map {
                    Category(
                        it.attr("data-select-suggest-value"),
                        it.html()
                    )
                }
        }
    }

}