package com.decard.socketlibs.adb;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;


/**
 * ADB SOCKET 通信管理类
 *
 * @author ZJ
 * created at 2020/12/11 9:48
 */
public class ADBSocketManager {
	private static final String TAG = "---ADBSocketManager";

	private ServerSocket mServerSocket;
	private static Socket mSocket;
	private ADBSocketReadThread mReadThread;
	private OutputStream mOutputStream;
	private HandlerThread mWriteThread;
	private Scheduler mSendScheduler;
	private volatile boolean isLoop;
	private Handler handler;

	private static class InstanceHolder {

		static ADBSocketManager sManager = new ADBSocketManager();
	}

	public static ADBSocketManager instance() {
		return InstanceHolder.sManager;
	}


	/**
	 * 创建socket服务
	 *
	 * @return
	 */
	public void createServer(final ADBSocketListener adbSocketListener) {
		isLoop = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mServerSocket = new ServerSocket(8789, 1);
					Log.d(TAG, "run: 1");
					while (isLoop) {
						//阻塞等待客户端的接入
						mSocket = mServerSocket.accept();
						Log.d(TAG, "run: 2");
						mReadThread = new ADBSocketReadThread(mSocket.getInputStream(),adbSocketListener);
						mReadThread.start();

						mOutputStream = mSocket.getOutputStream();

						mWriteThread = new HandlerThread("write-thread");
						mWriteThread.start();
						handler = new Handler(mWriteThread.getLooper()) {
							@Override
							public void handleMessage(Message msg) {
								super.handleMessage(msg);
								try {
									mOutputStream.write(msg.getData().getByteArray("msg"));
								} catch (IOException e) {
									Log.d(TAG, "handleMessage: " + e.getMessage());
									e.printStackTrace();
								}
								Log.d(TAG,
										"消息： " + msg.what + "  线程： " + Thread.currentThread().getName());
							}
						};
//						mSendScheduler = AndroidSchedulers.from(mWriteThread.getLooper());
//						mSocket.sendUrgentData(255);

						try {
							while (true) {
								mSocket.sendUrgentData(255);
								Thread.sleep(200);
							}
						} catch (IOException e) {
							Log.d(TAG, "run: 断开");
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} catch (IOException e) {
					Log.d(TAG, "createServer: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 关闭串口
	 */
	public void close() {
		isLoop = false;
		if (mReadThread != null) {
			mReadThread.close();
		}
		if (mOutputStream != null) {
			try {
				mOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (mWriteThread != null) {
			mWriteThread.quit();
		}


	}

	/**
	 * 发送数据
	 *
	 * @param datas
	 * @return
	 */
	public void sendData(byte[] datas) throws Exception {
		if (handler != null) {
			Message message = handler.obtainMessage(1);
			Bundle bundle = new Bundle();
			bundle.putByteArray("msg", datas);
			message.setData(bundle);
			handler.sendMessage(message);
		}
	}

	/**
	 * (rx包裹)发送数据
	 *
	 * @param datas
	 * @return
	 */
	private Observable<Object> rxSendData(final byte[] datas) {

		return Observable.create(new ObservableOnSubscribe<Object>() {
			@Override
			public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
				try {
					sendData(datas);
					emitter.onNext(new Object());
				} catch (Exception e) {
					if (!emitter.isDisposed()) {
						emitter.onError(e);
						return;
					}
				}
				emitter.onComplete();
			}
		});
	}


	/**
	 * 发送 命令
	 *
	 * @param command
	 */
	public void sendCommand(final String command) {

		byte[] bytes = null;
		try {
			bytes = command.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		rxSendData(bytes).subscribeOn(mSendScheduler).subscribe(new Observer<Object>() {
			@Override
			public void onSubscribe(Disposable d) {
			}

			@Override
			public void onNext(Object o) {
			}

			@Override
			public void onError(Throwable e) {
				Log.d(TAG, "onError: 发送失败" + e);
			}

			@Override
			public void onComplete() {
			}
		});
	}

	public interface ADBSocketListener {
		public void receiveADBData(byte[] bytes);
	}
}
