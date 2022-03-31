package com.decard.netlibrary.net;

public interface INetCallback {

    void success(String response);

    void failed(Throwable throwable);
}
