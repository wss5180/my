package com.wss.safetyguard.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.wss.safetyguard.utils.MyConstant;
import com.wss.safetyguard.utils.SharedPreferencesUtils;

/**
 * 
 * @author wss-pc
 * @desc 进场界面
 */
public class Splash extends Activity {
	private static final int LODEOLD = 1;
	private static final int NEWVERSION = 2;
	private TextView splash_tv_versionname;
	private TextView splash_tv_versioncode;
	private RelativeLayout splash_root;
	private AlphaAnimation aa;
	private AnimationSet mAs;
	private int mVersioncode;
	private VersionInfo mversioninfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置自动检测更新 TODO 代码完成后删除
		SharedPreferencesUtils.putIsUpdate(getApplicationContext(),
				MyConstant.AUTOUPDATE, true);

		// 初始化界面
		initview();

		// 初始化数据
		initDate();

		// 初始化动画
		initAnimation();

		// 动画初始事件,即初始化完成前后事件
		initEvent();

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LODEOLD:
				// 不需要更新，进入主界面
				enterHome();
				break;
			case NEWVERSION:
				// 需要更新，提示用户是否更新的对话框
				showIsUpdate();
				break;
			case 404:
				// 网络连接失败
				Toast.makeText(getApplicationContext(), "网络连接失败", 0).show();
				enterHome();
				break;

