package ru.mirea.bogomolovaa.mireaproject.ui.heroes

import android.app.AlertDialog
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import ru.mirea.bogomolovaa.mireaproject.R
import ru.mirea.bogomolovaa.mireaproject.adapter.HeroesAdapter
import ru.mirea.bogomolovaa.mireaproject.model.Hero
import ru.mirea.bogomolovaa.mireaproject.databinding.FragmentHeroesBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class HeroesFragment : Fragment() {

    companion object {
        private val TAG = HeroesFragment::class.java.simpleName
        private const val BASE_URL =
            "https://www.simplifiedcoding.net/demos/marvel/"
        fun newInstance() = HeroesFragment()
    }

    private val viewModel: HeroesViewModel by viewModels()
    private var _binding: FragmentHeroesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HeroesAdapter
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeroesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = AlertDialog.Builder(requireContext())
            .setMessage("Loading...")
            .setIcon(R.drawable.loading_img)
            .create()

        adapter = HeroesAdapter()
        binding.recyclerHeroList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HeroesFragment.adapter
        }

        val connectivityManager =
            requireContext().getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (networkCapabilities != null &&
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        ) {
            lifecycleScope.launch {
                loadHeroes()
            }
        } else {
            Toast.makeText(requireContext(), "Нет интернета", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun loadHeroes() {
        dialog.show()

        val result = fetchHeroes()
        result?.let {
            val heroes = parseHeroes(it)
            adapter.submitList(heroes)
        }

        dialog.dismiss()
    }

    private suspend fun fetchHeroes(): String? =
        withContext(Dispatchers.IO) {
            var inputStream: InputStream? = null
            var data: String? = null
            try {
                val url = URL(BASE_URL)
                val connection = url.openConnection() as HttpURLConnection

                connection.readTimeout = 100000
                connection.connectTimeout = 100000
                connection.requestMethod = "GET"
                connection.instanceFollowRedirects = true
                connection.useCaches = false
                connection.doInput = true

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.inputStream
                    data = inputStream.bufferedReader().use(BufferedReader::readText)
                } else {
                    data = "${connection.responseMessage}. Error Code: $responseCode"
                }
                connection.disconnect()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                inputStream?.close()
            }
            data
        }

    private fun parseHeroes(jsonArrayString: String?): List<Hero> {
        val heroes = mutableListOf<Hero>()
        jsonArrayString?.let {
            Log.d(TAG, jsonArrayString)
            val jsonArray = JSONArray(it)
            Log.d(TAG, jsonArray.toString())
            for (i in 0 until jsonArray.length()) {
                Log.d(TAG, jsonArray.getJSONObject(i).toString())
                val jsonObject = jsonArray.getJSONObject(i)
                val name = jsonObject.getString("name")
                val realName = jsonObject.getString("realname")
                val team = jsonObject.getString("team")
                val firstAppearance = jsonObject.getString("firstappearance")
                val createdBy = jsonObject.getString("createdby")
                val publisher = jsonObject.getString("publisher")
                val imageUrl = jsonObject.getString("imageurl")
                val bio = jsonObject.getString("bio")

                val hero = Hero(name, realName, team, firstAppearance, createdBy, publisher, imageUrl, bio)
                heroes.add(hero)
            }
        }
        return heroes
    }
}