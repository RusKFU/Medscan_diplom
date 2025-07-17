import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel : ViewModel() {
    private val _userId = MutableStateFlow<Int?>(null)
    val userId = _userId.asStateFlow()

    fun login(login: String, password: String) {
        // Заглушка: всегда авторизуем как userId = 1
        _userId.value = 1
    }

    fun register(login: String, password: String) {
        // Заглушка: всегда создаём userId = 1
        _userId.value = 1
    }

    fun logout() {
        _userId.value = null
    }
}