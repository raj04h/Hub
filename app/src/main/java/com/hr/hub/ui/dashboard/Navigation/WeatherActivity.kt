package com.hr.hub.ui.dashboard.Navigation

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hr.hub.R
import com.hr.hub.ui.APIFile
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class WeatherActivity: AppCompatActivity() {

    private lateinit var etlocation:EditText
    private lateinit var btnenter:Button
    private lateinit var tvweather:TextView
    private val client=OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        etlocation=findViewById(R.id.et_city)
        btnenter=findViewById(R.id.btn_enter)
        tvweather=findViewById(R.id.tv_weatherres)

        btnenter.setOnClickListener{
            val city=etlocation.text.toString()

            if (city.isNotEmpty()){
                fetchWeather(city)
            }
            else{
                tvweather.text="Please Enter City"
            }
        }

    }

    private fun fetchWeather(city:String){
        val apikey=APIFile.weatherAPI
        val URL="https://api.openweathermap.org/data/2.5/weather?q=$city &appid=$apikey&units=metric"

        val request=Request.Builder()
            .url(URL)
            .build()

        client.newCall(request).enqueue(object :Callback{

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread{
                    tvweather.text="Failed to load"
                }
            }

            override fun onResponse(call: Call, response: Response) {

                if (!response.isSuccessful) {
                    runOnUiThread {
                        tvweather.text = "Error: ${response.message} (${response.code})"
                    }
                    return
                }

                response.body?.let { responseBody ->
                    try { 
                        val jsonResponse = JSONObject(responseBody.string())

                        if (jsonResponse.has("weather") && jsonResponse.has("main")){

                            val weatherDescription = jsonResponse
                                .getJSONArray("weather")
                                .getJSONObject(0)
                                .getString("description")

                            val temperature = jsonResponse
                                .getJSONObject("main")
                                .getDouble("temp")

                            val humidity=jsonResponse
                                .getJSONObject("main")
                                .getInt("humidity")

                            val weadtherResult = "City= $city\nTemperature= $temperature°C\nHumidity: $humidity%\nDescription: $weatherDescription"
                            runOnUiThread {
                                tvweather.text = weadtherResult
                            }
                        }
                        else{
                            runOnUiThread{
                                tvweather.text="Error: Unable to retrieve weather information."
                            }
                        }
                    }
                    catch (e:JSONException){
                        runOnUiThread{
                            tvweather.text="Parsing Error: ${e.localizedMessage}"
                        }
                    }
                }?: runOnUiThread{
                    tvweather.text="Error: Empty response from the server."
                }
            }

        })
    }
}