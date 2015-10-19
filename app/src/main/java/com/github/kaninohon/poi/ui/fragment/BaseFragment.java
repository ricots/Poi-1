package com.github.kaninohon.poi.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.kaninohon.poi.R;

public abstract class BaseFragment extends Fragment{
    private View mRootView;

    abstract protected int provideContentViewId();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(provideContentViewId(), container, false);

        return mRootView;
    }

    public <T extends View> T $(int id) {
        return (T) mRootView.findViewById(id);
    }
}
