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
 * @desc ��������
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
		// �����Զ������� TODO ������ɺ�ɾ��
		SharedPreferencesUtils.putIsUpdate(getApplicationContext(),
				MyConstant.AUTOUPDATE, true);

		// ��ʼ������
		initview();

		// ��ʼ������
		initDate();

		// ��ʼ������
		initAnimation();

		// ������ʼ�¼�,����ʼ�����ǰ���¼�
		initEvent();

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LODEOLD:
				// ����Ҫ���£�����������
				enterHome();
				break;
			case NEWVERSION:
				// ��Ҫ���£���ʾ�û��Ƿ���µĶԻ���
				showIsUpdate();
				break;
			case 404:
				// ��������ʧ��
				Toast.makeText(getApplicationContext(), "��������ʧ��", 0).show();
				enterHome();
				break;

			default:
				enterHome();
				break;
			}
		};
	};

	private void initEvent() {
		// �����������������ɺ���¼�
		mAs.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// ��������һ��ʼ
				// ��ʼ�����磬���汾����
				// ��� �Ƿ�����µİ�ť״̬
				if (SharedPreferencesUtils.getIsUpdate(getApplicationContext(),
						MyConstant.AUTOUPDATE, false)) {
					// ������
					checkVersion();
				} else {
					// �������£�Ҫֱ�ӽ��������棬����ʲô��������������������֮����
				}
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// ��������һ����
				// ����汾�и��£�ѯ���û��Ƿ���и���
				if (SharedPreferencesUtils.getIsUpdate(getApplicationContext(),
						MyConstant.AUTOUPDATE, false)) {
					// �����£�����ʲô���������Ѿ��ɶ�����ʼȥ����
				} else {
					// �������£�����������
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
		// ��ʾ�û��Ƿ����ظ���
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		// ���öԻ�������
		ab.setTitle("������ʾ");
		ab.setMessage("�Ƿ����ظ��£�������" + mversioninfo.desc);
		ab.setPositiveButton("ȷ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ���أ���װ
				downloadNewVersion(mversioninfo);
			}
		}).setNegativeButton("ȡ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ȡ�����أ�����������
				enterHome();
			}
		});
		
		ab.show();
	}

	protected void downloadNewVersion(VersionInfo vinfo) {
		// ���ظ���
		// ����,ʹ�ÿ�Դ���
		HttpUtils httpUtils = new HttpUtils();
		// ָ�����������ļ���Ŀ¼
		File apkdir = Environment.getExternalStorageDirectory();
		// �ж��Ƿ��оɰ汾��APK
		File apkfFile = new File(apkdir, "new.apk");
		if (apkfFile.length() > 0) {
			apkfFile.delete();
		}
		// ��ʼ����
		httpUtils.download(vinfo.downloadurl, apkdir.getAbsolutePath()
				+ "/new.apk", new RequestCallBack<File>() {

			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				// ���سɹ�
				// ��װ
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");

				// ָ����װ�ļ�
				File file = new File(Environment.getExternalStorageDirectory(),
						"new.apk");

				// ������ͼ����
				intent.setDataAndType(Uri.fromFile(file),
						"application/vnd.android.package-archive");
				//
				startActivityForResult(intent, 1);
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// ����ʧ��
				// ����������
				enterHome();

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// �û�ѡ��װ�򲻰�װ
		enterHome();
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void checkVersion() {
		// ���汾����
		// ��Ҫ�õ����磬�����߳�ȥ��
		new Thread() {
			public void run() {
				checkUrlVersion();
			};
		}.start();
	}

	protected void checkUrlVersion() {
		// ����������Դ�еİ汾��Ϣ�뱾��App�汾��Ϣ�ȶԣ�����и�����ʾ�û�����
		// �������� url: http://188.188.4.39:8080/versionConfig.json
		// Url��Ҫ�����������ļ���
		//
		Message message = handler.obtainMessage();
		long startconn = System.currentTimeMillis();
		
		try {
			URL url = new URL(getResources()
					.getString(R.string.checkversionurl));
			// ��ȡ����
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			// ��ȡ�������ӷ�����
			int code = conn.getResponseCode();
			// �ж���������
			if (code == 200) {
				// ���ӳɹ�
				// �������ݽ�����String���͵�Json����
				String json = StreamToJson(conn.getInputStream());
				mversioninfo = parseJson(json);

				// �õ������еİ汾��
				// У��
				if (mVersioncode == mversioninfo.versioncode) {
					// codeһ�£�����Ҫ����
					message.what = LODEOLD;
				} else {
					// ��Ҫ����
					// ����
					// ��װ
					message.what = NEWVERSION;
				}
			} else {
				// ��������ʧ��
				// ����״̬��
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
				// ˵������û���꣬�����Ѿ����
				SystemClock.sleep(3000 - (endconn - startconn));
			}
			handler.sendMessage(message);
		}

	}

	private VersionInfo parseJson(String json) throws JSONException {
		// ����Json���ݣ���װ��VersionInfo
		VersionInfo vinfo = new VersionInfo();
		JSONObject jsonObject = new JSONObject(json);

		vinfo.versionname = jsonObject.getString("versionname");
		vinfo.versioncode = jsonObject.getInt("versioncode");
		vinfo.desc = jsonObject.getString("desc");
		vinfo.downloadurl = jsonObject.getString("downloadurl");
		return vinfo;
	}

	/**
	 * ��װVersion��Json���ݳ�VersionInfo
	 */
	private class VersionInfo {
		String versionname;
		int versioncode;
		String desc;
		String downloadurl;
	}

	private String StreamToJson(InputStream inputStream) throws IOException {
		// ƴ��json����
		StringBuilder sjson = new StringBuilder();
		// ����������
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

		// �رս�������
		finish();
	}

	/**
	 * ��ʼ��������ʹ�����������ֶ�̬ �����ڶ��������� ����������ʼ�������������û�����
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

		// ���ö���������һ�ξͽ���
		aa.setFillAfter(true);

		mAs = new AnimationSet(true);
		mAs.addAnimation(aa);
		mAs.addAnimation(sa);
		mAs.addAnimation(ra);

		mAs.setDuration(3000);

		// ���ý������沥��
		splash_root.startAnimation(mAs);

	}

	/**
	 * ��ʼ��������������ݰ����� �汾�� �汾�� �������嵥�ļ���ͨ��API��ȡ
	 */
	private void initDate() {

		// ��ȡ��������
		PackageManager mpm = getPackageManager();

		try {
			// ��ȡ����Ϣ��װ��
			PackageInfo packageInfo = mpm.getPackageInfo(getPackageName(), 0);

			// ��ʼ���汾��
			String mVersionname = packageInfo.versionName;
			splash_tv_versionname.setText(mVersionname);

			mVersioncode = packageInfo.versionCode;
			splash_tv_versioncode.setText(mVersioncode + "");

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void initview() {
		// ��ʾ��������
		setContentView(R.layout.activity_splash);

		splash_root = (RelativeLayout) findViewById(R.id.splash_root);

		splash_tv_versionname = (TextView) findViewById(R.id.splash_tv_versionname);

		splash_tv_versioncode = (TextView) findViewById(R.id.splash_tv_versioncode);
	}
}
