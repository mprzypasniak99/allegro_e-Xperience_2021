package com.mprzypasniak.allegroGithub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.mprzypasniak.allegroGithub.databinding.ActivityMainBinding
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = JSONArray() // we will store downloaded info about repos here
        val adapter = ReposListAdapter(data) // adapter for a RecyclerView

        binding.listBody.layoutManager = LinearLayoutManager(this)
        binding.listBody.itemAnimator = DefaultItemAnimator()
        binding.listBody.adapter = adapter

        requestQueue = Volley.newRequestQueue(this)

        val url = "https://api.github.com/orgs/allegro/repos"
        val request = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener { response: JSONArray ->
                for (i in 0 until response.length()) {
                    data.put(response[i])
                }
                binding.progressBar.visibility = View.GONE // hide progress bar
                binding.listBody.visibility = View.VISIBLE // show list of repositories

                adapter.notifyDataSetChanged() // notify RecyclerView that data has been changed

                Log.i("Github_API", "Received info about %d repos".format(response.length()))
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, R.string.github_fail, Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.INVISIBLE
                Log.i("Github_API", error.toString())
            })

        requestQueue.add(request)

    }
}