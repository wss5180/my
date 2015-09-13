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

	private static final String[] names = new String[] { "�ֻ�����", "ͨѶ��ʿ",
			"����ܼ�", "���̹���", "����ͳ��", "������ɱ", "��������", "�߼�����" };
	private static final String[] descs = new String[] { "�ֻ���ʧ����", "��ɧ�ŷ�����",
			"����������", "�����ֻ�ͨ��", "ע����������", "�ֻ���ȫ����", "�ֻ��첽���", "���Դ������" };
	private static final int[] icons = new int[] { R.drawable.sjfd,
			R.drawable.srlj, R.drawable.rjgj, R.drawable.jcgl, R.drawable.lltj,
			R.drawable.sjsd, R.drawable.hcql, R.drawable.szzx };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ��ʼ������
		initView();

		// ��ʼ������
		initData();

		// ���Ŷ���
		startAnimation();

		// �¼�
		initEvent();
	}

	/**
	 * ���� Gridview��Ŀ�ĵ�� ���ð�ť�ĵ��
	 */
	private void initEvent() {
		// �¼�����
		// ��������¼�
		home_gv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ��ÿ����Ŀ���м���
				switch (position) {
				case 0:
					// ������ǵ�һ����Ŀ--�ֻ�����
					// ���sharepreference�����ݣ�����û��Ƿ���������
					String password = SharedPreferencesUtils.getString(
							getApplicationContext(), MyConstant.PASSWORD, "");

					// �ж�
					if (TextUtils.isEmpty(password)) {
						// û�������룬�������������
						showSetPasswordDialog();
					} else {
						// �Ѿ����ã��������������
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
		// ������������Ի���
		showPasswordDialog(1);
	}

	protected void showSetPasswordDialog() {
		// ��������Ի���
		showPasswordDialog(0);
	}

	private void showPasswordDialog(final int i) {
		// ������������ʾ��������ĶԻ���
		// ��Ϊ��������Ի���ֻ����������Ի�����һ��EditText�������жϣ����������Ի���
		// ������һ��Edittext

		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		// ���Զ������ͼ�Ի���ת���� View
		View v = View.inflate(getApplicationContext(),
				R.layout.dailog_password, null);

		// ��ȡ�ӿؼ�
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

		// �жϵ�ǰ��������Ҫ�����������뻹����������
		if (i == 1) {
			// ��Ҫ��������Ի�������password_dialog_et2
			// ����Goneʹ�����ز��Ҳ�ռViewλ�ã���Ϊ�Ƴ�
			password_dialog_et2.setVisibility(View.GONE);
			tv_password_dialog.setText("��������");
		}
		// ���ð�ť�����������û������ȷ�ϻ���ȡ��������ȡ��Ӧ����
		View.OnClickListener btn_listener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// ȷ��
				// ��ȡ�û�����ֵ
				String password1 = password_dialog_et1.getText().toString()
						.trim();
				String password2 = password_dialog_et2.getText().toString()
						.trim();
				// �ж�
				if (v == btn_confirm_dialog) {

					if (i == 0) {
						// ��������
						// �жϿ�
						if (TextUtils.isEmpty(password1)
								|| TextUtils.isEmpty(password2)) {
							Toast.makeText(getApplicationContext(), "���벻��Ϊ��", 0)
									.show();
							return;
						}
						// �ж����������Ƿ�һ��
						if (password1.equals(password2)) {
							// һ��
							SharedPreferencesUtils.putString(
									getApplicationContext(),
									MyConstant.PASSWORD, Md5utils.enceypt(password1));
							Toast.makeText(getApplicationContext(), "�������óɹ�", 0)
									.show();
						} else {
							// ��һ��
							Toast.makeText(getApplicationContext(), "�������벻һ��",
									0).show();
							return;
						}
						mPwd.dismiss();
					} else {
						// ��������
						//У������
						if (SharedPreferencesUtils.getString(getApplicationContext(), MyConstant.PASSWORD, "").equals(Md5utils.enceypt(password1))) {
							//������ȷ
							//��ת��ȫ���ý���
							Intent intent = new Intent(HomeActivity.this,SecuritySetupActivity1.class);
							startActivity(intent);
						}else {
							//�������
							Toast.makeText(getApplicationContext(), "�������", 0).show();
							return;
						}
					}
					mPwd.dismiss();
				} else {
					// ���ȡ��
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
	 * ����Ч�� logo��תЧ��
	 */
	private void startAnimation() {

		// ���Զ���ʵ�ַ�תЧ��
		ObjectAnimator oa = ObjectAnimator.ofFloat(home_iv_logo, "rotationY",
				0, 30, 90, 150, 180, 270, 330, 360);

		// ��������
		oa.setDuration(4000);
		oa.setRepeatCount(ObjectAnimator.INFINITE);
		oa.start();
	}

	/**
	 * ��ʼ��GridView ���������䵽Gridview
	 */
	private void initData() {
		home_gv_list.setAdapter(new BaseAdapter() {

			@Override
			public int getCount() {
				// �������鳤��
				return names.length;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// ����������ƥ�䵽ָ��xml,ת����view��ͼ����
				View v = View.inflate(getApplicationContext(),
						R.layout.home_item_gv, null);

				ImageView home_item_iv = (ImageView) v
						.findViewById(R.id.home_item_iv);
				TextView home_item_title = (TextView) v
						.findViewById(R.id.home_item_title);
				TextView home_item_desc = (TextView) v
						.findViewById(R.id.home_item_desc);
				// ������Ŀ����
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
		// ��ʾ����
		setContentView(R.layout.activity_home);
		// ��ȡ�ؼ�
		home_iv_setting = (ImageView) findViewById(R.id.home_iv_setting);
		home_gv_list = (GridView) findViewById(R.id.home_gv_list);

		home_iv_logo = (ImageView) findViewById(R.id.home_iv_logo);
	}
}
