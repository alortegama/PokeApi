package ad.cfp.pokemon

import ad.cfp.pokemon.api.Pokemon
import ad.cfp.pokemon.databinding.ActivityMainBinding
import ad.cfp.pokemon.viewModels.MainViewModel
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            mainViewModel.state.collect { text ->
                Log.d("MAIN_VIEW_MODEL_DATA", text)
            }
        }

        lifecycleScope.launch {
            mainViewModel.pokemon.collect { pokemon ->
                pokemon?.let {
                    //SEMPRE TINDRÉ UN POKEMÓN VÀLID
                    updateUI(pokemon)
                }
            }
        }
        mainViewModel.findPokemon("23")
    }
    fun updateUI(pokemon: Pokemon){
        binding.txtPokemonName.text = pokemon.name
        binding.txtHeightValue.text = pokemon.height.toString()
        binding.txtWeightValue.text = pokemon.weight.toString()
        binding.txtPokemonId.text = pokemon.id.toString()
    }
}