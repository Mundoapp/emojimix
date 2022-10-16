package com.emojimixer.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    protected BaseActivity activity;

    public void onAttach(Activity _activity) {
        super.onAttach(_activity);
        this.activity = (BaseActivity) _activity;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onDetach() {
        super.onDetach();
    }
}
