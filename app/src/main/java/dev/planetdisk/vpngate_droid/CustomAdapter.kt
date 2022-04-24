package dev.planetdisk.vpngate_droid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val serverList: ArrayList<ServerList>): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private lateinit var listener: OnButtonClickListener

    interface  OnButtonClickListener {
        fun onButtonClick(server: ServerList)
    }

    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        this.listener = listener
    }

    // Viewの初期化
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val hostName: TextView = view.findViewById(R.id.hostName)
        val ipAddr: TextView = view.findViewById(R.id.ipAddr)
        val ping: TextView = view.findViewById(R.id.ping)
        val country: TextView = view.findViewById(R.id.country)
        val operator: TextView = view.findViewById(R.id.operator)
        val saveButton: com.google.android.material.button.MaterialButton = view.findViewById(R.id.save_button)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.recycler_card, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val server = serverList[position]

        viewHolder.hostName.text = server.hostName
        viewHolder.ipAddr.text = server.ipAddr
        viewHolder.ping.text = server.ping
        viewHolder.country.text = server.country
        viewHolder.operator.text = server.operator

        viewHolder.saveButton.setOnClickListener {
            listener.onButtonClick(server)
        }
    }
    override fun getItemCount() = serverList.size
}