package georgia.languagelandscape.data;

import java.util.ArrayList;

/**
 * Created by Georgia on 3/15/2017.
 */

public class Projects {

    static ArrayList<String> names= new ArrayList<String>();

    public static void addItem(String name)
    {
        names.add(name);
    }

    public static ArrayList getArrayList()
    {
        for(int i=0;i<names.size();i++)
            System.out.println(names.get(i));
        return names;
    }
}
