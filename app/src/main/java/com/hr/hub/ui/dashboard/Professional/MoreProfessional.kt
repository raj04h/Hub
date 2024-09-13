package com.hr.hub.ui.dashboard.Professional

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hololo.tutorial.library.Step
import com.hololo.tutorial.library.TutorialActivity
import com.hr.hub.R

class MoreProfessional : TutorialActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("com.hr.hub", MODE_PRIVATE)

        // Check if tutorial has been shown before
        val isTutorialShown = sharedPreferences.getBoolean("isTutorialShown", false)

        if (isTutorialShown) {
            // If tutorial has been shown before, redirect to the main screen
            goToMainScreen()
        } else {
            // Show tutorial for the first time
            showTutorial()
        }
    }

    private fun showTutorial() {
        // Add tutorial steps
        addFragment(
            Step.Builder()
                .setTitle("Header 1")
                .setContent("This is the content for the first step")
                .setBackgroundColor(Color.parseColor("#FF0957")) // Background color
                .setDrawable(R.drawable.undraw_add_document_re_mbjx) // Drawable resource
                .setSummary("Summary for the first step")
                .build()
        )

        addFragment(
            Step.Builder()
                .setTitle("Header 2")
                .setContent("This is the content for the second step")
                .setBackgroundColor(Color.parseColor("#4CAF50")) // Example: different background color
                .setDrawable(R.drawable.undraw_add_document_re_mbjx) // Use different drawable if needed
                .setSummary("Summary for the second step")
                .build()
        )
    }

    override fun finishTutorial() {
        // Save the flag in SharedPreferences indicating that the tutorial has been shown
        sharedPreferences.edit().putBoolean("isTutorialShown", true).apply()

        // Start the main screen (ReminderActivity in your case) when the tutorial finishes
        goToMainScreen()

        super.finishTutorial()
    }

    private fun goToMainScreen() {
        val intent = Intent(this, TextScanTutorialActivity::class.java)
        startActivity(intent)
        finish() // Close the current activity to prevent going back to the tutorial
    }

    override fun currentFragmentPosition(position: Int) {
        // Optionally handle the current fragment position
    }
}
