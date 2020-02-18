package com.tripplanner.data_layer.local_data;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeConverter {
    static DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", Locale.US);

    @TypeConverter
    public static Date fromTimeStamp(String value) {
        if (value != null) {
            try {
                return df.parse(value);


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
