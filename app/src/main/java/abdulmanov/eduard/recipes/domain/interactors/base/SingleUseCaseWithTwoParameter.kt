package abdulmanov.eduard.recipes.domain.interactors.base

import io.reactivex.Single

interface SingleUseCaseWithTwoParameter<F, S, R> {

    fun execute(first: F, second: S): Single<R>
}