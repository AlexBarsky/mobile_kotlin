package ru.mirea.bogomolovaa.cryptoloader

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import androidx.loader.content.AsyncTaskLoader
import ru.mirea.bogomolovaa.cryptoloader.MainActivity.Companion.decryptMsg
import javax.crypto.spec.SecretKeySpec

class MyLoader(
    context: Context,
    args: Bundle?
) : AsyncTaskLoader<String>(context) {

    private val firstName: String? = args?.getString(ARG_WORD)

    private val encryptedMsg: ByteArray? = args?.getByteArray(ARG_WORD)
    private val key: ByteArray? = args?.getByteArray("key")

    override fun onStartLoading() {
        super.onStartLoading()
        forceLoad()
    }

    override fun loadInBackground(): String? {
        SystemClock.sleep(5000);
        if (encryptedMsg != null && key != null) {
            val originalKey = SecretKeySpec(key, 0, key.size, "AES")
            return decryptMsg(encryptedMsg, originalKey)
        }
        return firstName
    }

    companion object {
        const val ARG_WORD = "word"
    }
}