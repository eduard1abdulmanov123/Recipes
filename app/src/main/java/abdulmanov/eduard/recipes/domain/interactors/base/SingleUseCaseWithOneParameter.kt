package abdulmanov.eduard.recipes.domain.interactors.base

import io.reactivex.Single

interface SingleUseCaseWithOneParameter<P, R> {

    fun execute(parameter: P): Single<R>
}