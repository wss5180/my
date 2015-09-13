package com.wss.safetyguard.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.animation.ObjectAnimator;
import com.wss.safetyguard.utils.Md5utils;
import com.wss.safetyguard.utils.MyConstant;
import com.wss.safetyguard.utils.SharedPreferencesUtils;

/**
 * @author wss-pc
 * 
 */
public class HomeActivity extends Activity {
	private ImageView home_iv_setting;
	private GridView home_gv_list;
	private ImageView home_iv_logo;
	private AlertDialog mPwd;

	private static final String[] names = new String[] { "手机防盗", "通讯卫士",
			"软件管家", "进程管理", "流量统计", "病毒查杀", "缓存清理", "高级工具" };
	private static final String[] descs = new String[] { "手机丢失好找", "防骚扰防监听",
			"方便管理软件", "保持手机通畅", "注意流量超标", "手机安全保障", "手机快步如飞", "特性处理更好" };
	private static final int[] icons = new int[] { R.drawable.sjfd,
			R.drawable.srlj, R.drawable.rjgj, R.drawable.jcgl, R.drawable.lltj,
			R.drawable.sjsd, R.drawable.hcql, R.drawable.szzx };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 初始化界面
		initView();

		// 初始化数据
		initData();

		// 播放动画
		startAnimation();

		// 事件
		initEvent();
	}

	/**
	 * 监听 Gridview条目的点击 设置按钮的点击
	 */
	private void initEvent() {
		// 事件监听
		// 定义监听事件
		home_gv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 对每个条目进行监听
				switch (position) {
				case 0:
					// 点击的是第一个条目--手机防盗
					// 检查sharepreference里数据，检测用户是否设置密码
					String password = SharedPreferencesUtils.getString(
							getApplicationContext(), MyConstant.PASSWORD, "");

					// 判断
					if (TextUtils.isEmpty(password)) {
						// 没设置密码，弹出设置密码框
						showSetPasswordDialog();
					} else {
						// 已经设置，弹出输入密码框
						showEnterPasswordDialog();
					}
					break;

				default:
					break;
				}

			}
		});
	}

	protected void showEnterPasswordDialog() {
		// 设置输入密码对话框
		showPasswordDialog(1);
	}

	protected void showSetPasswordDialog() {
		// 设置密码对话框
		showPasswordDialog(0);
	}

	private void showPasswordDialog(final int i) {
		// 在这里主线显示设置密码的对话框
		// 因为输入密码对话框只比设置密码对话框少一个EditText，所以判断，如果是输入对话框
		// 就隐藏一个Edittext

		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		// 将自定义的视图对话框转换成 View
		View v = View.inflate(getApplicationContext(),
				R.layout.dailog_password, null);

		// 获取子控件
		final TextView tv_password_dialog = (TextView) v
				.findViewById(R.id.tv_password_dialog);
		final EditText password_dialog_et1 = (EditText) v
				.findViewById(R.id.password_dialog_et1);
		final EditText password_dialog_et2 = (EditText) v
				.findViewById(R.id.password_dialog_et2);

		final Button btn_confirm_dialog = (Button) v
				.findViewById(R.id.btn_confirm_dialog);
		final Button btn_cancel_dialog = (Button) v
				.findViewById(R.id.btn_cancel_dialog);

		// 判断当前场景所需要的是设置密码还是输入密码
		if (i == 1) {
			// 需要输入密码对话框，隐藏password_dialog_et2
			// 设置Gone使其隐藏并且不占View位置，视为移除
			password_dialog_et2.setVisibility(View.GONE);
			tv_password_dialog.setText("输入密码");
		}
		// 设置按钮监听，监听用户点击了确认还是取消，并采取响应动作
		View.OnClickListener btn_listener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 确认
				// 获取用户输入值
				String password1 = password_dialog_et1.getText().toString()
						.trim();
				String password2 = password_dialog_et2.getText().toString()
						.trim();
				// 判断
				if (v == btn_confirm_dialog) {

					if (i == 0) {
						// 设置密码
						// 判断空
						if (TextUtils.isEmpty(password1)
								|| TextUtils.isEmpty(password2)) {
							Toast.makeText(getApplicationContext(), "密码不能为空", 0)
									.show();
							return;
						}
						// 判断两次密码是否一致
						if (password1.equals(password2)) {
							// 一致
							SharedPreferencesUtils.putString(
									getApplicationContext(),
									MyConstant.PASSWORD, Md5utils.enceypt(password1));
							Toast.makeText(getApplicationContext(), "密码设置成功", 0)
									.show();
						} else {
							// 不一致
							Toast.makeText(getApplicationContext(), "两次密码不一致",
									0).show();
							return;
						}
						mPwd.dismiss();
					} else {
						// 输入密码
						//校对密码
						if (SharedPreferencesUtils.getString(getApplicationContext(), MyConstant.PASSWORD, "").equals(Md5utils.enceypt(password1))) {
							//密码正确
							//跳转安全设置界面
							Intent intent = new Intent(HomeActivity.this,SecuritySetupActivity1.class);
							startActivity(intent);
						}else {
							//密码错误
							Toast.makeText(getApplicationContext(), "密码错误", 0).show();
							return;
						}
					}
					mPwd.dismiss();
				} else {
					// 点击取消
					mPwd.dismiss();
				}

			}
		};

		btn_confirm_dialog.setOnClickListener(btn_listener);
		btn_cancel_dialog.setOnClickListener(btn_listener);

		ab.setView(v);
		mPwd = ab.create();
		mPwd.show();
	}

	/**
	 * 动画效果 logo翻转效果
	 */
	private void startAnimation() {

		// 属性动画实现翻转效果
		ObjectAnimator oa = ObjectAnimator.ofFloat(home_iv_logo, "rotationY",
				0, 30, 90, 150, 180, 270, 330, 360);

		// 设置属性
		oa.setDuration(4000);
		oa.setRepeatCount(ObjectAnimator.INFINITE);
		oa.start();
	}

	/**
	 * 初始化GridView 将数组适配到Gridview
	 */
	private void initData() {
		home_gv_list.setAdapter(new BaseAdapter() {

			@Override
			public int getCount() {
				// 返回数组长度
				return names.length;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// 将数组数据匹配到指定xml,转换成view视图返回
				View v = View.inflate(getApplicationContext(),
						R.layout.home_item_gv, null);

				ImageView home_item_iv = (ImageView) v
						.findViewById(R.id.home_item_iv);
				TextView home_item_title = (TextView) v
						.findViewById(R.id.home_item_title);
				TextView home_item_desc = (TextView) v
						.findViewById(R.id.home_item_desc);
				// 设置条目数据
				home_item_iv.setImageResource(icons[position]);
				home_item_title.setText(names[position]);
				home_item_desc.setText(descs[position]);

				return v;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}
		});

	}

	private void initView() {
		// 显示界面
		setContentView(R.layout.activity_home);
		// 获取控件
		home_iv_setting = (ImageView) findViewById(R.id.home_iv_setting);
		home_gv_list = (GridView) findViewById(R.id.home_gv_list);

		home_iv_logo = (ImageView) findViewById(R.id.home_iv_logo);
	}
}
