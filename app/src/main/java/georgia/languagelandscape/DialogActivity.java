package georgia.languagelandscape;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.TextView;

import georgia.languagelandscape.data.Markers;


public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.content_dialog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tv_title=(TextView) findViewById(R.id.tv_name_recording);

        String recording_title="Title: " + Markers.getCurrent_title();

        tv_title.setText(recording_title);

    }
}