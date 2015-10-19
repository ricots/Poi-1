package com.github.kaninohon.poi.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.kaninohon.poi.R;
import com.github.kaninohon.poi.ui.widget.CircleTransform;
import com.github.kaninohon.poi.utils.SharedPreferManager;
import com.squareup.picasso.Picasso;


public class AboutFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    private Preference mVersionPref;
    private Preference mMailPref;
    //private Preference mGithubPref;
    private Preference mReferencePref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.abouts);

        mVersionPref = findPreference("version");
        mMailPref = findPreference("mail");
        //mGithubPref = findPreference("github");
        mReferencePref = findPreference("reference");

        //mGithubPref.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(android.preference.Preference pref) {
//        if (pref == mGithubPref) {
//            openWebUrl(getString(R.string.project_github_url));
//            return true;
//        }
        return false;
    }

    private void openWebUrl(String url) {
        Uri uri = Uri.parse(url);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}
