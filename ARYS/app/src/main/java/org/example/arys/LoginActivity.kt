package org.example.arys

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    companion object {
        const val TAG = "LoginActivity"
        const val REQUEST_SIGN_UP = 0
    }

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()

        buttonLogin.setOnClickListener { login() }
        linkSignUp.setOnClickListener {
            val intent = Intent(applicationContext, SignUpActivity::class.java)
            startActivityForResult(intent, REQUEST_SIGN_UP)
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
    }

    private fun login() {
        Log.d(TAG, "Login")

        buttonLogin.isEnabled = false

        val email = inputEmail.text.toString()
        val password = inputPassword.text.toString()

        val progressDialog = ProgressDialog(this, R.style.AppTheme_Dark_Dialog)
        progressDialog.apply {
            isIndeterminate = true
            setMessage("Logging in...")
            show()
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                progressDialog.dismiss()
            }
            .addOnSuccessListener {
                onLoginSuccess()
            }
            .addOnFailureListener {
                it.printStackTrace()

                val errorMessage = "An error occurred while signing in"

                onLoginFailed(errorMessage)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_SIGN_UP)
            if (resultCode == Activity.RESULT_OK)
                onLoginSuccess()
    }

    private fun onLoginSuccess() {
        val intent = Intent(this, HomeActivity::class.java)

        startActivity(intent)
        finish()
    }

    private fun onLoginFailed(reason: String = "") {
        if (reason.isNotBlank())
            Toast.makeText(baseContext, reason, Toast.LENGTH_LONG).show()

        buttonLogin.isEnabled = true
    }
}
