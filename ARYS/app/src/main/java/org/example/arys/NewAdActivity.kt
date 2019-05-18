package org.example.arys

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_new_ad.*

class NewAdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_ad)

        buttonSave.setOnClickListener(onClickSaveButton)
        buttonCancel.setOnClickListener(onClickCancelButton)
    }

    private val onClickSaveButton = View.OnClickListener {
        finish()
    }

    private val onClickCancelButton = View.OnClickListener {
        finish()
    }
}
