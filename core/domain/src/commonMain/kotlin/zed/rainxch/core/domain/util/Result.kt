package zed.rainxch.core.domain.util

sealed interface Result<out D, out E : Error> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Failure<out E : Error>(val error: E) : Result<Nothing, E>
}

inline fun <D, E : Error, T> Result<D, E>.map(map : (D) -> T) : Result<T, E> {
    return when(this) {
        is Result.Failure -> {
            this
        }
        is Result.Success -> {
            Result.Success(map(this.data))
        }
    }
}

inline fun <D, E: Error> Result<D, E>.onSuccess(action : (D) -> Unit) : Result<D, E> {
    return when(this) {
        is Result.Failure -> {
            this
        }
        is Result.Success -> {
            action(this.data)
            this
        }
    }
}


inline fun <D, E: Error> Result<D, E>.onFailure(action : (E) -> Unit) : Result<D, E> {
    return when(this) {
        is Result.Failure -> {
            action(this.error)
            this
        }
        is Result.Success -> {
            this
        }
    }
}

fun <D, E: Error> Result<D, E>.asEmptyResult() : EmptyResult<E> {
    return map { }
}

typealias EmptyResult<E> = Result<Unit, E>