			default:
				enterHome();
				break;
			}
		};
	};

	private void initEvent() {
		// 这里监听动画播放完成后的事件
		mAs.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// 动画播放一开始
				// 初始化网络，检查版本更新
				// 检查 是否检查更新的按钮状态
				if (SharedPreferencesUtils.getIsUpdate(getApplicationContext(),
						MyConstant.AUTOUPDATE, false)) {
					// 检查更新
					checkVersion();
				} else {
					// 不检查更新，要直接进入主界面，但是什么都不做，留给动画结束之后做
				}
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// 动画播放一结束
				// 如果版本有更新，询问用户是否进行更新
				if (SharedPreferencesUtils.getIsUpdate(getApplicationContext(),
						MyConstant.AUTOUPDATE, false)) {
					// 检查更新，但是什么都不做，已经由动画开始去做了
				} else {
					// 不检查更新，进入主界面
					enterHome();
				}

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

		});
	}

	protected void showIsUpdate() {
		// 提示用户是否下载更新
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		// 设置对话框属性
		ab.setTitle("更新提示");
		ab.setMessage("是否下载更新？新增：" + mversioninfo.desc);
		ab.setPositiveButton("确认", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 下载，安装
				downloadNewVersion(mversioninfo);
			}
		}).setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 取消下载，进入主界面
				enterHome();
			}
		});
		
		ab.show();
	}

	protected void downloadNewVersion(VersionInfo vinfo) {
		// 下载更新
		// 下载,使用开源框架
		HttpUtils httpUtils = new HttpUtils();
		// 指定保存下载文件的目录
		File apkdir = Environment.getExternalStorageDirectory();
		// 判断是否有旧版本的APK
		File apkfFile = new File(apkdir, "new.apk");
		if (apkfFile.length() > 0) {
			apkfFile.delete();
		}
		// 开始下载
		httpUtils.download(vinfo.downloadurl, apkdir.getAbsolutePath()
				+ "/new.apk", new RequestCallBack<File>() {

			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				// 下载成功
				// 安装
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");

				// 指定安装文件
				File file = new File(Environment.getExternalStorageDirectory(),
						"new.apk");

				// 设置意图数据
				intent.setDataAndType(Uri.fromFile(file),
						"application/vnd.android.package-archive");
				//
				startActivityForResult(intent, 1);
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// 下载失败
				// 进入主界面
				enterHome();

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 用户选择安装或不安装
		enterHome();
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void checkVersion() {
		// 检查版本更新
		// 需要用到网络，开启线程去做
		new Thread() {
			public void run() {
				checkUrlVersion();
			};
		}.start();
	}

	protected void checkUrlVersion() {
		// 解析网络资源中的版本信息与本地App版本信息比对，如果有更新提示用户更新
		// 连接网络 url: http://188.188.4.39:8080/versionConfig.json
		// Url需要设置在配置文件中
		//
		Message message = handler.obtainMessage();
		long startconn = System.currentTimeMillis();
		
		try {
			URL url = new URL(getResources()
					.getString(R.string.checkversionurl));
			// 获取连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			// 获取网络连接返回码
			int code = conn.getResponseCode();
			// 判断网络连接
			if (code == 200) {
				// 连接成功
				// 将流数据解析成String类型的Json数据
				String json = StreamToJson(conn.getInputStream());
				mversioninfo = parseJson(json);

				// 得到网络中的版本号
				// 校对
				if (mVersioncode == mversioninfo.versioncode) {
					// code一致，不需要更新
					message.what = LODEOLD;
				} else {
					// 需要更新
					// 下载
					// 安装
					message.what = NEWVERSION;
				}
			} else {
				// 网络连接失败
				// 返回状态码
				message.what = code;
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			message.what = 10070;
			e.printStackTrace();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			message.what = 10071;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			message.what = 10072;
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			message.what = 10073;
			e.printStackTrace();
		} finally {
			long endconn = System.currentTimeMillis();
			if (endconn - startconn < 3000) {
				// 说明动画没播完，数据已经完毕
				SystemClock.sleep(3000 - (endconn - startconn));
			}
			handler.sendMessage(message);
		}

	}

	private VersionInfo parseJson(String json) throws JSONException {
		// 解析Json数据，封装到VersionInfo
		VersionInfo vinfo = new VersionInfo();
		JSONObject jsonObject = new JSONObject(json);

		vinfo.versionname = jsonObject.getString("versionname");
		vinfo.versioncode = jsonObject.getInt("versioncode");
		vinfo.desc = jsonObject.getString("desc");
		vinfo.downloadurl = jsonObject.getString("downloadurl");
		return vinfo;
	}

	/**
	 * 封装Version的Json数据成VersionInfo
	 */
	private class VersionInfo {
		String versionname;
		int versioncode;
		String desc;
		String downloadurl;
	}

	private String StreamToJson(InputStream inputStream) throws IOException {
		// 拼接json数据
		StringBuilder sjson = new StringBuilder();
		// 解析流数据
		BufferedReader br = new BufferedReader(new InputStreamReader(
				inputStream));
		String line = "";
		while ((line = br.readLine()) != null) {
			sjson.append(line);
		}
		return sjson + "";
	}

	private void enterHome() {
		Intent intent = new Intent(Splash.this, HomeActivity.class);
		startActivity(intent);

		// 关闭进场界面
		finish();
	}

	/**
	 * 初始化动画是使进场界面显现动态 并且在动画进行中 进行其他初始化操作，增加用户体验
	 */
	private void initAnimation() {
		RotateAnimation ra = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setFillAfter(true);

		ScaleAnimation sa = new ScaleAnimation(0f, 1f, 0f, 1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);

		AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);

		// 设置动画播放完一次就结束
		aa.setFillAfter(true);

		mAs = new AnimationSet(true);
		mAs.addAnimation(aa);
		mAs.addAnimation(sa);
		mAs.addAnimation(ra);

		mAs.setDuration(3000);

		// 设置进场画面播放
		splash_root.startAnimation(mAs);

	}

	/**
	 * 初始化进场界面的数据包括： 版本名 版本号 都是在清单文件中通过API获取
	 */
	private void initDate() {

		// 获取包管理者
		PackageManager mpm = getPackageManager();

		try {
			// 获取包信息封装类
			PackageInfo packageInfo = mpm.getPackageInfo(getPackageName(), 0);

			// 初始化版本名
			String mVersionname = packageInfo.versionName;
			splash_tv_versionname.setText(mVersionname);

			mVersioncode = packageInfo.versionCode;
			splash_tv_versioncode.setText(mVersioncode + "");

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void initview() {
		// 显示进场界面
		setContentView(R.layout.activity_splash);

		splash_root = (RelativeLayout) findViewById(R.id.splash_root);

		splash_tv_versionname = (TextView) findViewById(R.id.splash_tv_versionname);

		splash_tv_versioncode = (TextView) findViewById(R.id.splash_tv_versioncode);
	}
}
