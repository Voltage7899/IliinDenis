package com.company.iliindenis.fragments

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.company.iliindenis.DayWeather
import com.company.iliindenis.MainViewModel
import com.company.iliindenis.R
import com.company.iliindenis.databinding.FragmentMainBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject

const val API_KEY="cdc17cb52992487497a153615222110"
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var plauncher:ActivityResultLauncher<String>
    private val viewModel:MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()

        sendRequest("Moscow")
        update()

        binding.find.setOnClickListener {
            sendRequest(binding.cityFind.text.toString())
        }
    }

    fun permissionListener(){
        plauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            Toast.makeText(activity,"Yes $it", Toast.LENGTH_LONG).show()
        }
    }
    fun checkPermission(){
        if(!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
            permissionListener()
            plauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    fun sendRequest(city:String){

        val url = "https://api.weatherapi.com/v1/current.json?key=$API_KEY&q=$city&aqi=no"
        val queue = Volley.newRequestQueue(activity)
        val request = StringRequest(Request.Method.GET,url,{
            result->parseFromJSONtoObject(result)
        },
            {
                error-> Toast.makeText(activity,"Ошибка "+error,Toast.LENGTH_LONG).show()
            })
        queue.add(request)

    }

    fun update() = with(binding){
        viewModel.liveDataCurrent.observe(viewLifecycleOwner, Observer {
            dataUpdate.text=it.time
            city.text=it.city
            statusText.text=it.condition
            temp.text=it.currentTemp
            Picasso.get().load("https:"+it.imageUrl).into(statusImage)
        })
    }

    fun parseFromJSONtoObject(res: String) {

        val mainObject = JSONObject(res)
        val day = DayWeather(
            mainObject.getJSONObject("location").getString("name"),
            mainObject.getJSONObject("current").getString("last_updated"),
            mainObject.getJSONObject("current").getJSONObject("condition").getString("text"),
            mainObject.getJSONObject("current").getJSONObject("condition").getString("icon"),
            mainObject.getJSONObject("current").getString("temp_c")
        )
        Toast.makeText(activity,"Температура "+day.imageUrl,Toast.LENGTH_LONG).show()
        viewModel.liveDataCurrent.value=day
    }

    companion object {


        @JvmStatic
        fun newInstance() = MainFragment()
    }
}