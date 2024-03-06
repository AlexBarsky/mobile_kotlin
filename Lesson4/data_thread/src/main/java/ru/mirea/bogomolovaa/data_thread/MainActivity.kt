package ru.mirea.bogomolovaa.data_thread

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.mirea.bogomolovaa.data_thread.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

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

        val text1 = getString(R.string.runn1)
        val text2 = getString(R.string.runn2)
        val text3 = getString(R.string.runn3)

        val runn1 = Runnable {
            binding.tvInfo.append("\nВыполнен runn1")
            binding.tvInfo.append("\n$text1")
        }
        val runn2 = Runnable {
            binding.tvInfo.append("\nВыполнен runn2")
            binding.tvInfo.append("\n$text2")
        }
        val runn3 = Runnable {
            binding.tvInfo.append("\nВыполнен runn3")
            binding.tvInfo.append("\n$text3")
        }

        val t = Thread {
            try {
                TimeUnit.SECONDS.sleep(2)
                runOnUiThread(runn1)
                TimeUnit.SECONDS.sleep(1)
                binding.tvInfo.postDelayed(runn3, 2000)
                binding.tvInfo.post(runn2)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        t.start()
    }
}