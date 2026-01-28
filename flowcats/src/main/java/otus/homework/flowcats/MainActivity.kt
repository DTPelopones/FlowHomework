package otus.homework.flowcats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
//import androidx.lifecycle.repeatOnLifecycle

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private val catsViewModel by viewModels<CatsViewModel> { CatsViewModelFactory(diContainer.repository) }

    @OptIn(InternalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)


        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
                catsViewModel.catsState.collect { result ->
                    when (result) {
                        is Result.Success -> view.populate(result.data)
                        is Result.Error -> {
                            // минимально: лог или заглушка
                            // Log.e("MainActivity", "Error", result.throwable)
                        }
                    }
                }
//            }
        }

    }
}