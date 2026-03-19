package ad.cfp.pokemon.viewModels

import ad.cfp.pokemon.api.PokeApiClient
import ad.cfp.pokemon.api.Pokemon
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _state = MutableStateFlow("Idle")
    val state: StateFlow<String> = _state

    private val _pokemon = MutableStateFlow<Pokemon?>(null)
    val pokemon: StateFlow<Pokemon?> = _pokemon

    fun findPokemon(idOrName: String) {
        viewModelScope.launch {
            val path = idOrName.trim().lowercase()
            if (path.isEmpty()) {
                _state.value = "Introdueix un id o nom correcte"
                return@launch
            }
            _state.value = "Carregant Pokemon . . ."
            try {
                val res = PokeApiClient.api.obtenirPokemon(idOrName)
                if (res.isSuccessful) {
                    val body = res.body()
                    _pokemon.value = body
                    if (body != null) {
                        _state.value = "OK"
                    } else {
                        _state.value = "No s'ha recuperat cap pokemon"
                    }
                }
            } catch (e: Exception) {
                _pokemon.value = null
                _state.value = "Error de connexió. ${e.message}"
            }
        }
    }

}