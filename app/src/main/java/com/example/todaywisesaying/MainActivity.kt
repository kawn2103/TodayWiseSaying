package com.example.todaywisesaying

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.todaywisesaying.databinding.ActivityMainBinding
import com.example.todaywisesaying.databinding.ItemQuoteBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initViews()
    }

    private fun initData(){
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
        )
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            binding.progressBar.visibility = View.GONE
            if(it.isSuccessful){
                val quotes = parseQuotesJson(remoteConfig.getString("quotes"))
                val isNameRevealed = remoteConfig.getBoolean("is_name_revealed")
                displayQuotesPager(quotes,isNameRevealed)
            }
        }
    }

    private fun initViews(){
        binding.viewPager.setPageTransformer { page, position ->
            when{
                position.absoluteValue >= 1F ->{
                    page.alpha = 0F
                }
                position == 0F ->{
                    page.alpha = 1F
                }
                else -> {
                    page.alpha = 1F- 2 * position.absoluteValue
                }
            }
        }
    }

    private fun parseQuotesJson(json: String): List<Quote>{
        val jsonArray = JSONArray(json)
        var jsonList = emptyList<JSONObject>()
        for(index in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(index)
            jsonObject?.let {
                jsonList = jsonList + it
            }
        }

        return jsonList.map {
            Quote(
                it.getString("quote"),
                it.getString("name")
            )
        }
    }

    private fun displayQuotesPager(quotes : List<Quote>, isNameRevealed: Boolean){
        val adapter = QuotesPagerAdapter(
            quotes,
            isNameRevealed
        )
        binding.viewPager.adapter = adapter
        binding.viewPager.setCurrentItem(adapter.itemCount/2,false)
    }
}