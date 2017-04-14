package georgia.languagelandscape.data;

import java.util.ArrayList;

/**
 * Created by Georgia on 3/18/2017.
 */

public class Markers {

    public static ArrayList<Double> longitudes=new ArrayList<>();
    public static ArrayList<Double> latitudes=new ArrayList<>();
    public static ArrayList<String> titles=new ArrayList<>();

    public static void AddLatitude(double latitude)
    {
        latitudes.add(latitude);
    }

    public static void AddTitle(String title)
    {
        titles.add(title);
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

    public static ArrayList<String> getTitles()
    {
        return titles;
    }

}
