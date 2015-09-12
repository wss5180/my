package com.wss.safetyguard.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 此工具类用于设置一些boolean数据，比如是否检查更新
 * 
 * @author wss-pc
 * 
 */
public class SharedPreferencesUtils {

	public static boolean getIsUpdate(Context context, String key, boolean value) {
		SharedPreferences sp = context.getSharedPreferences(MyConstant.AUTOUPDATE, Context.MODE_PRIVATE);
		
		return sp.getBoolean(MyConstant.AUTOUPDATE, false);
	}

	public static void putIsUpdate(Context context,
			String key, boolean value) {
		// 设置是否自动更新
		SharedPreferences sp = context.getSharedPreferences(
				MyConstant.AUTOUPDATE, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();

	}

}
