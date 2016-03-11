package com.example.myawesomelibrary;

import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackFactory {
    public void tasty(View view) {
        Snackbar.make(view, "Home is where the heart is", Snackbar.LENGTH_LONG)
                .setAction("Let's go", null).show();
    }
}
