package georgia.languagelandscape.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import georgia.languagelandscape.R;
import georgia.languagelandscape.data.Recording;

public class RecordingAdaptor extends RecyclerView.Adapter<RecordingAdaptor.ViewHolder>{

    private Context context;
    private List<Recording> recordings;

    public RecordingAdaptor(Context context, List<Recording> recordings) {
        this.context = context;
        this.recordings = recordings;
    }

    @Override
    public RecordingAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_recordings, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecordingAdaptor.ViewHolder holder, int position) {
        final Recording recording = recordings.get(position);

        holder.rlTitle.setText(recording.getTitle());
        holder.rlLanguages.setText(recording.getLanguage().get(0));
        holder.rlSpeakers.setText(recording.getSpeakers().get(0));
        holder.rlLocation.setText(recording.getLocation());
        holder.rlDate.setText(recording.getDate());

        // TODO: set onclick listener for both more button and the whole view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "clicked" + recording.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return recordings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public View itemView;
        public TextView rlTitle;
        public TextView rlLocation;
        public TextView rlSpeakers;
        public TextView rlLanguages;
        public TextView rlDate;
        public Button moreButton;

        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            rlTitle = (TextView) itemView.findViewById(R.id.recordingList_title);
            rlLocation = (TextView) itemView.findViewById(R.id.recordingList_location);
            rlSpeakers = (TextView) itemView.findViewById(R.id.recordingList_speakers_content);
            rlLanguages = (TextView) itemView.findViewById(R.id.recordingList_languages_content);
            rlDate = (TextView) itemView.findViewById(R.id.recordingList_date);
            moreButton = (Button) itemView.findViewById(R.id.recordingList_more);
        }
    }
}
