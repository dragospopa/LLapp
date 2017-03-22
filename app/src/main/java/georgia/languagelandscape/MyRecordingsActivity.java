package georgia.languagelandscape;

import android.os.Bundle;

public class MyRecordingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrecordings_nav_drawer);
        super.onDrawerCreated();

    }
}
