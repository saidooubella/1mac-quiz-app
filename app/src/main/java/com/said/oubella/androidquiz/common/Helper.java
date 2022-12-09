package com.said.oubella.androidquiz.common;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public final class Helper {

    public static final long ANIMATION_DURATION = 280L;

    private Helper() {}

    public static void require(boolean condition, String message) {
        if (!condition) throw new IllegalArgumentException(message);
    }

    public static void setActionBarTitle(Fragment fragment, String title) {
        ActionBar toolbar = ((AppCompatActivity) fragment.requireActivity()).getSupportActionBar();
        if (toolbar != null) toolbar.setTitle(title);
    }
}
