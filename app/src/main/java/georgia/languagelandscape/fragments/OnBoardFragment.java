/**
 * Copyright (C) 2017 Language Landscape Organisation - All Rights Reserved
 *
 * Reference list:
 *      bumptech, Glide 3.7.0, 2016
 *
 */
package georgia.languagelandscape.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import georgia.languagelandscape.R;
import georgia.languagelandscape.data.User;

/**
 * A simple dummy onboarding page including log in and sign up
 * when the user first open the app.
 *
 * Activities that contains this fragment must implements
 * {@link OnBoardFragmentListener} interface to interact with
 * login, signup and forgot password events
 */
public class OnBoardFragment extends Fragment {

    private Context context;

    private Button createAccountButton;
    private Button loginButton;
    private ImageView logoView;
    private EditText userText;
    private EditText passwordText;
    private RequestQueue mRequestQueue;
    private TextView forgotPass;

    public OnBoardFragment() {
    }

    public interface OnBoardFragmentListener {
        public void onLoginClick(JSONObject jsonObject);

        public void onCreateAccountClick();

        public void onForgotPassClick();
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

        View view = inflater.inflate(R.layout.fragment_onboarding, container, false);

        createAccountButton = (Button) view.findViewById(R.id.create_account);
        loginButton = (Button) view.findViewById(R.id.login);
        logoView = (ImageView) view.findViewById(R.id.logo);
        userText = (EditText) view.findViewById(R.id.nameText);
        passwordText = (EditText) view.findViewById(R.id.passText);
        forgotPass = (TextView) view.findViewById(R.id.forgetpass);

        // display the logo
        Drawable logo = context
                .getResources()
                .getDrawable(R.drawable.ll_logo_full);
        Bitmap bm = ((BitmapDrawable) logo).getBitmap();
        logo = new BitmapDrawable(
                context.getResources(),
                Bitmap.createScaledBitmap(
                        bm,
                        (int) Math.round(bm.getWidth() * 0.08),
                        (int) Math.round(bm.getHeight() * 0.08),
                        true));
        logoView.setImageDrawable(logo);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // initialize a user
                if (mRequestQueue == null) {
                    mRequestQueue = Volley.newRequestQueue(context);
                }

                String name = userText.getText().toString();
                String pass = passwordText.getText().toString();

                User user = new User();

                user.setUsername(name);
                user.setPassword(pass);

                Map<String, String> map = new HashMap<>();

                map.put("username", user.getUsername());
                map.put("password", user.getPassword());

                JSONObject json = new JSONObject(map);

                String getUser = userText.getText().toString();
                User.addUser(getUser);

                if (context instanceof OnBoardFragmentListener) {
                    ((OnBoardFragmentListener) context).onLoginClick(json);
                }
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof OnBoardFragmentListener) {
                    ((OnBoardFragmentListener) context).onCreateAccountClick();
                }
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof OnBoardFragmentListener) {
                    ((OnBoardFragmentListener) context).onForgotPassClick();
                }
            }
        });

        return view;
    }
}
