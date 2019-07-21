package cn.blinkdagger.android.qrcode.camera;

import android.content.SharedPreferences;

import cn.blinkdagger.android.qrcode.constants.PreferenceKey;


/**
 * Enumerates settings of the PreferenceKey controlling the front light.
 */
public enum FrontLightMode {

  /** Always on. */
  ON,
  /** On only when ambient light is low. */
  AUTO,
  /** Always off. */
  OFF;

  private static FrontLightMode parse(String modeString) {
    return modeString == null ? OFF : valueOf(modeString);
  }

  public static FrontLightMode readPref(SharedPreferences sharedPrefs) {
    return parse(sharedPrefs.getString(PreferenceKey.KEY_FRONT_LIGHT_MODE, OFF.toString()));
  }

}
