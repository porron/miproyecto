package com.example.tabulado;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class preferencias extends PreferenceActivity  implements OnSharedPreferenceChangeListener {

    public static final String NOMBRE_SERVIDOR = "";
    public static final String PASS_SERVIDOR = "";
    public static final String DIR_ESPEJO = "";

	
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.preferencias);
	SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

    Preference preferen = findPreference("NOMBRE_SERVIDOR");
    preferen.setSummary(sharedPref.getString("NOMBRE_SERVIDOR", ""));
    preferen = findPreference("PASS_SERVIDOR");
    preferen.setSummary(sharedPref.getString("PASS_SERVIDOR", ""));
    preferen = findPreference("DIR_ESPEJO");
    preferen.setSummary(sharedPref.getString("DIR_ESPEJO", ""));
}


@Override

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
     
               Preference connectionPref = findPreference(key);
                // Set summary to be the user-description for the selected value
               connectionPref.setSummary(sharedPreferences.getString(key, ""));
            
        }

@Override
protected void onResume() {
    super.onResume();
    getPreferenceScreen().getSharedPreferences()
            .registerOnSharedPreferenceChangeListener(this);
}

@Override
protected void onPause() {
    super.onPause();
    getPreferenceScreen().getSharedPreferences()
            .unregisterOnSharedPreferenceChangeListener(this);
}
	
}

//public class preferencias extends PreferenceFragment {
//@Override
//
//    public void onCreate(Bundle savedInstanceState) {
//       super.onCreate(savedInstanceState);
//        // Load the preferences from an XML resource
//        addPreferencesFromResource(R.xml.preferencias);
//   }
    
//   @Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//    	super.onCreateView(inflater, container, savedInstanceState);  
//		View rootView = inflater.inflate(R.layout.vacio,
//				container, false);
//		return rootView;	
//}
