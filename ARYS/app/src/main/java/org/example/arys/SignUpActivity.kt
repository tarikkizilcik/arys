package org.example.arys

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_signup.*
import org.example.arys.database.DatabaseRef

class SignUpActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SignUpActivity"
        private const val ERR_RES_EMAIL_IN_USE = "is already in use"
    }


    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        firebaseAuth = FirebaseAuth.getInstance()



        buttonSignUp.setOnClickListener { signUp() }
        linkLogin.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
    }

    private fun signUp() {
        Log.d(TAG, "Sign Up")

        if (!validateInput()) {
            onSignUpFailed()
            return
        }

        buttonSignUp.isEnabled = false


        val progressDialog = ProgressDialog(this, R.style.AppTheme_Dark_Dialog)
        progressDialog.apply {
            isIndeterminate = true
            setMessage("Signing up...")
            show()


        }

        fun getInputStr(editText: EditText): String {
            return editText.text.toString()
        }

        val email = getInputStr(inputEmail)
        val password = getInputStr(inputPassword)

        val adsRef = DatabaseRef.adsRef

        adsRef.setValue("Hello World!")


        adsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val value = dataSnapshot.getValue(String::class.java)
                Log.d(TAG, "Value is : $value")
            }

            override fun onCancelled(error: DatabaseError) {

                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                progressDialog.dismiss()

                onSignUpSuccess()
            }
            .addOnFailureListener {
                it.printStackTrace()

                val errorMessage = "An error occurred while registering"

                onSignUpFailed(errorMessage)

                progressDialog.dismiss()
            }
    }

    private fun onSignUpSuccess() {
        buttonSignUp.isEnabled = true
        setResult(Activity.RESULT_OK)

        finish()
    }

    private fun onSignUpFailed(errorMessage: String = "") {
        if (errorMessage.isNotBlank())
            Toast.makeText(baseContext, errorMessage, Toast.LENGTH_LONG).show()

        buttonSignUp.isEnabled = true
    }

    private fun validateInput(): Boolean {
        val editTexts = arrayOf(
            inputName,
            inputEmail,
            inputPassword,
            inputRePassword
        )

        val inputs = editTexts.map { i -> i.text.toString() }

        val conditions = arrayOf(
            inputs[0].run { isNotBlank() || length >= 3 },
            inputs[1].let { it.isNotBlank() || android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() },
            inputs[2].run { isNotBlank() || length >= 4 },
            inputs[3] == inputs[2]
        )

        val errorMessages = arrayOf(
            "Name must be at least 3 characters long",
            "enter a valid email address",
            "Password must be at least 4 characters long",
            "Passwords do not match"
        )

        var valid = true

        for (i: Int in 0 until inputs.size) {
            if (valid && !conditions[i]) valid = false

            editTexts[i].error = if (conditions[i]) null else errorMessages[i]
        }

        return valid
    }
}
