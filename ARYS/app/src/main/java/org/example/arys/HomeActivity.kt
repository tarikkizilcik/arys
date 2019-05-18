package org.example.arys

import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.View
import android.widget.EditText
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_home.*
import org.example.arys.adapters.HomeAdapter
import org.example.arys.adapters.OnItemClickListener
import org.example.arys.data.Room
import org.example.arys.fragments.ContactDialogFragment
import org.example.arys.fragments.OnArrayItemClickListener
import org.json.JSONArray
import org.json.JSONObject

class HomeActivity : AppCompatActivity(), OnArrayItemClickListener {
    companion object {
        const val TAG = "HomeActivity"
        const val EXTRA_ROOM = "EXTRA_ROOM"
        const val USER_CONTACT = 0
        const val ROOM_CONTACT = 1
    }

    private lateinit var mUserString: String
    private lateinit var mUserJson: JSONObject
    private val mRooms = mutableListOf<Room>()
    private val mHomeAdapter = HomeAdapter(this, mRooms)
    private val mSocket = Connection.socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mUserString = intent.getStringExtra(LoginActivity.EXTRA_UID)
        mUserJson = JSONObject(mUserString)

        recyclerViewHome.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = mHomeAdapter
        }

        mSocket
            .on(Connection.EVENT_ROOMS, onEventRooms)
            .on(Connection.EVENT_JOIN, onEventJoin)

        mHomeAdapter.setOnItemClickListener(onHomeItemClick)
        buttonAddRoom.setOnClickListener(onAddButtonClick)

        val bodyJson = JSONObject()
        bodyJson.put("user", mUserJson)
        mSocket.emit(Connection.EVENT_ROOMS, bodyJson)
    }

    override fun onArrayItemClick(index: Int) {
        val contactInput = when (index) {
            USER_CONTACT -> {
                takeAddContactInput(R.string.user_contact, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
            }
            ROOM_CONTACT -> {
                takeAddContactInput(R.string.room_contact, InputType.TYPE_CLASS_TEXT)
            }
            else -> throw IllegalArgumentException()
        } ?: return
    }

    private val onEventRooms = Emitter.Listener {
        val roomsArray = it[0] as JSONArray

        for (i: Int in 0 until roomsArray.length()) {
            val roomJson = roomsArray[i] as JSONObject
            val name = roomJson.getString("name")
            val iconName = roomJson.getString("iconName")

            val room = Room(name, iconName)
            mRooms.add(room)
        }

        this@HomeActivity.runOnUiThread { mHomeAdapter.notifyDataSetChanged() }
    }

    private val onEventJoin = Emitter.Listener {
        val bodyJson = it[0] as JSONObject
        val roomJson = bodyJson.getJSONObject("room")

        val intent = Intent(this@HomeActivity, ChatActivity::class.java)
        intent.putExtra(LoginActivity.EXTRA_UID, mUserString)
        intent.putExtra(EXTRA_ROOM, roomJson.toString())

        this@HomeActivity.runOnUiThread {
            startActivity(intent)
        }
    }

    private val onHomeItemClick = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            val room = mRooms[position]
            val roomJson = JSONObject()
            roomJson.apply {
                put("name", room.name)
                put("iconName", room.iconName)
            }
            val bodyJson = JSONObject()
            bodyJson.put("room", roomJson)
            bodyJson.put("user", mUserJson)

            mSocket.emit(Connection.EVENT_JOIN, bodyJson)
        }
    }

    private val onAddButtonClick = View.OnClickListener {
        val dialogFragment = ContactDialogFragment()
        dialogFragment.show(supportFragmentManager, TAG)
    }

    private fun takeAddContactInput(@StringRes titleId: Int, inputType: Int): String? {
        val builder = AlertDialog.Builder(this)
        val input = EditText(this)
        input.inputType = inputType

        var result: String? = null
        builder.apply {
            setTitle(titleId)
            setView(input)
            setPositiveButton(getString(R.string.ok)) { _, _ ->
                result = input.text.toString()
            }
            setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }
        }

        builder.show()

        return result
    }
}
