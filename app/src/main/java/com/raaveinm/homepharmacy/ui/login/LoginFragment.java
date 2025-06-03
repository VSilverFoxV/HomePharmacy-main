package com.raaveinm.homepharmacy.ui.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.raaveinm.homepharmacy.MainActivity;
import com.raaveinm.homepharmacy.R;
import com.raaveinm.homepharmacy.domain.ImageFetching;
import com.raaveinm.homepharmacy.domain.ManageSharedPreferences;
import com.google.android.material.snackbar.Snackbar;

public class LoginFragment extends Fragment {

    private ImageView loginImageView;
    private static final String TAG = "LoginFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginImageView = view.findViewById(R.id.loginImageView);
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        if (getContext() == null || getView() == null) {
            Log.e(TAG, "Context or View is null in onResume");
            return;
        }

        ManageSharedPreferences loginData = new ManageSharedPreferences(requireContext());
        TextView passwordText = requireView().findViewById(R.id.editTextNumberPasswordLogin);
        TextView loginButton = requireView().findViewById(R.id.submitPassword);
        TextView welcomeText = requireView().findViewById(R.id.welcomeText);

        welcomeText.setText(getString(R.string.welcome) + " " + loginData.getUsername());

        passwordText.setOnFocusChangeListener((v, hasFocus) -> {
                    if (loginButton != null) {
                        loginButton.setEnabled(true);
                    }
                });

                loginButton.setOnClickListener(view -> {
                    String passwordString = passwordText.getText().toString();
                    if (passwordString.isEmpty()) {
                        Snackbar.make(requireView(), "Password cannot be empty", Snackbar.LENGTH_SHORT).show();
                        return;
                    }


                    try {
                        if (loginData.getPassword() == Integer.parseInt(passwordString)) {
                            loginData.loggedIn();
                            Intent intent = new Intent(requireContext(), MainActivity.class);
                            intent.setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                            );
                            startActivity(intent);
                            if (getActivity() != null) {
                                getActivity().finish();
                            }
                        } else {
                            Snackbar.make(requireView(), R.string.wrong_password, Snackbar.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Snackbar.make(requireView(), "Invalid password format", Snackbar.LENGTH_SHORT).show();
                        Log.e(TAG, "Password parsing error", e);
                    }
                });
                new ImageFetching().fetchAndLoadImage( loginImageView );
            }
        }