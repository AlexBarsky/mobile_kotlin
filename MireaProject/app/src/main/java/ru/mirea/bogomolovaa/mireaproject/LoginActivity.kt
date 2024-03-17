package ru.mirea.bogomolovaa.mireaproject

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import ru.mirea.bogomolovaa.mireaproject.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
//        const val PREFERENCES_FILE_NAME = "secret_shared_preferences"
        const val PREFERENCES_FILE_NAME = "shared_preferences"
//        const val PREFERENCES_KEY_USER_TOKEN = "user_token"
        const val PREFERENCES_KEY_USER_AUTH_DATA = "user_auth_data"
        const val EXTRA_SIGN_OUT = "sign_out"
    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth
//    private lateinit var secureSharedPreferences: SharedPreferences
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var statusTextView: TextView
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: TextView
    private lateinit var createAccountButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        statusTextView = binding.statusTextView
        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText
        signInButton = binding.signInButton
        createAccountButton = binding.createAccountButton

//        try {
//            val ketGenParameterSpec = MasterKeys.AES256_GCM_SPEC
//            val mainKeyAlias = MasterKeys.getOrCreate(ketGenParameterSpec)
//
//            secureSharedPreferences = EncryptedSharedPreferences.create(
//                PREFERENCES_FILE_NAME,
//                mainKeyAlias,
//                applicationContext,
//                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//            )
//        } catch (e: IOException) {
//            throw RuntimeException(e)
//        } catch (e: GeneralSecurityException) {
//            throw RuntimeException(e)
//        }

        sharedPreferences = getSharedPreferences(PREFERENCES_FILE_NAME, MODE_PRIVATE)

        Log.d(TAG, "onCreate: ${mAuth.currentUser?.email.toString()}")
        intent.extras?.let {
            if (it.getBoolean(EXTRA_SIGN_OUT)) {
                mAuth.signOut()
                sharedPreferences.edit()
                    .remove(PREFERENCES_KEY_USER_AUTH_DATA)
                    .apply()
            }
        }
        Log.d(TAG, "Sign out intent: ${mAuth.currentUser?.email.toString()}")
        binding.signInButton.setOnClickListener {
            signIn(
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }
        binding.createAccountButton.setOnClickListener {
            createAccount(
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }
    }

    override fun onStart() {
        super.onStart()

        if (sharedPreferences.contains(PREFERENCES_KEY_USER_AUTH_DATA)) {
//            val userToken = sharedPreferences.getString(PREFERENCES_KEY_USER_TOKEN, "")
            val userAuthData = sharedPreferences.getStringSet(PREFERENCES_KEY_USER_AUTH_DATA, setOf())
            Log.d(TAG, userAuthData.toString())
            when {
                userAuthData == null -> {
                    Log.d(TAG, "User auth data is null")
                }
                userAuthData.isEmpty() -> {
                    Log.d(TAG, "User auth data is empty: $userAuthData")
                    sharedPreferences.edit()
                        .remove(PREFERENCES_KEY_USER_AUTH_DATA)
                        .apply()
                }
                userAuthData.size == 2 -> {
                    Log.d(TAG, "User auth data is not empty: $userAuthData")
//                    mAuth.signInWithCustomToken(userAuthData)
//                        .addOnCompleteListener(this) {
//                            Log.d(TAG, it.exception.toString())
//                            if (it.isSuccessful) {
//                                Log.d(TAG, "signInWithCustomToken: success")
//                                startActivityMain()
//                            } else {
//                                Log.d(TAG, "Sign in with custom token failed")
//                                sharedPreferences.edit()
//                                    .remove(PREFERENCES_KEY_USER_TOKEN)
//                                    .apply()
//                            }
//                        }
                    val email = userAuthData.elementAt(0)
                    val password = userAuthData.elementAt(1)

                    statusTextView.text = getString(R.string.signed_in)
                    emailEditText.setText(email)
                    emailEditText.isEnabled = false
                    passwordEditText.setText(password)
                    passwordEditText.isEnabled = false
                    signInButton.isEnabled = false
                    createAccountButton.isEnabled = false

                    mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) {
                            Log.d(TAG, it.exception.toString())
                            if (it.isSuccessful) {
                                Log.d(TAG, "signInWithEmailAndPassword: success")
                                startActivityMain()
                            } else {
                                Log.d(TAG, "Sign in with user auth data failed")
                                sharedPreferences.edit()
                                    .remove(PREFERENCES_KEY_USER_AUTH_DATA)
                                    .apply()
                            }
                        }
                }
                else -> {
                    Log.d(TAG, "Exception: $userAuthData")
                }
            }
        }
        Log.d(TAG, "onStart: ${mAuth.currentUser?.email.toString()}")
    }

    private fun saveUserPreference(email: String, password: String) {
        Log.d(TAG, "saveUserPreference")
//        mAuth.currentUser?.getIdToken(false)
//            ?.addOnCompleteListener(this) { task ->
//                if(task.isSuccessful) {
//                    task.result.token?.let {
//                        Log.d(TAG, "userToken: $it")
//                        val editor = sharedPreferences.edit()
//
//                        editor.putString(PREFERENCES_KEY_USER_TOKEN, it)
//                        editor.apply()
//                    }
//                }
//            }
        Log.d(TAG, "userAuthData: $email, $password")
        val editor = sharedPreferences.edit()

        editor.putStringSet(PREFERENCES_KEY_USER_AUTH_DATA, setOf(email, password))
        editor.apply()
    }

    private fun startActivityMain() {
        startActivity(Intent(
            this,
            MainActivity::class.java
        ))
        finish()
    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createrAccount: $email")
//        if (!validateForm()) {
//            return
//        }
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Log.d(TAG, "createrUserWithEmail: success")
                    saveUserPreference(email, password)
                    startActivityMain()
                } else {
                    Log.w(TAG, "createrUserWithEmail: failure", it.exception)
                    Toast.makeText(
                        this,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn: $email")
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Log.d(TAG, "signInWithEmail: success")
                    saveUserPreference(email, password)
                    startActivityMain()
                } else {
                    Log.w(TAG, "signInWithEmail: failure", it.exception)
                    Toast.makeText(
                        this,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                if (!it.isSuccessful) {
                    binding.statusTextView.text = getString(R.string.auth_failed)
                }
            }
    }

    private fun sendEmailVerification() {
        binding.verifyEmailButton.isEnabled = false

        val user = mAuth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) {
                binding.verifyEmailButton.isEnabled = true

                if (it.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Verification email sent to ${user.email}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.w(TAG, "sendEmailVerification", it.exception)
                    Toast.makeText(
                        this,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}