package org.example.arys.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.example.arys.R
import org.example.arys.data.User

class UsersAdapter(private val context: Context, private val dataSet: List<User>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: run {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.list_item_users, parent, false)

            view.tag = ViewHolder(view.findViewById(R.id.textViewEmail), view.findViewById(R.id.textViewName))

            view
        }

        val viewHolder = view.tag as ViewHolder
        viewHolder.textViewEmail.text = getItem(position).email
        viewHolder.textViewName.text = getItem(position).name

        return view
    }

    override fun getItem(position: Int): User {
        return dataSet[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dataSet.size
    }

    private data class ViewHolder(val textViewEmail: TextView, val textViewName: TextView)
}