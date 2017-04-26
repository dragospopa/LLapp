/**
 * Copyright (C) 2017 Language Landscape Organisation - All Rights Reserved
 *
 * Reference list:
 *      bumptech, Glide 3.7.0, 2016
 *
 */
package georgia.languagelandscape.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import georgia.languagelandscape.R;
import georgia.languagelandscape.data.Recording;
import georgia.languagelandscape.util.RecordingAdaptor;

/**
 * A simple {@link DialogFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeleteDialogListener} interface to handle interaction events.
 */
public class DeleteDialogFragment extends DialogFragment{

    private Context context;

    public interface DeleteDialogListener {
        public void onDeleteClick(String id, String title, int index);
    }
    public DeleteDialogFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String id = getArguments().getString(Recording.PARCEL_ID_KEY);
        final int adaptorPosition = getArguments().getInt(RecordingAdaptor.ADAPTOR_POSITION);
        final String title = getArguments().getString(Recording.PARCEL_TITLE_KEY);

        AlertDialog.Builder builder =
                new AlertDialog.Builder(context)
                        .setTitle("Delete Recording")
                        .setMessage("Delete this recording?")
                        .setNegativeButton(R.string.dialog_negativeButton,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                        .setPositiveButton(R.string.dialog_delete_positiveButton,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DeleteDialogListener listener =
                                                (DeleteDialogListener) getActivity();
                                        listener.onDeleteClick(id, title, adaptorPosition);
                                        dialog.dismiss();
                                    }
                                });
        return builder.create();
    }

    /**
     * The factory method that creates new instance of this fragment
     * @param id the id of the recording to be deleted
     * @param title the title of the recording; needed to find the file path to the recording file
     * @param position adaptor position indicating which view to delete
     * @return an instance of this class
     */
    public static DeleteDialogFragment newInstance(String id, String title, int position) {
        DeleteDialogFragment dialog = new DeleteDialogFragment();

        Bundle args = new Bundle();
        args.putString(Recording.PARCEL_ID_KEY, id);
        args.putString(Recording.PARCEL_TITLE_KEY, title);
        args.putInt(RecordingAdaptor.ADAPTOR_POSITION, position);
        dialog.setArguments(args);

        return dialog;
    }
}
