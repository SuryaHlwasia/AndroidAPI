package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnCityID, btnGetWeatherbyID, btnGetWeatherByName;
    EditText etDataInput;
    ListView lvWeatherReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCityID = findViewById(R.id.btn_getCityID);
        btnGetWeatherbyID = findViewById(R.id.btn_getWeatherByCityID);
        btnGetWeatherByName = findViewById(R.id.btn_getWeatherByCityName);

        etDataInput = findViewById(R.id.et_dataInput);
        lvWeatherReport = findViewById(R.id.lv_weatherReports);

        WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);

        btnGetWeatherbyID.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     weatherDataService.getCityForecastbyID(etDataInput.getText().toString(), new WeatherDataService.ForecaseByIDResponse() {
                                                         @Override
                                                         public void onError(String message) {
                                                             Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                                         }

                                                         @Override
                                                         public void onResponse(List<WeatherReportModel> weatherReportModels) {
                                                             ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModels);
                                                             lvWeatherReport.setAdapter(arrayAdapter);
                                                             Toast.makeText(MainActivity.this, weatherReportModels.get(0).toString(), Toast.LENGTH_SHORT).show();
                                                         }

                                                     });
                                                 }
                                             });


                btnCityID.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        weatherDataService.getCityID(etDataInput.getText().toString(), new WeatherDataService.VolleyResponseListener() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(MainActivity.this, "SOMETHING WRONG", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String cityID) {
                                Toast.makeText(MainActivity.this, "Returned ID of " + cityID, Toast.LENGTH_SHORT).show();
                            }
                        });
                        // Toast.makeText(MainActivity.this, "Returned = " + cityID, Toast.LENGTH_SHORT).show();
                    }
                });

                btnGetWeatherByName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        weatherDataService.getCityForecastbyName(etDataInput.getText().toString(), new WeatherDataService.GetCityForecastByNameCallback() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(List<WeatherReportModel> weatherReportModels) {
                                ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModels);
                                lvWeatherReport.setAdapter(arrayAdapter);
                                Toast.makeText(MainActivity.this, weatherReportModels.get(0).toString(), Toast.LENGTH_SHORT).show();
                            }

                        });

                    }
                });

            }
        }