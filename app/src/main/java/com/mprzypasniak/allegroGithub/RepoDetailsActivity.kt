package com.mprzypasniak.allegroGithub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mprzypasniak.allegroGithub.databinding.ActivityRepoDetailsBinding
import com.mprzypasniak.allegroGithub.repoDetails.RepoDetailsAdapter
import org.json.JSONObject

class RepoDetailsActivity : AppCompatActivity() {
    private val details = ArrayList< Pair<String, String> >()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityRepoDetailsBinding.inflate(this.layoutInflater)
        setContentView(binding.root)

        val detailsObject = JSONObject(intent.getStringExtra("details"))
        // sent in MainActivity

        binding.repoDetailsName.text = detailsObject["name"].toString()

        prepareList(detailsObject) // prepare data for display

        binding.detailsList.layoutManager = LinearLayoutManager(this)
        binding.detailsList.itemAnimator = DefaultItemAnimator()
        binding.detailsList.adapter = RepoDetailsAdapter(details)
    }

    private fun prepareList(json: JSONObject) {
        val attrList = listOf<String>("full_name", "created_at", "homepage", "size",
            "watchers_count", "language", "forks_count")
        val etiquettes = resources.getStringArray(R.array.details_etiquetes)

        details.clear()

        for(i in attrList.indices) {
            details.add( Pair(etiquettes[i], json[attrList[i]].toString()) )
        }
    }
}