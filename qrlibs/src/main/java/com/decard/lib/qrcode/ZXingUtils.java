package com.decard.lib.qrcode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ZXingUtils {
	private static final String TAG = "---ZXingUtils";

	/**
	 * 生成二维码
	 *
	 * @param content 内容
	 * @param width   二维码图片宽
	 * @param height  二维码图片高
	 * @return 二维码照片
	 */
	public static Bitmap generateQRBitmap(String content, int width, int height) {
		QRCodeWriter codeWriter = new QRCodeWriter();
		//EncodeHintType  编码显示风格
		Map<EncodeHintType, String> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//编码格式
		hints.put(EncodeHintType.MARGIN, "1");//外边框
		try {
			BitMatrix matrix = codeWriter.encode(content, BarcodeFormat.QR_CODE, width, height,
					hints);
			int pixels[] = new int[width * height];
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					if (matrix.get(j, i)) {
						pixels[i * width + j] = Color.BLACK;
					} else {
						pixels[i * width + j] = Color.WHITE;
					}
				}
			}
			return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 生成二维码 中间插入小图片
	 *
	 * @param content 内容
	 * @param width   二维码图片宽
	 * @param height  二维码图片高
	 * @param icon    插入图片的bitmap
	 * @return
	 * @throws WriterException
	 */
	public static Bitmap generateQRBitmap(String content, int width, int height, Bitmap icon) {
		int IMAGE_HALFWIDTH = width / 8;
		icon = zoomBitmap(icon, IMAGE_HALFWIDTH);
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 1);
		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = null;
		try {
			matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height,
					hints);
			// 二维矩阵转为一维像素数组,也就是一直横着排了
			int halfW = width / 2;
			int halfH = height / 2;
			int[] pixels = new int[width * height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH && y > halfH - IMAGE_HALFWIDTH && y < halfH + IMAGE_HALFWIDTH) {
						pixels[y * width + x] = icon.getPixel(x - halfW + IMAGE_HALFWIDTH,
								y - halfH + IMAGE_HALFWIDTH);
					} else {
						if (matrix.get(x, y)) {
							pixels[y * width + x] = 0xff000000;
						} else { // 无信息设置像素点为白色
							pixels[y * width + x] = 0xffffffff;
						}
					}
				}
			}
			Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			// 通过像素数组生成bitmap
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 缩放图片
	 */
	private static Bitmap zoomBitmap(Bitmap icon, int h) {
		// 缩放图片
		Matrix m = new Matrix();
		float sx = (float) 2 * h / icon.getWidth();
		float sy = (float) 2 * h / icon.getHeight();
		m.setScale(sx, sy);
		// 重新构造一个图片
		return Bitmap.createBitmap(icon, 0, 0, icon.getWidth(), icon.getHeight(), m, false);
	}


	/**
	 * 生成条形码
	 *
	 * @param str 条形码的字符串
	 * @return
	 * @throws WriterException
	 */
	public static Bitmap generateBarcodeBitmap(String str) {
		int width = 800;
		int height = 200;
		BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;
		BitMatrix matrix = null;
		try {
			matrix = new MultiFormatWriter().encode(str, barcodeFormat, width, height, null);
			return bitMatrix2Bitmap(matrix);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Bitmap bitMatrix2Bitmap(BitMatrix matrix) {
		int w = matrix.getWidth();
		int h = matrix.getHeight();
		int[] rawData = new int[w * h];
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int color = Color.WHITE;
				if (matrix.get(i, j)) {
					// 有内容的部分，颜色设置为黑色，可以自己修改成其他颜色
					color = Color.BLACK;
				}
				rawData[i + (j * w)] = color;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
		return bitmap;
	}


	/**
	 * zxing解码二维码图片
	 *
	 * @param bitmap
	 * @return
	 * @throws NotFoundException
	 */
	public static String decodeQrcodeBitmap(Bitmap bitmap) {
		Map<DecodeHintType, Object> hints =
				new EnumMap<>(DecodeHintType.class);
//		Collection<BarcodeFormat> decodeFormats = EnumSet.noneOf(BarcodeFormat.class);
//		decodeFormats.addAll(EnumSet.of(BarcodeFormat.QR_CODE));
//		decodeFormats.addAll(EnumSet.of(BarcodeFormat.CODABAR));
		List<BarcodeFormat> decodeFormats = new ArrayList<>();
		decodeFormats.add(BarcodeFormat.QR_CODE);
		hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
		hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
		hints.put(DecodeHintType.PURE_BARCODE, BarcodeFormat.QR_CODE);

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int[] lPixels = new int[width * height];
		bitmap.getPixels(lPixels, 0, width, 0, 0, width, height);
		RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(width, height, lPixels);

		BinaryBitmap binaryBitmap =
				new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));
		QRCodeReader reader = new QRCodeReader();
		String qrText = "";
		try {
			Result lResult = reader.decode(binaryBitmap, hints);
			qrText = lResult.getText();
		} catch (NotFoundException e) {
			Log.d(TAG, "zxingScanQrcodePicture: " + e.getMessage());
			e.printStackTrace();
		} catch (FormatException e) {
			Log.d(TAG, "zxingScanQrcodePicture: FormatException" + e.getMessage());
			e.printStackTrace();
		} catch (ChecksumException e) {
			Log.d(TAG, "zxingScanQrcodePicture: ChecksumException" + e.getMessage());
			e.printStackTrace();
		}
		Log.d(TAG, "zxingScanQrcodePicture: qrText:  " + qrText);
		return qrText;
	}


	/**
	 * zxing解码二维码图片
	 *
	 * @param filePath 图片路径
	 * @return content 二维码数据
	 */
	public static String decodeQrcodePicture(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			Log.d(TAG, "decodeQrcodePicture: 文件不存在");
			return "";
		}
		Bitmap bitmap = BitmapFactory.decodeFile(filePath);
		if (bitmap == null) {
			return "";
		}
		return decodeOneBitmap(bitmap);
	}


	public static String decodeOneBitmap(Bitmap bitmap) {
		Map<DecodeHintType, Object> hints =
				new EnumMap<>(DecodeHintType.class);
//		Collection<BarcodeFormat> decodeFormats = EnumSet.noneOf(BarcodeFormat.class);
//		decodeFormats.addAll(EnumSet.of(BarcodeFormat.QR_CODE));
//		decodeFormats.addAll(EnumSet.of(BarcodeFormat.CODABAR));
		List<BarcodeFormat> decodeFormats = new ArrayList<>();
		decodeFormats.add(BarcodeFormat.CODE_128);
//		hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
		hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
		hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int[] lPixels = new int[width * height];
		bitmap.getPixels(lPixels, 0, width, 0, 0, width, height);
		RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(width, height, lPixels);

		BinaryBitmap binaryBitmap =
				new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));
		MultiFormatReader reader = new MultiFormatReader();
		String qrText = "";
		try {
			Result lResult = reader.decode(binaryBitmap, hints);
			qrText = lResult.getText();
		} catch (NotFoundException e) {
			Log.d(TAG, "zxingScanQrcodePicture: NotFoundException" + e.getMessage());
			e.printStackTrace();
		}
		Log.d(TAG, "zxingScanQrcodePicture: qrText:  " + qrText);
		return qrText;
	}

}
