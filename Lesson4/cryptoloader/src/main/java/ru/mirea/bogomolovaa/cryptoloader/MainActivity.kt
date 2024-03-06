package ru.mirea.bogomolovaa.cryptoloader

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import ru.mirea.bogomolovaa.cryptoloader.databinding.ActivityMainBinding
import java.security.InvalidKeyException
import java.security.InvalidParameterException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<String> {

    private val TAG = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding
    private lateinit var secretKey: SecretKey

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
    }

    fun onClickButton(view: View) {
        val bundle = Bundle()
        bundle.putString(MyLoader.ARG_WORD, "mirea")
        LoaderManager.getInstance(this).initLoader(LoaderID, bundle, this)
    }

    fun onBtnEncryptClick(view: View) {
        val inputText = binding.editTextView.text.toString()
        if (inputText.isEmpty()) {
            Toast.makeText(this, "Input text is empty", Toast.LENGTH_SHORT).show()
            return
        }
        secretKey = generateKey()

        val encryptedMsg = encryptMsg(inputText, secretKey)

        val bundle = Bundle()
        bundle.putByteArray(MyLoader.ARG_WORD, encryptedMsg)
        bundle.putByteArray("key", secretKey.encoded)

        LoaderManager.getInstance(this).restartLoader(LoaderID, bundle, this)
    }

    override fun onCreateLoader(id: Int, bundle: Bundle?): Loader<String> {
        if (id == LoaderID) {
            Toast.makeText(this, "onCreateLoader: $id", Toast.LENGTH_SHORT).show()
            return MyLoader(this, bundle)
        }
        throw InvalidParameterException("Invalid loader ID")
    }

    override fun onLoadFinished(loader: Loader<String>, data: String?) {
        if (loader.id == LoaderID) {
            Log.d(TAG, "onLoadFinished: $data")
            Toast.makeText(this, "onLoadFinished: $data", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onLoaderReset(loader: Loader<String>) {
        Log.d(TAG, "onLoaderReset")
    }

    companion object {
        private const val LoaderID = 1234

        fun generateKey(): SecretKey {
            try {
                val sr = SecureRandom.getInstance("SHA1PRNG")
                sr.setSeed("any data used as random seed".toByteArray())

                val kg = KeyGenerator.getInstance("AES")
                kg.init(256, sr)

                return SecretKeySpec(kg.generateKey().encoded, "AES")
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException(e)
            }
        }
        fun encryptMsg(msg: String, key: SecretKey): ByteArray {
            var cipher: Cipher? = null
            try {
                cipher = Cipher.getInstance("AES")
                cipher.init(Cipher.ENCRYPT_MODE, key)
                return cipher.doFinal(msg.toByteArray())
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException(e)
            } catch (e: NoSuchPaddingException) {
                throw RuntimeException(e)
            } catch (e: InvalidKeyException) {
                throw RuntimeException(e)
            } catch (e: BadPaddingException) {
                throw RuntimeException(e)
            } catch (e: IllegalBlockSizeException) {
                throw RuntimeException(e)
            }
        }
        fun decryptMsg(msg: ByteArray, key: SecretKey): String {
            try {
                val cipher = Cipher.getInstance("AES")
                cipher.init(Cipher.DECRYPT_MODE, key)
                return String(cipher.doFinal(msg))
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException(e)
            } catch (e: NoSuchPaddingException) {
                throw RuntimeException(e)
            } catch (e: IllegalBlockSizeException) {
                throw RuntimeException(e)
            }
        }
    }
}