package com.decard.socketlibs.port;

/**********************************************
 * 作者： Tom 
 * 日期： 2018-01-04
 * 描述： 
 ***********************************************/

public interface IPort {

    boolean open(String... param);

    boolean close();

    boolean send(byte[] buffer, int offset, int len);

    boolean read(byte[] buffer, int len);

    byte[] read();
}
