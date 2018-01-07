package com.aderayanbima31.masigit;

import android.os.Bundle;
import android.preference.PreferenceActivity;
/**
 * Created by Basil.
 */
public class Prefs extends PreferenceActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
    }
}
