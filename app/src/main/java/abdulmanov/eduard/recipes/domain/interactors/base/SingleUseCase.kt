package abdulmanov.eduard.recipes.domain.interactors.base

import io.reactivex.Single

interface SingleUseCase<R> {

    fun execute(): Single<R>
}