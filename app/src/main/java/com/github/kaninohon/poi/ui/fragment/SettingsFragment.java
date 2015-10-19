package com.github.kaninohon.poi.ui.fragment;

import android.preference.CheckBoxPreference;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import com.github.kaninohon.poi.R;
import com.github.kaninohon.poi.utils.FileUtils;
import com.github.kaninohon.poi.utils.SharedPreferManager;

public class SettingsFragment extends PreferenceFragment {
    private CheckBoxPreference mifNoImage;
    private Preference mCache;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        // 清除缓存
        mCache = findPreference("cache");
        mCache.setSummary(FileUtils.getFileSize(FileUtils.getCacheSize(getActivity())));
        mCache.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("提示")
                        .setMessage("确定清除缓存？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FileUtils.clearAppCache(getActivity());
                                mCache.setSummary("0KB");
                            }
                        })
                        .setNegativeButton("取消", null).show();
                return true;
            }
        });
        // 加载图片
        mifNoImage = (CheckBoxPreference)findPreference("if_no_image");
        mifNoImage.setChecked(SharedPreferManager.getInstance().is3gNoImage());
        mifNoImage.setSummary("2G/3G网络下不显示图片");
        mifNoImage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferManager.getInstance().setIf3gNoImage(mifNoImage.isChecked());
                return true;
            }
        });
    }

}
