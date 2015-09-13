package com.wss.safetyguard.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * �˹�������������һЩboolean���ݣ������Ƿ������
 * 
 * @author wss-pc
 * 
 */
public class SharedPreferencesUtils {

	public static boolean getIsUpdate(Context context, String key,
			boolean defvalue) {
		SharedPreferences sp = context.getSharedPreferences(
				MyConstant.AUTOUPDATE, Context.MODE_PRIVATE);

		return sp.getBoolean(key, defvalue);
	}

	public static void putIsUpdate(Context context, String key, boolean value) {
		// �����Ƿ��Զ�����
		SharedPreferences sp = context.getSharedPreferences(
				MyConstant.AUTOUPDATE, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();

	}

	public static String getString(Context context, String key, String defvalue) {
		// ��ȡ����
		SharedPreferences sp = context.getSharedPreferences(
				MyConstant.PASSWORD, Context.MODE_PRIVATE);
		return sp.getString(key, "");

	}

	public static void putString(Context context, String key, String value) {
		// ����sp
		SharedPreferences sp = context.getSharedPreferences(
				MyConstant.PASSWORD, Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}

	public static boolean getBoolean(Context context,
			String key, boolean defvalue) {
		SharedPreferences sp = context.getSharedPreferences(
				MyConstant.AUTOUPDATE, Context.MODE_PRIVATE);

		return sp.getBoolean(key, defvalue);
	}

}
