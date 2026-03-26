package ad.cfp.pokemon.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<NamedPokemon>
)

data class NamedPokemon(
    val name: String,
    val url: String
)

data class Pokemon(
    val id: Int,
    val height: Int,
    val name: String,
    val sprites: SpritePokemon,
    val weight: Int
)

data class SpritePokemon(
    val front_default: String
)

interface PokeApiService {
    @GET("pokemon")
    suspend fun llistarPokemons(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): Response<PokemonListResponse>

    @GET("pokemon/{idOrName}")
    suspend fun obtenirPokemon(
        @Path("idOrName") idOrName: String = "1"
    ): Response<Pokemon>
}








