package georgia.languagelandscape.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import georgia.languagelandscape.R;

public class ForgotPassFragment extends Fragment {

    private Context context;

    private EditText emailField;
    private Button sendButton;

    public ForgotPassFragment() {
    }

    public interface ForgotPassFragmentListener {
        public void onSendClick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forgot_pass, container, false);

        emailField = (EditText) view.findViewById(R.id.reset_pass_email);
        sendButton = (Button) view.findViewById(R.id.send);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (context instanceof ForgotPassFragmentListener) {
                    ((ForgotPassFragmentListener) context).onSendClick();
                }
                Toast.makeText(context, "email sent", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
