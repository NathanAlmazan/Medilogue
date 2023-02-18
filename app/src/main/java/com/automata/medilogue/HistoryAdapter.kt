package com.automata.medilogue

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>(){

    private var name = arrayOf("Kyle", "Nathan", "Nica", "Trisha", "Jem")
    private var result = arrayOf("Result 1", "Result 2", "Result 3", "Result 4", "Result 5")
    private var date = arrayOf("February 18, 2023", "February 18, 2023", "February 18, 2023", "February 18, 2023", "February 18, 2023")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
        holder.itemName.text = name[position]
        holder.itemResult.text = result[position]
        holder.itemDate.text = date[position]
    }

    override fun getItemCount(): Int {
        return name.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var itemName: TextView
        var itemResult: TextView
        var itemDate: TextView

        init {
            itemName = itemView.findViewById((R.id.tvName))
            itemResult = itemView.findViewById((R.id.tvResult))
            itemDate = itemView.findViewById((R.id.tvDate))
        }
    }
}