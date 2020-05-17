package com.runningtechie.transparentrunning.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.runningtechie.transparentrunning.R
import com.runningtechie.transparentrunning.ui.viewLocationPoint.LocationPointListFragment
import com.runningtechie.transparentrunning.ui.viewWorkouts.WorkoutSessionListFragment

class WorkoutSessionActivity : AppCompatActivity(),
    WorkoutSessionListFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_session)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment =
                WorkoutSessionListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onWorkoutSelected(workoutSessionId: Long?) {
        val fragment = LocationPointListFragment.newInstance(workoutSessionId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        fun newIntent(packageContext: Context): Intent {
            return Intent(packageContext, WorkoutSessionActivity::class.java)
        }
    }
}
