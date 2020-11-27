package com.decard.pdflibs;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class PDFAndBase64ConvertUtil {

	private static final String TAG = "PDFAndBase64ConvertUtil";

	/**
	 * 将base64编码转换成PDF
	 *
	 * @param base64String 1.使用BASE64Decoder对编码的字符串解码成字节数组
	 *                     2.使用底层输入流ByteArrayInputStream对象从字节数组中获取数据；
	 *                     3.建立从底层输入流中读取数据的BufferedInputStream缓冲输出流对象；
	 *                     4.使用BufferedOutputStream和FileOutputSteam输出数据到指定的文件中
	 */
	public static void base64StringToPDF(String base64String, File file) {
		BASE64Decoder decoder = new BASE64Decoder();
		BufferedInputStream bin = null;
		FileOutputStream fout = null;
		BufferedOutputStream bout = null;
		try {
			//将base64编码的字符串解码成字节数组
			byte[] bytes = decoder.decodeBuffer(base64String);
			//创建一个将bytes作为其缓冲区的ByteArrayInputStream对象
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			//创建从底层输入流中读取数据的缓冲输入流对象
			bin = new BufferedInputStream(bais);
			//创建到指定文件的输出流
			fout = new FileOutputStream(file);
			//为文件输出流对接缓冲输出流对象
			bout = new BufferedOutputStream(fout);

			byte[] buffers = new byte[1024];
			int len = bin.read(buffers);
			while (len != -1) {
				bout.write(buffers, 0, len);
				len = bin.read(buffers);
			}
			//刷新此输出流并强制写出所有缓冲的输出字节，必须这行代码，否则有可能有问题
			bout.flush();
		} catch (IOException e) {
			Log.d(TAG, "base64StringToPDF: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				bout.close();
				fout.close();
				bin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * PDF转换为Base64编码
	 *
	 * @param file
	 * @return
	 */
	public static String pdfToBase64(File file) {
		BASE64Encoder encoder = new BASE64Encoder();
		FileInputStream fin = null;
		BufferedInputStream bin = null;
		ByteArrayOutputStream baos = null;
		BufferedOutputStream bout = null;
		try {
			fin = new FileInputStream(file);
			bin = new BufferedInputStream(fin);
			baos = new ByteArrayOutputStream();
			bout = new BufferedOutputStream(baos);
			byte[] buffer = new byte[1024];
			int len = bin.read(buffer);
			while (len != -1) {
				bout.write(buffer, 0, len);
				len = bin.read(buffer);
			}
			//刷新此输出流并强制写出所有缓冲的输出字节
			bout.flush();
			byte[] bytes = baos.toByteArray();
			return encoder.encodeBuffer(bytes).trim();

		} catch (IOException e) {
			Log.d(TAG, "pdfToBase64: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				fin.close();
				bin.close();
				baos.close();
				bout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
