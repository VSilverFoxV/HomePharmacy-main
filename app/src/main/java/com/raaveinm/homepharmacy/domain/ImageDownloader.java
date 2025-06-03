package com.raaveinm.homepharmacy.domain;

import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.raaveinm.homepharmacy.R;

public class ImageDownloader {

    public static void loadImage(ImageView imageView, String url) {
        if (imageView != null && url != null && !url.isEmpty()) {
            Glide.with(imageView.getContext())
                    .load(url)
                    .error(R.drawable.ic_dashboard_black_24dp)
                    .into(imageView);
        }
    }
}