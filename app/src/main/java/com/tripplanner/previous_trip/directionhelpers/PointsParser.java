package com.tripplanner.previous_trip.directionhelpers;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tripplanner.previous_trip.MapOfAllTripsFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class PointsParser extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
    TaskLoadedCallback taskCallback;
    String directionMode = "driving";
    List< PolylineOptions>  lineOptions =new  ArrayList<PolylineOptions>();
    public PointsParser(TaskLoadedCallback mContext, String directionMode) {
        this.taskCallback = (TaskLoadedCallback) mContext;
        this.directionMode = directionMode;
    }

    // Parsing the data in non-ui thread
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);
            Log.d("mylog", jsonData[0].toString());
            DataParser parser = new DataParser();
            Log.d("mylog", parser.toString());

            // Starts parsing data
            routes = parser.parse(jObject);
            Log.d("mylog", "Executing routes");
            Log.d("mylog", routes.toString());

        } catch (Exception e) {
            Log.d("mylog", e.toString());
            e.printStackTrace();
        }
        return routes;
    }

    // Executes in UI thread, after the parsing process
    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        ArrayList<LatLng> points;
        // Traversing through all the routes
        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList<>();

            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);
            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);
                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);
                points.add(position);
            }
            // Adding all the points in the route to LineOptions
            PolylineOptions p=new PolylineOptions();
            p.addAll(points);
            lineOptions.add(p);

            if (directionMode.equalsIgnoreCase("walking")) {
                p.width(10);
                p.color(Color.MAGENTA);
            } else {
                p.width(20);
                p.color(Color.BLUE);
            }
            Log.d("mylog", "onPostExecute lineoptions decoded");
        }

        if (lineOptions != null) {
            taskCallback.onTaskDone(lineOptions);

        } else {
            Log.d("mylog", "without Polylines drawn");
        }
    }
}
