package org.example.arys.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import org.example.arys.R

class ContactDialogFragment : DialogFragment() {
    private lateinit var mListener: OnArrayItemClickListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.select_contact_type)
                .setItems(R.array.contact_types) { _, which ->
                    mListener.onArrayItemClick(which)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener = context as OnArrayItemClickListener
    }
}
