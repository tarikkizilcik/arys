package org.example.arys

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        buttonNewAd.setOnClickListener(onClickNewAd)
    }

    private val onClickNewAd = View.OnClickListener {
        val intent = Intent(this, NewAdActivity::class.java)
        startActivity(intent)
    }
}
