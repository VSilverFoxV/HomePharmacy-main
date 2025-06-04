package com.raaveinm.homepharmacy.ui.webView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.raaveinm.homepharmacy.databinding.FragmentWebViewBinding;

public class WebViewFragment extends Fragment {
    private FragmentWebViewBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentWebViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        binding.webView.setWebViewClient(new WebViewClient());
        binding.webView.loadUrl("https://www.eapteka.ru");
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.webView.onResume();
        binding.webView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.webView.onPause();
        binding.webView.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        ViewGroup parent = (ViewGroup) binding.webView.getParent();
        if (parent != null) {
            parent.removeView(binding.webView);
        }
        binding.webView.destroy();
        super.onDestroyView();
        binding = null;
    }
}