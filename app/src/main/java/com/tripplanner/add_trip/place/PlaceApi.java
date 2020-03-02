package com.tripplanner.add_trip.place;

import android.util.Log;

import com.tripplanner.data_layer.local_data.entity.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PlaceApi {
    private static final String key = "AIzaSyC63bAG-cHY03C2tauZuvF1YWk_zlTxnoM";

    public ArrayList<Place> autoComplete(String input) {
        ArrayList<Place> arrayList = new ArrayList();
        HttpURLConnection connection = null;
        StringBuilder jsonResult = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
            sb.append("query=" + input);
            sb.append("&key=" + key);
            URL url = new URL(sb.toString());
            connection = (HttpURLConnection) url.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());

            int read;

            char[] buff = new char[1024];
            while ((read = inputStreamReader.read(buff)) != -1) {
                jsonResult.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonResult.toString());
            JSONArray prediction = jsonObject.getJSONArray("results");
            for (int i = 0; i < prediction.length(); i++) {
                String name = prediction.getJSONObject(i).getString("name");
                double lat = prediction.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                double lng = prediction.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");

                arrayList.add(new Place(name, lat, lng));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;
    }
}