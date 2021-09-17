package com.decard.bluetoothlibs;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Method;

public class ConnectA2dpActivity extends AppCompatActivity {

	private BluetoothAdapter bluetoothAdapter;

	private Handler handler = new Handler();

	private BluetoothA2dp bluetoothA2dp;

	private static final String TAG = "---ConnectA2dpActivity";

	private BroadcastReceiver a2dpReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action != null) {
				switch (action) {
					case BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED:
						int connectState = intent.getIntExtra(BluetoothA2dp.EXTRA_STATE,
								BluetoothA2dp.STATE_DISCONNECTED);
						if (connectState == BluetoothA2dp.STATE_DISCONNECTED) {
							Log.d(TAG, "onReceive: 已断开连接");
						} else if (connectState == BluetoothA2dp.STATE_CONNECTED) {
							Log.d(TAG, "onReceive: 已连接");
						}
						break;
					case BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED:
						int playState = intent.getIntExtra(BluetoothA2dp.EXTRA_STATE,
								BluetoothA2dp.STATE_NOT_PLAYING);
						if (playState == BluetoothA2dp.STATE_PLAYING) {
							Log.d(TAG, "onReceive: 处于播放状态");
						} else if (playState == BluetoothA2dp.STATE_NOT_PLAYING) {
							Log.d(TAG, "onReceive: 未在播放");
						}
						break;
				}
			}
		}
	};

	private BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action != null) {
				switch (action) {
					case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
						Log.d(TAG, "onReceive: 正在搜索蓝牙设备，搜索时间大约一分钟");
						break;
					case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
						Log.d(TAG, "onReceive: 搜索蓝牙设备结束");
						break;
					case BluetoothDevice.ACTION_FOUND:
						BluetoothDevice bluetoothDevice =
								intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
						Log.d(TAG,
								"onReceive: " + bluetoothDevice.getAddress() + bluetoothDevice.getName());
						if (bluetoothDevice.getName() != null && bluetoothDevice.getName().equals(
								"BT07")) {
							device = bluetoothDevice;
						}
						break;
					case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
						int status = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE,
								BluetoothDevice.BOND_NONE);
						if (status == BluetoothDevice.BOND_BONDED) {
							Log.d(TAG, "onReceive: 已连接");
						} else if (status == BluetoothDevice.BOND_NONE) {
							Log.d(TAG, "onReceive: 未连接");
						}
						break;
				}
			}
		}
	};

	private BluetoothProfile.ServiceListener profileServiceListener =
			new BluetoothProfile.ServiceListener() {

				@Override
				public void onServiceDisconnected(int profile) {
					Log.d(TAG, "onServiceDisconnected: ");
					if (profile == BluetoothProfile.A2DP) {
						bluetoothA2dp = null;
					}
				}

				@Override
				public void onServiceConnected(int profile, final BluetoothProfile proxy) {
					Log.d(TAG, "onServiceConnected: " + profile);
					if (profile == BluetoothProfile.A2DP) {
						bluetoothA2dp = (BluetoothA2dp) proxy;
					}
				}
			};

//	private AdapterView.OnItemClickListener itemClickListener =
//			new AdapterView.OnItemClickListener() {
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//			BluetoothDevice device = deviceAdapter.getDevice(position);
//			if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
//				showToast("已连接该设备");
//				return;
//			}
//			showLoadingDialog("正在连接");
//			connectA2dp(device);
//		}
//	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect_a2dp);
		BluetoothManager bluetoothManager =
				(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		bluetoothAdapter = bluetoothManager.getAdapter();
		if (bluetoothAdapter == null || !getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			Log.d(TAG, "onCreate: 当前设备不支持蓝牙");
			finish();
			return;
		}
		boolean profileProxy = bluetoothAdapter.getProfileProxy(this, profileServiceListener,
				BluetoothProfile.A2DP);
		Log.d(TAG, "onCreate: profileProxy " + profileProxy);
		registerDiscoveryReceiver();
		registerA2dpReceiver();
		startScan();
		Button button = findViewById(R.id.btn_connect);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				connectA2dp(device);
			}
		});
	}

	BluetoothDevice device;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
		unregisterReceiver(a2dpReceiver);
		unregisterReceiver(discoveryReceiver);
		if (bluetoothAdapter.isDiscovering()) {
			bluetoothAdapter.cancelDiscovery();
		}
	}

	private void registerDiscoveryReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		registerReceiver(discoveryReceiver, intentFilter);
	}

	private void registerA2dpReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED);
		intentFilter.addAction(BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED);
		registerReceiver(a2dpReceiver, intentFilter);
	}

	private void startScan() {
		if (!bluetoothAdapter.isEnabled()) {
			if (bluetoothAdapter.enable()) {
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						scanDevice();
					}
				}, 1500);
			} else {
				Log.d(TAG, "startScan: 请求蓝牙权限被拒绝");
			}
		} else {
			scanDevice();
		}
	}

	private void scanDevice() {
		if (bluetoothAdapter.isDiscovering()) {
			bluetoothAdapter.cancelDiscovery();
		}
		bluetoothAdapter.startDiscovery();
	}

	public void setPriority(BluetoothDevice device, int priority) {
		try {
			Method connectMethod = BluetoothA2dp.class.getMethod("setPriority",
					BluetoothDevice.class, int.class);
			connectMethod.invoke(bluetoothA2dp, device, priority);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void connectA2dp(BluetoothDevice bluetoothDevice) {
		if (bluetoothA2dp == null || bluetoothDevice == null) {
			Log.d(TAG, "connectA2dp: 连接空");
			return;
		}
		setPriority(bluetoothDevice, 100);
		try {
			Method connectMethod = BluetoothA2dp.class.getMethod("connect",
					BluetoothDevice.class);
			connectMethod.invoke(bluetoothA2dp, bluetoothDevice);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}