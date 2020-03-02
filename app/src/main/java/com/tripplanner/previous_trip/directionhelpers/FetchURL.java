package com.tripplanner.previous_trip.directionhelpers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.tripplanner.previous_trip.MapContinerFragment;
import com.tripplanner.previous_trip.MapOfAllTripsFragment;
import com.tripplanner.previous_trip_details.previousTripDetailsFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class FetchURL extends AsyncTask<String, Void, String> {
    TaskLoadedCallback mContext;
    previousTripDetailsFragment previousTripDetailsFragment;
    MapOfAllTripsFragment mapOfAllTripsFragment;
    String directionMode = "driving";

   /* public FetchURL(MapOfAllTripsFragment mContext) {
        this.mContext = mContext;
    }
*/
    public FetchURL(previousTripDetailsFragment previousTripDetailsFragment) {
        this.mContext = previousTripDetailsFragment;
    }
public FetchURL(MapContinerFragment mContext)
{
    this.mContext = mContext;
}
    @Override
    protected String doInBackground(String... strings) {
        // For storing data from web service
        String data = "";
        directionMode = strings[1];
        try {
            // Fetching the data from web service
            data = downloadUrl(strings[0]);
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        PointsParser parserTask = new PointsParser(mContext, directionMode);
        // Invokes the thread for parsing the JSON data
        parserTask.execute(s);
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}

