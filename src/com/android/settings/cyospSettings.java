/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings;

import com.android.internal.logging.MetricsLogger;
import com.android.internal.view.RotationPolicy;
import com.android.settings.DropDownPreference.Callback;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;

//import static android.provider.Settings.System.SCREEN_OFF_TIMEOUT;

import android.app.Activity;
import android.app.ActivityManagerNative;
import android.app.Dialog;
import android.app.UiModeManager;
import android.app.admin.DevicePolicyManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class cyospSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, OnPreferenceClickListener, Indexable {
    private static final String TAG = "cyospSettings";

    /** If there is no setting in the provider, use this. */
    private static final int FALLBACK_SCREEN_OFF_ANIMATION = 2; // Classic Electron Beam

    private static final String KEY_SCREEN_OFF_ANIMATION = "screen_off_animation";

    private static final int DLG_GLOBAL_CHANGE_WARNING = 1;

    private final Configuration mCurConfig = new Configuration();

    private ListPreference mScreenOffAnimationPreference;

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.DISPLAY;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Activity activity = getActivity();
        final ContentResolver resolver = activity.getContentResolver();

        addPreferencesFromResource(R.xml.cyosp_settings);

        // mScreenTimeoutPreference = (ListPreference) findPreference(KEY_SCREEN_TIMEOUT);
        // final long currentTimeout = Settings.System.getLong(resolver, SCREEN_OFF_TIMEOUT,
        //         FALLBACK_SCREEN_TIMEOUT_VALUE);
        // mScreenTimeoutPreference.setValue(String.valueOf(currentTimeout));
        // mScreenTimeoutPreference.setOnPreferenceChangeListener(this);
        // disableUnusableTimeouts(mScreenTimeoutPreference);
        // updateTimeoutPreferenceDescription(currentTimeout);

        // mNightModePreference = (ListPreference) findPreference(KEY_NIGHT_MODE);
        // if (mNightModePreference != null) {
        //     final UiModeManager uiManager = (UiModeManager) getSystemService(
        //             Context.UI_MODE_SERVICE);
        //     final int currentNightMode = uiManager.getNightMode();
        //     mNightModePreference.setValue(String.valueOf(currentNightMode));
        //     mNightModePreference.setOnPreferenceChangeListener(this);
        // }
    }
    //
    // private void updateTimeoutPreferenceDescription(long currentTimeout) {
    //     ListPreference preference = mScreenTimeoutPreference;
    //     String summary;
    //     if (currentTimeout < 0) {
    //         // Unsupported value
    //         summary = "";
    //     } else {
    //         final CharSequence[] entries = preference.getEntries();
    //         final CharSequence[] values = preference.getEntryValues();
    //         if (entries == null || entries.length == 0) {
    //             summary = "";
    //         } else {
    //             int best = 0;
    //             for (int i = 0; i < values.length; i++) {
    //                 long timeout = Long.parseLong(values[i].toString());
    //                 if (currentTimeout >= timeout) {
    //                     best = i;
    //                 }
    //             }
    //             summary = preference.getContext().getString(R.string.screen_timeout_summary,
    //                     entries[best]);
    //         }
    //     }
    //     preference.setSummary(summary);
    // }

    @Override
    public void onResume() {
        super.onResume();
        updateState();
    }

    @Override
    public Dialog onCreateDialog(int dialogId) {
        return null;
    }

    private void updateState() {
    }

    public void writeFontSizePreference(Object objValue) {
        try {
            mCurConfig.fontScale = Float.parseFloat(objValue.toString());
            ActivityManagerNative.getDefault().updatePersistentConfiguration(mCurConfig);
        } catch (RemoteException e) {
            Log.w(TAG, "Unable to save font size");
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final String key = preference.getKey();
        // if (KEY_SCREEN_TIMEOUT.equals(key)) {
        //     try {
        //         int value = Integer.parseInt((String) objValue);
        //         Settings.System.putInt(getContentResolver(), SCREEN_OFF_TIMEOUT, value);
        //         updateTimeoutPreferenceDescription(value);
        //     } catch (NumberFormatException e) {
        //         Log.e(TAG, "could not persist screen timeout setting", e);
        //     }
        // }
        // if (preference == mNightModePreference) {
        //     try {
        //         final int value = Integer.parseInt((String) objValue);
        //         final UiModeManager uiManager = (UiModeManager) getSystemService(
        //                 Context.UI_MODE_SERVICE);
        //         uiManager.setNightMode(value);
        //     } catch (NumberFormatException e) {
        //         Log.e(TAG, "could not persist night mode setting", e);
        //     }
        // }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }

    @Override
    protected int getHelpResource() {
        return R.string.help_uri_display;
    }

    public static final Indexable.SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
                    ArrayList<SearchIndexableResource> result =
                            new ArrayList<SearchIndexableResource>();

                    SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.cyosp_settings;
                    result.add(sir);

                    return result;
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    ArrayList<String> result = new ArrayList<String>();
                    return result;
                }
            };
}
