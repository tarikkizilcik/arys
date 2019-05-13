package org.example.arys

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class LoginActivity : AppCompatActivity() {
    companion object {
        const val TAG = "LoginActivity"
        const val REQUEST_SIGN_UP = 0
        const val EXTRA_USER = "EXTRA_USER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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

        val url = "${Connection.URL}/api/users/login"

        val jsonUser = JSONObject()
        jsonUser.put("email", email)
        jsonUser.put("password", password)
        val jsonBody = JSONObject()
        jsonBody.put("user", jsonUser)

        val responseListener = Response.Listener<JSONObject> {
            progressDialog.dismiss()

            onLoginSuccess(it.getJSONObject("user"))
        }
        val errorListener = Response.ErrorListener {
            var errorMessage = "An error occurred while logging in"

            it.networkResponse?.apply {
                data?.apply {
                    try {
                        val bodyStr = String(this, Charsets.UTF_8)
                        val body = JSONObject(bodyStr)

                        val errors = body.getJSONObject("errors")

                        errorMessage = errors.toString()
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            } ?: run { errorMessage = "Server not responding" }

            onLoginFailed(errorMessage)

            progressDialog.dismiss()
        }
        val registerUserRequest = JsonObjectRequest(Request.Method.POST, url, jsonBody, responseListener, errorListener)

        val queue = Volley.newRequestQueue(this)
        queue.add(registerUserRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_SIGN_UP) {
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    Toast.makeText(this, "An error occurred while registering", Toast.LENGTH_LONG).show()
                    return
                }

                val userStr = data.getStringExtra(EXTRA_USER)
                onLoginSuccess(JSONObject(userStr))
            }
        }
    }

    private fun onLoginSuccess(userJson: JSONObject) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra(EXTRA_USER, userJson.toString())

        val socket = Connection.socket
        socket.on(Connection.EVENT_AUTHENTICATED) {
            this@LoginActivity.runOnUiThread {
                startActivity(intent)
                finish()
            }
        }

        val jwt = JSONObject("{token:${userJson.getString("token")}}")
        Connection.authenticate(jwt)
    }

    private fun onLoginFailed(reason: String = "") {
        if (reason.isNotBlank())
            Toast.makeText(baseContext, reason, Toast.LENGTH_LONG).show()

        buttonLogin.isEnabled = true
    }
}
