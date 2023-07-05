package com.example.kotlinseries

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

//        splashScreen.setOnExitAnimationListener{splashScreenView->
//            val slideUp = ObjectAnimator.ofFloat(
//                splashScreenView,
//                "translationY",
//            0f)
//            slideUp.interpolator = AnticipateInterpolator()
//            slideUp.duration = 200L
//
//            // Call SplashScreenView.remove at the end of your custom animation.
//            slideUp.doOnEnd {
//                splashScreen.setKeepOnScreenCondition { true }
//                splashScreenView.remove()
//                val intent = Intent(this,MainActivity::class.java)
//                startActivity(intent)
//                finish()}
//
//            // Run your animation.
//            slideUp.start()
//
//        }
        Handler().postDelayed({
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        },2000)

    }
}