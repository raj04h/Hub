package com.hr.hub

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task

class LoginActivity: AppCompatActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail() // Request user's email address
            .requestProfile() // Request user's profile information
            .build()


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Set up the Google Sign-In button
        val googleSignInButton: SignInButton = findViewById(R.id.btn_google_sign_in)
        googleSignInButton.setOnClickListener {
            signIn()
        }

        // Check if user is already signed in and update UI accordingly
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            // User is already signed in, navigate to MainActivity or update the UI
            updateUI(account)
        }
    }

    // Sign in with Google
    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    // Handle the Google Sign-In result
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, navigate to MainActivity
            updateUI(account)
        } catch (e: ApiException) {
            // Sign-in failed, update UI appropriately
            Log.w("Google Sign-In", "signInResult:failed code=" + e.statusCode)
            Toast.makeText(this, "Sign-in failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            updateUI(null)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            // Extract user information
            val email = account.email
            val displayName = account.displayName
            val photoUrl = account.photoUrl

            // Use the user information (e.g., display in UI or store in your app)
            Log.d("Google Sign-In", "Email: $email")
            Log.d("Google Sign-In", "Display Name: $displayName")
            Log.d("Google Sign-In", "Photo URL: $photoUrl")

            // Optionally, navigate to MainActivity or another part of your app
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
            finish() // Finish LoginActivity so it can't be returned to
        } else {
            // Handle UI update when sign-in fails
            Log.d("Google Sign-In", "User is not signed in")
            Toast.makeText(this, "Sign-in failed", Toast.LENGTH_LONG).show()
        }
    }

}