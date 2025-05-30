package com.raaveinm.homepharmacy.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.raaveinm.homepharmacy.MainActivity;
import com.raaveinm.homepharmacy.R;
import com.raaveinm.homepharmacy.domain.ManageSharedPreferences;

public class RegistrationFragment extends Fragment {
    private boolean success;

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
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        success = false;

        ManageSharedPreferences loginData = new ManageSharedPreferences(requireContext());

        TextView loginText = requireView().findViewById(R.id.editLoginText);
        TextView passwordText = requireView().findViewById(R.id.editTextNumberPassword);
        TextView confirmText = requireView().findViewById(R.id.editTextConfirmPassword);
        TextView letsGo = requireView().findViewById(R.id.startButton);

        letsGo.setOnClickListener(view -> {
            int passwordValue = Integer.parseInt(passwordText.getText().toString());

            if ((passwordText.getText().toString()).equals(confirmText.getText().toString()) &&
                    (passwordValue >= 1000 && passwordValue < 10000)) {

                loginData.setUsername(loginText.getText().toString());
                loginData.changePassword(Integer.parseInt(passwordText.getText().toString()));
                success = true;

            } else {
                Snackbar.make(requireView(), R.string.password_mismatch, Snackbar.LENGTH_SHORT).show();
                success = false;
            }

            if (success) {
                loginData.loggedIn();
                Intent intent = new Intent(requireContext(), MainActivity.class);

                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
                );

                startActivity(intent);
            }
        });

        loginText.setOnFocusChangeListener((v, hasFocus) -> {
            letsGo.setEnabled(!loginText.getText().toString().isEmpty() &&
                    !passwordText.getText().toString().isEmpty() ||
                    !confirmText.getText().toString().isEmpty());
        });
        confirmText.setOnFocusChangeListener((v, hasFocus) -> {
            letsGo.setEnabled(!loginText.getText().toString().isEmpty() &&
                    !passwordText.getText().toString().isEmpty() ||
                    !confirmText.getText().toString().isEmpty());
        });
    }
}