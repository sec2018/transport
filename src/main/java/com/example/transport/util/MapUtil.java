package com.example.transport.util;

public class MapUtil {
    private static double EARTH_RADIUS  = 6378.137; //地球半径

    public static double getDistance(String lng1,String lat1,String lng2,String lat2){
        double s = 0;
        try {
            double radlat1 = rad(lat1);
            double radlat2 = rad(lat2);
            double a = radlat1 - radlat2;
            double b = rad(lng1) - rad(lng2);
            s = 2*Math.asin(Math.sqrt(Math.pow(Math.sin(a/2.0),2)+Math.cos(radlat1)*Math.cos(radlat2)*Math.pow(Math.sin(b/2.0),2)));
            s = s * EARTH_RADIUS;
            s = Math.round(s * 10000.0)/10000.0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return s;
    }

    private static double rad(String d){
        double res = 0;
        try {
            res = Double.valueOf(d);
        }catch (Exception e){
            e.printStackTrace();
        }
        return res*Math.PI/180.0;
    }
}
