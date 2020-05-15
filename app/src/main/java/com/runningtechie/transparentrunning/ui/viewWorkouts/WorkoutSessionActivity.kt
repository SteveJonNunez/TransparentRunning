package com.runningtechie.transparentrunning.ui.viewWorkouts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.runningtechie.transparentrunning.R

class WorkoutSessionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_session)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = WorkoutSessionListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }
    companion object {
        fun newIntent(packageContext: Context): Intent {
            return Intent(packageContext, WorkoutSessionActivity::class.java)
        }
    }
}