package com.example.helloworld;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {
    public static final String USERS = "https://reqres.in/api/users/";

    Context context;

    public WeatherDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener{
        void onError(String message);
        void onResponse(String cityID);
    }
    public void getCityID(String cityName, VolleyResponseListener volleyResponseListener)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        String number = cityName;
        String url = USERS + number;

        final String[] id = new String[1];

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        id[0] = "";
                        try {

                            JSONObject name = response.getJSONObject("data");
                            id[0] = name.getString("id");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                     //   Toast.makeText(context, "Avatar = " + avatar[0], Toast.LENGTH_SHORT).show();
                        volleyResponseListener.onResponse(id[0]);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  Toast.makeText(context, "Something wrong", Toast.LENGTH_SHORT).show();
                volleyResponseListener.onError("Something error");
            }
        });
// Request a string response from the provided URL.

// Add the request to the RequestQueue.
        Singleton.getInstance(context).addToRequestQueue(request);

       // return avatar[0];

    }

    public interface  ForecaseByIDResponse{
        void onError(String message);

        void onResponse(List<WeatherReportModel> weatherReportModels);

    }

    public void getCityForecastbyID(String cityID,  ForecaseByIDResponse forecaseByIDResponse)
    {
        String url = USERS.substring(0,USERS.length()-1) + "?page=" + cityID;
        List<WeatherReportModel> report = new ArrayList<>();

        //get the json Object
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray data = response.getJSONArray("data");
                            Toast.makeText(context, data.getJSONObject(0).toString(), Toast.LENGTH_SHORT).show();

                            for(int i=0; i<data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);
                                WeatherReportModel one_day = new WeatherReportModel();

                                one_day.setId((Integer) object.get("id"));
                                one_day.setEmail(object.getString("email"));
                                one_day.setFirst_name(object.getString("first_name"));
                                one_day.setLast_name(object.getString("last_name"));
                                one_day.setAvatar(object.getString("avatar"));

                                report.add(one_day);

                            }
                            forecaseByIDResponse.onResponse(report);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
    });
        Singleton.getInstance(context).addToRequestQueue(request);



   }

    public interface GetCityForecastByNameCallback{
        void onError(String message);
        void onResponse(List<WeatherReportModel> weatherReportModels);
    }

    public void getCityForecastbyName(String cityName, GetCityForecastByNameCallback getCityForecastByNameCallback)
    {
        getCityID(cityName, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(String cityID) {

                getCityForecastbyID(cityID, new ForecaseByIDResponse() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
                        getCityForecastByNameCallback.onResponse(weatherReportModels);
                    }
                });
            }
        });
    }
}
