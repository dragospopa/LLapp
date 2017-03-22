package georgia.languagelandscape.data;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by Georgia on 3/18/2017.
 */

public class Markers {

    public static ArrayList<Double> longitudes=new ArrayList<>();
    public static ArrayList<Double> latitudes=new ArrayList<>();
    public static ArrayList<Pair<Double, Double>> latLong= new ArrayList<>();

    public static void AddLatitude(double latitude)
    {
        latitudes.add(latitude);
    }

    public static void AddLongitude(double longitude)
    {
        longitudes.add(longitude);

    }

    public static ArrayList<Double> getLongitudes()
    {
        return longitudes;
    }

    public static ArrayList<Double> getLatitudes()
    {
        return latitudes;
    }

}
