package ru.mirea.bogomolovaa.mireaproject

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import ru.mirea.bogomolovaa.mireaproject.databinding.ActivityMainBinding
import ru.mirea.bogomolovaa.mireaproject.ui.backgroundtask.BackgroundTaskViewModel
import ru.mirea.bogomolovaa.mireaproject.ui.mail.MailDraftFragment
import ru.mirea.bogomolovaa.mireaproject.ui.mail.MailViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val backgroundTaskViewModel: BackgroundTaskViewModel by viewModels()
    private val mailViewModel: MailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener {
            val fragment = MailDraftFragment()
            fragment.show(supportFragmentManager, "MyDialogFragment")
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_data, R.id.nav_web_view, R.id.nav_background_task,
                R.id.nav_audio_record, R.id.nav_camera, R.id.nav_temperature,
                R.id.nav_draft_list,
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        backgroundTaskViewModel.resultTextView.observe(this) {
            Snackbar.make(
                binding.root,
                it,
                Snackbar.LENGTH_LONG
            ).show()
        }

        mailViewModel.onFileSaved.observe(this) {
            if (it) {
                Snackbar.make(
                    binding.root,
                    R.string.draft_saved,
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                Snackbar.make(
                    binding.root,
                    R.string.draft_not_saved,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                navController.navigate(R.id.nav_profile)
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    companion object {
        private val TAG: String = MainActivity::class.java.simpleName
    }
}