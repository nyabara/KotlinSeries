package com.example.kotlinseries

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kotlinseries.databinding.ActivityMainBinding
import com.example.kotlinseries.viewmodels.KotlinSeriersViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay

const val KEY_EVENT_ACTION = "key_event_action"
const val KEY_EVENT_EXTRA = "key_event_extra"
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    val atester = 1 //Acts as a viewmodel state when it is ready
    //Initializing viewmodel
    private val viewModel: KotlinSeriersViewModel by lazy {
        val activity = requireNotNull(this) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(
            this, Injection.provideViewModelFactory(
                context = activity,
                owner = this,
            )
        )
            .get(KotlinSeriersViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val drawerLayout =binding.drawerLayout
        val navView = binding.navView
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        setSupportActionBar(binding.toolbar)

        // Set up an OnPreDrawListener to the root view.
        //Keep the splash screen on screen for longer periods

        val content: View = binding.root
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check whether the initial data is ready.
                    Log.d("tester",atester.toString())
                    return if (atester == 1) {
                        // The content is ready. Start drawing.
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        // The content isn't ready. Suspend.
                        false
                    }
                }
            }
        )
        //splashScreen.setKeepOnScreenCondition { true }
        //Customize the animation for dismissing the splash screen




        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration
            .Builder(R.id.homeFragment, R.id.loginFragment,R.id.newsFragment,R.id.settingsFragment,R.id.aboutFragment)
            .setDrawerLayout(drawerLayout)
            .build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        bottomNav.setupWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    /** When key down event is triggered, relay it via local broadcast so fragments can handle it */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                val intent = Intent(KEY_EVENT_ACTION).apply { putExtra(KEY_EVENT_EXTRA, keyCode) }
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }


}