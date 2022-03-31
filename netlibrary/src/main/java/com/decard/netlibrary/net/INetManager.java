package com.decard.netlibrary.net;

import java.io.File;

public interface INetManager {

    void get(String url, INetCallback callback);


    void download(String url, File targetFile, INetDownloadCallback callback);
}
