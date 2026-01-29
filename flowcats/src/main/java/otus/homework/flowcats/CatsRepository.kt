package otus.homework.flowcats

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CatsRepository(
    private val catsService: CatsService,
    private val refreshIntervalMs: Long = 5000
) {

    fun listenForCatFacts(): Flow<Result<Fact>> = flow {
        while (true) {
            try {
                emit(Result.Success(catsService.getCatFact()))
            } catch (e: Exception) {
                emit(Result.Error(e))
                break
            }
            delay(refreshIntervalMs)
        }
    }
}