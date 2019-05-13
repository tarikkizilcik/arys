package org.example.arys.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.example.arys.R
import org.example.arys.data.Room

class HomeAdapter(private val context: Context, private val dataSet: List<Room>) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val view = inflater.inflate(R.layout.list_item_home, p0, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val room = dataSet[p1]

        p0.bind(room)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val textViewRoom = view.findViewById<TextView>(R.id.textViewRoom)
        private val imageViewRoom = view.findViewById<ImageView>(R.id.imageViewRoom)

        fun bind(room: Room) {
            textViewRoom.text = room.name
            if (room.iconName.isNullOrBlank())
                imageViewRoom.visibility = View.INVISIBLE
            else {
                val id = context.resources.getIdentifier(room.iconName, "drawable", context.packageName)
                imageViewRoom.setImageResource(id)
                imageViewRoom.visibility = View.VISIBLE
            }

            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (::onItemClickListener.isInitialized)
                onItemClickListener.onItemClick(adapterPosition, view)
        }
    }
}