package com.mprzypasniak.allegroGithub

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mprzypasniak.allegroGithub.databinding.ReposListElementBinding
import org.json.JSONArray
import org.json.JSONObject

class ReposListAdapter(private val dataSet: JSONArray): RecyclerView.Adapter<ReposListAdapter.RepoHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoHolder {
        val elementBinding = ReposListElementBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)

        return RepoHolder(elementBinding)
    }

    override fun getItemCount(): Int {
        Log.i("Adapter", "Length of dataset: %d".format(dataSet.length()))
        return dataSet.length()
    }

    override fun onBindViewHolder(holder: RepoHolder, position: Int) {
        holder.bind(dataSet.getJSONObject(position))
        Log.i("Adapter", dataSet[position].toString())
    }

    inner class RepoHolder(private val elemBinding: ReposListElementBinding):
        RecyclerView.ViewHolder(elemBinding.root) {

        fun bind(repo: JSONObject) {
            elemBinding.repoName.text = repo.getString("name")
            elemBinding.repoDesc.text = repo.getString("description")
        }
    }
}