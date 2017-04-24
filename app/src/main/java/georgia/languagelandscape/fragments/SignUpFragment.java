package georgia.languagelandscape.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import georgia.languagelandscape.R;

public class SignUpFragment extends Fragment {

    private Context context;

    private EditText emailField;
    private EditText passwordField;
    private EditText usernameField;
    private Button signupButton;
    private TextView loginText;
    private TextView termsText;

    public SignUpFragment() {
    }

    public interface SignUpFragmentListener {
        public void onSignUpClick();

        public void onLoginTextClick();

        public void onTermsClick();
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

        View view = inflater.inflate(R.layout.fragment_create_account, container, false);

        emailField = (EditText) view.findViewById(R.id.emailText);
        passwordField = (EditText) view.findViewById(R.id.passText);
        usernameField = (EditText) view.findViewById(R.id.nameText);
        signupButton = (Button) view.findViewById(R.id.signUp);
        loginText = (TextView) view.findViewById(R.id.logIn);
        termsText = (TextView) view.findViewById(R.id.terms);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: collects info and create user
                if (context instanceof SignUpFragmentListener) {
                    ((SignUpFragmentListener) context).onSignUpClick();
                }
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof SignUpFragmentListener) {
                    ((SignUpFragmentListener) context).onLoginTextClick();
                }
            }
        });

        String terms = context.getResources().getString(R.string.terms_of_service);
        SpannableString content = new SpannableString(terms);
        String underlineString = "Terms of Service";
        int start = terms.indexOf(underlineString);
        int end = start + underlineString.length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (context instanceof SignUpFragmentListener) {
                    ((SignUpFragmentListener) context).onTermsClick();
                }
            }
        };
        content.setSpan(new UnderlineSpan(), start, end, 0);
        content.setSpan(clickableSpan, start, end, 0);
        termsText.setText(content);

        return view;
    }
}
