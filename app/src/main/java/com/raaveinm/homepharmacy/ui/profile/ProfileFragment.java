package com.raaveinm.homepharmacy.ui.profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.raaveinm.homepharmacy.R;
import com.raaveinm.homepharmacy.databinding.FragmentProfileBinding;
import com.raaveinm.homepharmacy.domain.ManageSharedPreferences;
import com.raaveinm.homepharmacy.ui.Registration;
import com.raaveinm.homepharmacy.ui.webView.WebViewFragment;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private Button changePassword;
    private Button logOut;
    private Button confirmPassword;
    private Button refillStocks;
    private EditText editTextNumberPassOld;
    private EditText editTextNumberPassNew;
    private EditText editTextNumberPassConf;
    private TextView profileName;
    private LinearLayout changePasswordLayout;
    private ManageSharedPreferences loginData;
    private LinearLayout profileContentContainer;
    private FrameLayout webViewFragmentContainer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        profileName = binding.profileName;
        changePassword = binding.changePassword;
        logOut = binding.logOut;
        changePasswordLayout = binding.changePasswordLayout;
        editTextNumberPassOld = binding.editTextNumberPassOld;
        editTextNumberPassNew = binding.editTextNumberPassNew;
        editTextNumberPassConf = binding.editTextNumberPassConf;
        confirmPassword = binding.confirmPassword;
        refillStocks = binding.refillStocks;

        profileContentContainer = binding.profileContentContainer;
        webViewFragmentContainer = binding.webViewFragmentContainer;

        loginData = new ManageSharedPreferences(requireContext());

        getChildFragmentManager().addOnBackStackChangedListener(() -> {
            if (getChildFragmentManager().getBackStackEntryCount() == 0) {
                profileContentContainer.setVisibility(View.VISIBLE);
                webViewFragmentContainer.setVisibility(View.GONE);
            } else {
                profileContentContainer.setVisibility(View.GONE);
                webViewFragmentContainer.setVisibility(View.VISIBLE);
            }
        });

        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            profileContentContainer.setVisibility(View.GONE);
            webViewFragmentContainer.setVisibility(View.VISIBLE);
        } else {
            profileContentContainer.setVisibility(View.VISIBLE);
            webViewFragmentContainer.setVisibility(View.GONE);
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        String username = loginData.getUsername();

        profileName.setText(getString(R.string.about_me) + " " + username);
        changePassword.setOnClickListener(v -> changePassword());
        logOut.setOnClickListener(v -> logout());

        refillStocks.setOnClickListener(v -> {
            profileContentContainer.setVisibility(View.GONE);
            webViewFragmentContainer.setVisibility(View.VISIBLE);

            Fragment webViewFragment = new WebViewFragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.web_view_fragment_container, webViewFragment)
                    .addToBackStack("WebViewInProfile")
                    .commit();
        });
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void changePassword() {
        // Temporarily hide refillStocks button during password change for cleaner UI
        refillStocks.setVisibility(View.GONE);
        changePasswordLayout.setVisibility(View.VISIBLE);
        changePassword.setVisibility(View.GONE);
        logOut.setVisibility(View.GONE);
        confirmPassword.setEnabled(false);

        editTextNumberPassConf.setOnFocusChangeListener(
                (v, hasFocus) -> confirmPassword.setEnabled(true)
        );

        confirmPassword.setOnClickListener(v -> {

            String oldPassStr = editTextNumberPassOld.getText().toString();
            String newPassStr = editTextNumberPassNew.getText().toString();
            String confPassStr = editTextNumberPassConf.getText().toString();

            if (oldPassStr.isEmpty() || newPassStr.isEmpty() || confPassStr.isEmpty()) {
                Snackbar.make(requireView(), "Fill all fields", Snackbar.LENGTH_SHORT).show();
                return;
            }

            int oldPassword = Integer.parseInt(oldPassStr);
            int newPassword = Integer.parseInt(newPassStr);
            int confPassword = Integer.parseInt(confPassStr);


            if (oldPassword == loginData.getPassword()) {
                if (newPassword == confPassword) {
                    loginData.changePassword(newPassword);
                    confirmPassword.setEnabled(true);
                    changePasswordLayout.setVisibility(View.GONE);
                    changePassword.setVisibility(View.VISIBLE);
                    logOut.setVisibility(View.VISIBLE);
                    refillStocks.setVisibility(View.VISIBLE);

                    editTextNumberPassOld.setText("");
                    editTextNumberPassNew.setText("");
                    editTextNumberPassConf.setText("");

                    Snackbar.make(
                            requireView(),
                            R.string.password_changed,
                            Snackbar.LENGTH_SHORT
                    ).show();
                } else {
                    Snackbar.make(
                            requireView(),
                            R.string.password_mismatch,
                            Snackbar.LENGTH_SHORT
                    ).show();
                }
            } else {
                Snackbar.make(
                        requireView(),
                        R.string.wrong_password,
                        Snackbar.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void logout() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.log_out_confirm_title)
                .setMessage(R.string.log_out_confirm_message)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    loginData.loggedOut();
                    loginData.deleteData();
                    Intent intent = new Intent(getContext(), Registration.class);
                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                    );
                    startActivity(intent);
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}