package com.decard.socketlibs.port.tcp;

import android.util.Log;

import com.decard.socketlibs.port.IPort;
import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.RetryListener;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.google.common.base.Predicates;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class SocketPort implements IPort {

    private static final String TAG = "---SocketPort";
    private volatile static SocketPort mInstance = null;
    private Socket mTcpSocket = null;
    Retryer<Boolean> retryer = null;

    private SocketPort() {
        // RetryerBuilder 构建重试实例 retryer,可以设置重试源且可以支持多个重试源，
        // 可以配置重试次数或重试超时时间，以及可以配置等待时间间隔
        retryer = RetryerBuilder
                .<Boolean>newBuilder()
                .retryIfException()//设置异常重试源
                .retryIfResult(Predicates.equalTo(false))////设置自定义段元重试源//特别注意：这个apply返回true说明需要重试，与操作逻辑的语义要区分
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))//设置重试次数
                .withWaitStrategy(WaitStrategies.fixedWait(500L, TimeUnit.MILLISECONDS))//设置每次重试间隔
                .withRetryListener(new ConnectRetryListener())//自定义重试监听器，可以用于异步记录错误日志
                .build();

    }

    public static SocketPort getInstance() {
        if (mInstance == null) {
            synchronized (SocketPort.class) {
                mInstance = new SocketPort();
            }
        }
        return mInstance;
    }


    @Override
    public boolean open(String... param) {

        boolean isConnected = false;

        try {
            mTcpSocket = new Socket();
            mTcpSocket.setSoTimeout(10000);
            mTcpSocket.setReceiveBufferSize(4096);
            mTcpSocket.setSendBufferSize(4096);
            mTcpSocket.setKeepAlive(false);
            mTcpSocket.setTcpNoDelay(false);
            mTcpSocket.setOOBInline(false);
                isConnected = retryer.call(new ConnectCallable(mTcpSocket, param[0], Integer.parseInt(param[1])));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (RetryException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return isConnected;
    }

    private class ConnectRetryListener<Boolean> implements RetryListener {

        @Override
        public <Boolean> void onRetry(Attempt<Boolean> attempt) {
            Log.d(TAG, "第" + attempt.getAttemptNumber() + "次尝试连接...");
            if (attempt.hasResult()) {
                Log.d(TAG, "第" + attempt.getAttemptNumber() + "次尝试连接 : " + (attempt.getResult().equals(true) ? "成功" : "失败"));
            }

            if (attempt.hasException()) {
                Log.d(TAG, "第" + attempt.getAttemptNumber() + "次尝试连接失败 ，失败原因 ： " + attempt.getExceptionCause().toString());
            }
        }
    }


    private class ConnectCallable implements Callable<Boolean> {

        private Socket socket;
        private String host;
        private int port;

        public ConnectCallable(Socket s, String h, int p) {
            socket = s;
            host = h;
            port = p;
        }

        @Override
        public Boolean call() throws Exception {
            try {
                Log.d("TCP", "TCP socket 开始连接... ");
                Log.d("TCP", "ip地址 ： " + host);
                Log.d("TCP", "ip端口 ： " + port);
                socket.connect(new InetSocketAddress(host, port), 5000);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }


    @Override
    public boolean send(byte[] buffer, int offset, int len) {

        if (buffer == null || buffer.length == 0) {
            Log.e(TAG, "Tcp 发送数据为空！");
            return false;
        }

        if (!checkSocketIsReady()) {
            return false;
        }
        Log.d(TAG, "Tcp Send : " + new String(buffer));
        try {
            mTcpSocket.getOutputStream().write(buffer, offset, len);
            mTcpSocket.getOutputStream().flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public boolean read(byte[] buffer, int length) {

        if (buffer == null || buffer.length == 0) {
            Log.e(TAG, "Tcp 读取，未初始化缓冲区！");
            return false;
        }

        if (!checkSocketIsReady()) {
            return false;
        }
        try {
            DataInputStream dataInputStream = new DataInputStream(mTcpSocket.getInputStream());
            dataInputStream.readFully(buffer);
            Log.d(TAG, "Tcp Read : " + new String(buffer));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public byte[] read() {
        byte[] temp = new byte[512];
        int size;
        try {
            size = mTcpSocket.getInputStream().read(temp);
            if (size > 0) {
               byte[] buffer = new byte[size];
                System.arraycopy(temp, 0, buffer, 0, size);
                return buffer;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }


    @Override
    public boolean close() {
        Log.e(TAG, "Tcp 主动关闭连接！");
        try {

            if (!mTcpSocket.isInputShutdown()) {
                mTcpSocket.shutdownInput();
            }
            if (!mTcpSocket.isOutputShutdown()) {
                mTcpSocket.shutdownOutput();
            }
            if (!mTcpSocket.isClosed()) {
                mTcpSocket.close();
            }
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    public boolean checkSocketIsReady() {
        if (mTcpSocket == null) {
            Log.e(TAG, "Tcp Socket 未初始化！");
            return false;
        }

        if (!mTcpSocket.isConnected()) {
            Log.e(TAG, "Tcp Socket 未连接！");
            return false;
        }

        if (mTcpSocket.isClosed()) {
            Log.e(TAG, "Tcp Socket 已关闭！");
            return false;
        }

        if (mTcpSocket.isInputShutdown()) {
            Log.e(TAG, "Tcp 输入流 已关闭！");
            return false;
        }

        if (mTcpSocket.isOutputShutdown()) {
            Log.e(TAG, "Tcp 输出流 已关闭！");
            return false;
        }
        return true;

    }


}
