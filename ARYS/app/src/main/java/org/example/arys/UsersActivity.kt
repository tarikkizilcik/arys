package org.example.arys

import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_users.*
import org.example.arys.adapters.UsersAdapter
import org.example.arys.data.User
import org.json.JSONArray
import org.json.JSONObject

class UsersActivity : AppCompatActivity() {
    companion object {
        const val TAG = "UsersActivity"
        private const val EVENT_AUTHENTICATE = "authenticate"
        private const val EVENT_AUTHENTICATED = "authenticated"
        private const val EVENT_UNAUTHORIZED = "unauthorized"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val users = mutableListOf<User>()
        val usersAdapter = UsersAdapter(this, users)
        listViewUsers.adapter = usersAdapter

        val token = intent.getStringExtra(LoginActivity.EXTRA_USER)

        val socket = IO.socket("http://10.0.3.2:23058")
        socket.on(Socket.EVENT_CONNECT) {
            Log.d(TAG, "Connected to server")

            val jwt = JSONObject("{token: $token}")

            socket.on(EVENT_AUTHENTICATED) {
                socket.on("res-users") {
                    val jsonArray = it[0] as JSONArray
                    for (i: Int in 0 until jsonArray.length()) {
                        val o = jsonArray[i]
                        if (o is JSONObject)
                            users.add(User(o.getString("email"), o.getString("name")))
                    }

                    this@UsersActivity.runOnUiThread {
                        usersAdapter.notifyDataSetChanged()
                    }
                }
                    .on(Socket.EVENT_ERROR) { args -> Log.e(TAG, args.toString()) }
                    .emit("req-users", jwt)
            }
                .on(EVENT_UNAUTHORIZED) {
                    Toast.makeText(this, "Unauthorized", Toast.LENGTH_LONG).show()
                    socket.close()
                }
                .emit(EVENT_AUTHENTICATE, jwt)
        }

        socket.connect()
    }
}
