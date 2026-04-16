package ad.cfp.pokemon.viewModels

import ad.cfp.pokemon.api.PokeApiClient
import ad.cfp.pokemon.api.Pokemon
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _state = MutableStateFlow("Esperant...")
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
                } else {
                    _state.value = "No s'ha trobat el pokemon"
                    _pokemon.value = null
                }
            } catch (e: Exception) {
                _pokemon.value = null
                _state.value = "Error de connexió. ${e.message}"
            }
        }
    }

    private fun extractUniqueSpriteUrls(
        element: JsonElement,
        out: MutableSet<String> = linkedSetOf()
    ): List<String> {

        if (element.isJsonNull) return out.toList()

        when {
            element.isJsonPrimitive && element.asJsonPrimitive.isString -> {
                val value = element.asString.trim()
                if (value.startsWith("http")) out.add(value)
            }

            element.isJsonObject -> {
                for ((_, child) in element.asJsonObject.entrySet()) {
                    extractUniqueSpriteUrls(child, out)
                }
            }

            element.isJsonArray -> {
                val arr = element.asJsonArray
                for (i in 0 until arr.size()) {
                    extractUniqueSpriteUrls(arr[i], out)
                }
            }
        }

        return out.toList()
    }
}
