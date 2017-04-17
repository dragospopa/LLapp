package georgia.languagelandscape;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;


public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  Bundle b=getIntent().getExtras();
        String recording_title;
      //  recording_title=b.getString("recording_title");
        setContentView(R.layout.content_dialog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tv_title=(TextView) findViewById(R.id.tv_name_recording);
     //   tv_title.setText(recording_title);
    }

}