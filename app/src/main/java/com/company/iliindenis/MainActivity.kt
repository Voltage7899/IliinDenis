package com.company.iliindenis


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.company.iliindenis.databinding.ActivityMainBinding
import com.company.iliindenis.fragments.MainFragment
import org.json.JSONObject

const val API_KEY ="cdc17cb52992487497a153615222110"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(R.id.main_fragment_place_holder,MainFragment.newInstance()).commit()

    }
}