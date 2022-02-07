package codes.adityaa.temasyschat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import codes.adityaa.temasyschat.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.mainNavHost)

        val fragmentWithoutAppBar = setOf(
            R.id.chatFragment,
        )


        navController.addOnDestinationChangedListener { _, destination, _ ->
                if (fragmentWithoutAppBar.contains(destination.id)) supportActionBar?.hide() else supportActionBar?.show()
        }


    }
}