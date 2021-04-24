package com.mprzypasniak.allegroGithub.repoDetails

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mprzypasniak.allegroGithub.databinding.RepoDetailsElementBinding

class RepoDetailsAdapter(private val dataSet: ArrayList< Pair<String, String> >):
    RecyclerView.Adapter<RepoDetailsAdapter.RepoDetailsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoDetailsHolder {
        val binding = RepoDetailsElementBinding.inflate(LayoutInflater.from(parent.context),
        parent, false)

        return RepoDetailsHolder(binding)
    }

    override fun getItemCount(): Int {
        Log.i("details", "Size: %d".format(dataSet.size))
        return dataSet.size
    }

    override fun onBindViewHolder(holder: RepoDetailsHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    inner class RepoDetailsHolder(private val elemBinding: RepoDetailsElementBinding):
        RecyclerView.ViewHolder(elemBinding.root) {

        fun bind(values: Pair<String, String>) {
            elemBinding.fieldName.text = values.first
            elemBinding.fieldContent.text = values.second
        }
    }
}