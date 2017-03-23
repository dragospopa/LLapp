package georgia.languagelandscape;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DialogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        TextView tv_recording_title= (TextView) findViewById(R.id.tv_recording_title);
        tv_recording_title.setText();
        Button button_play_recording= (Button) findViewById(R.id.button_play_recording);
        final MediaPlayer mp = MediaPlayer.create(this, );
        button_play_recording.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                mp.start();
            }
        });
    }

    public static void getTitle(String title)
    {

    }


}
