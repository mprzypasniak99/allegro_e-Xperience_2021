package com.mprzypasniak.allegroGithub

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.mprzypasniak.allegroGithub.databinding.ActivityMainBinding
import org.json.JSONArray

class MainActivity : AppCompatActivity(), ListElementMediator {
    private lateinit var requestQueue: RequestQueue
    private val data = JSONArray() // we will store downloaded info about repos here

    private lateinit var adapter: ReposListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ReposListAdapter(data) // adapter for a RecyclerView

        binding.listBody.layoutManager = LinearLayoutManager(this)
        binding.listBody.itemAnimator = DefaultItemAnimator()
        binding.listBody.adapter = adapter

        requestQueue = Volley.newRequestQueue(this)


        getGithubData(binding)
    }

    private fun getGithubData(binding: ActivityMainBinding) {
        val url = "https://api.github.com/orgs/allegro/repos"
        val request = object : JsonArrayRequest(Request.Method.GET, url, null,
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
            }) {
            override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONArray> {
                var cacheEntry : Cache.Entry? = HttpHeaderParser.parseCacheHeaders(response)

                // if caching is not enabled on the site, crate new cache entry
                if(cacheEntry == null) {
                    cacheEntry = Cache.Entry()
                }

                val cacheHitButRefreshed =
                    5 * 60 * 1000.toLong() // in 5 minutes cache will be hit, but also refreshed on background

                val cacheExpired =
                    1 * 60 * 60 * 1000.toLong() // in 1 hour cache entry will be deleted

                val now = System.currentTimeMillis()
                val softExpire = now + cacheHitButRefreshed
                val ttl = now + cacheExpired
                cacheEntry.data = response!!.data
                cacheEntry.softTtl = softExpire
                cacheEntry.ttl = ttl

                var headerValue: String?
                headerValue = response.headers!!["Date"]
                if (headerValue != null) {
                    cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue)
                }
                headerValue = response.headers!!["Last-Modified"]
                if (headerValue != null) {
                    cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue)
                }
                cacheEntry.responseHeaders = response.headers
                val jsonString = String(
                    response.data,
                    charset(HttpHeaderParser.parseCharset(response.headers))
                )
                return Response.success(JSONArray(jsonString), cacheEntry)
            }
        }

        requestQueue.add(request)
    }

    override fun onElementClick(index: Int) {
        val i = Intent(this, RepoDetailsActivity::class.java)
        i.putExtra("details", data[index].toString())

        startActivity(i)
    }
}