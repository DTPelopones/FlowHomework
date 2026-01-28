package otus.homework.flowcats

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatsViewModel(
    private val catsRepository: CatsRepository
) : ViewModel() {

    private val _catsState =
        MutableStateFlow<Result<Fact>>(Result.Error(IllegalStateException("No data")))
    val catsState: StateFlow<Result<Fact>> = _catsState

    init {
        viewModelScope.launch {
                catsRepository.listenForCatFacts()
                    .map { fact ->
                        Result.Success(fact) as Result<Fact>
                    }
                    .catch { throwable ->
                        _catsState.value = Result.Error(throwable)
                    }
                    .collect { result ->
                        _catsState.value = result
                    }

        }
    }
}

class CatsViewModelFactory(private val catsRepository: CatsRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CatsViewModel(catsRepository) as T
}

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val throwable: Throwable) : Result<Nothing>()
}
