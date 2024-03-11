package ru.mirea.bogomolovaa.securesharedpreferences

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import ru.mirea.bogomolovaa.securesharedpreferences.databinding.ActivityMainBinding
import java.io.IOException
import java.security.GeneralSecurityException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.imageViewAvatar.setImageResource(R.drawable.ic_poet)
        val name = binding.textViewName

        try {
            val ketGenParameterSpec = MasterKeys.AES256_GCM_SPEC
            val mainKeyAlias = MasterKeys.getOrCreate(ketGenParameterSpec)

            val secureSharedPreferences = EncryptedSharedPreferences.create(
                "secret_shared_preferences",
                mainKeyAlias,
                applicationContext,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            val editor = secureSharedPreferences.edit()

            editor.putString(KEY_NAME, "Пушкин А. С.")
            editor.apply()

            name.text = secureSharedPreferences.getString(KEY_NAME, "")
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: GeneralSecurityException) {
            throw RuntimeException(e)
        }
    }

    private companion object {
        const val KEY_NAME = "name"
    }
}