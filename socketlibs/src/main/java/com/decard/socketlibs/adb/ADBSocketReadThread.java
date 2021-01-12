package com.decard.socketlibs.adb;

import android.os.SystemClock;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * 读adb数据线程
 */
public class ADBSocketReadThread extends Thread {

	private static final String TAG = "---ADBSocketReadThread";

	private BufferedInputStream mInputStream;
	ADBSocketManager.ADBSocketListener mAdbSocketListener;

	public ADBSocketReadThread(InputStream is,
	                           ADBSocketManager.ADBSocketListener adbSocketListener) {
		mAdbSocketListener = adbSocketListener;
		mInputStream = new BufferedInputStream(is);
	}

	@Override
	public void run() {
		byte[] received = new byte[2048];
		int size;
		Log.d(TAG, Thread.currentThread().getName() + "run:开始读线程 ");
		while (true) {
			if (Thread.currentThread().isInterrupted()) {
				break;
			}
			try {
				int available = mInputStream.available();
				if (available > 0) {
					size = mInputStream.read(received);
					Log.d(TAG, "run: " + size);
					if (size > 0) {

						onDataReceive(received, size);
					}
				} else {
					// 暂停一点时间，免得一直循环造成CPU占用率过高
					SystemClock.sleep(1);
				}
			} catch (IOException e) {
				Log.d(TAG, "run: " + e);
			}
		}
	}

	/**
	 * 处理获取到的数据
	 *
	 * @param received
	 * @param size
	 */
	private void onDataReceive(byte[] received, int size) {

		byte[] temp = new byte[size];
		System.arraycopy(received, 0, temp, 0, size);
		mAdbSocketListener.receiveADBData(temp);
		//接收Pc传来的信息
		dealData(temp);
	}


	private void dealData(byte[] bytes) {
		Log.d(TAG, "dealData: " + (new String(bytes)).trim());
		String msg = null;
		try {
			msg = (new String(bytes, "GBK")).trim();
		} catch (UnsupportedEncodingException e) {
			Log.d(TAG, "dealData: " + e.getMessage());
			e.printStackTrace();
		}
		Log.d(TAG, "dealData: " + msg);
	}

	/**
	 * 停止读线程
	 */
	public void close() {
		try {
			mInputStream.close();
		} catch (IOException e) {
			Log.d(TAG, "close: 异常" + e);
		} finally {
			super.interrupt();
		}
	}
}
