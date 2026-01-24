package zed.rainxch.core.data.networking

import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import zed.rainxch.core.domain.util.DataError
import zed.rainxch.core.domain.util.Result
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

actual suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend (HttpResponse) -> Result<T, DataError.Remote>
): Result<T, DataError.Remote> {
    return try {
        val response = execute()
        handleResponse(response)
    } catch (e: UnknownHostException) {
        Result.Failure(DataError.Remote.NO_INTERNET)
    } catch (e: UnresolvedAddressException) {
        Result.Failure(DataError.Remote.NO_INTERNET)
    } catch (e: ConnectException) {
        Result.Failure(DataError.Remote.REQUEST_TIMEOUT)
    } catch (e: TimeoutException) {
        Result.Failure(DataError.Remote.REQUEST_TIMEOUT)
    } catch (e: SerializationException) {
        Result.Failure(DataError.Remote.SERIALIZATION)
    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
        Result.Failure(DataError.Remote.UNKNOWN)
    }
}