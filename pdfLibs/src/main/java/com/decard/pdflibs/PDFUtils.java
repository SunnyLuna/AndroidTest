package com.decard.pdflibs;

import android.os.Environment;
import android.util.Log;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.awt.geom.Rectangle2D.Float;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * PDF操作工具类
 *
 * @author ZJ
 * 1.获取关键字的绝对位置
 * 2.在pdf上签章
 * created at 2020/10/29 17:49
 */
public class PDFUtils {


	private static final String TAG = "----PDFUtils";


	/**
	 * 根据坐标指哪签哪
	 *
	 * @param img     签名图片
	 * @param pageNum 签名页数
	 * @param offsetX 签名位于PDF的X坐标
	 * @param offsetY 签名在PDF的Y坐标
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void addPi(final Image img, int pageNum, final float offsetX,
	                         final float offsetY) throws IOException, DocumentException {
		String source =
				Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
						"sign.pdf";
		String target =
				Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
						"signed.pdf";
		if (!new File(source).exists()) {
			Log.d(TAG, "addPi: source文件不存在");
			return;
		}
		PdfReader pdfReader = new PdfReader(source);
		FileOutputStream outputStream = new FileOutputStream(target);
		PdfStamper pdfStamper = new PdfStamper(pdfReader, outputStream);
		PdfContentByte pdfContentByte = pdfStamper.getOverContent(pageNum);
		BaseColor baseColor = new BaseColor(255, 255, 255, 0);
		img.scaleToFit(100, 100);

		//设置图片位置
		img.setAbsolutePosition(offsetX, offsetY);
		img.setBackgroundColor(baseColor);
		img.setBorderColor(baseColor);
		pdfContentByte.addImage(img);
		pdfContentByte.stroke();
		pdfStamper.close();
		pdfReader.close();
		Log.d(TAG, "addPi: 完成");
	}


	/**
	 * 根据pdf中的关键字，获取文字的绝对位置，从而确定签章位置
	 *
	 * @param img     签章图片
	 * @param keyWord 关键字
	 * @param pageNum pdf页数,可传null，默认设置最大页数
	 * @param offsetX x的偏移量（左-右+）
	 * @param offsetY y的偏移量（上+下-）
	 * @throws IOException
	 */
	public static void addPi(final Image img, final String keyWord, int pageNum,
	                         final float offsetX, final float offsetY) throws IOException,
			DocumentException {
		String source =
				Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
						"intelligentKit/SourceSign.pdf";
		String target =
				Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
						"intelligentKit/Signed.pdf";
		if (!new File(source).exists()) {
			Log.d(TAG, "addPi: source文件不存在");
			return;
		}
		PdfReader pdfReader = new PdfReader(source);
		FileOutputStream outputStream = new FileOutputStream(target);
		PdfStamper pdfStamper = new PdfStamper(pdfReader, outputStream);
		PdfContentByte pdfContentByte = pdfStamper.getOverContent(pageNum);
		BaseColor baseColor = new BaseColor(255, 255, 255, 0);
		img.scaleToFit(100, 100);
		final float[] absoluteX = {400};
		final float[] absoluteY = {50};
		new PdfReaderContentParser(pdfReader).processContent(pageNum, new RenderListener() {
			public void beginTextBlock() {
			}

			public void renderText(TextRenderInfo textRenderInfo) {
				String text = textRenderInfo.getText();
				Log.d(TAG, "renderText: " + text);
				if (text != null && text.contains(keyWord)) {
					// 文字在page中的横坐标、纵坐标
					Rectangle2D.Float textFloat =
							textRenderInfo.getBaseline().getBoundingRectange();
					float x = textFloat.x;
					float y = textFloat.y;
					Log.d(TAG, "renderText: x: " + x + "y: " + y);
					absoluteX[0] = x + offsetX;
					absoluteY[0] = y - 100 + offsetY;

				}
			}

			public void endTextBlock() {

			}

			public void renderImage(ImageRenderInfo renderInfo) {

			}
		});
		Log.d(TAG, "addPi: " + absoluteX[0] + absoluteY[0]);
		//设置图片位置
		img.setAbsolutePosition(absoluteX[0], absoluteY[0]);
		img.setBackgroundColor(baseColor);
		img.setBorderColor(baseColor);
		pdfContentByte.addImage(img);
		pdfContentByte.stroke();
		pdfStamper.close();
		pdfReader.close();
		Log.d(TAG, "addPi: 完成");
	}


	/**
	 * 根据pdf中的关键字，获取文字的绝对位置，从而确定签章位置
	 *
	 * @param filePath pdf文件的绝对地址
	 * @param filePath pdf中的关键字
	 * @param pageNum  pdf页数,可传null，默认设置最大页数
	 * @return x与y值
	 * @throws IOException
	 */
	public static float[] getFontPosition(String filePath, final String keyWord, Integer pageNum) throws IOException {
		final float[] result = new float[2];
		PdfReader pdfReader = new PdfReader(filePath);
		if (null == pageNum) {
			pageNum = pdfReader.getNumberOfPages();
		}
		final StringBuffer stringBuffer = new StringBuffer();
		new PdfReaderContentParser(pdfReader).processContent(pageNum, new RenderListener() {
			public void beginTextBlock() {
				Log.d(TAG, "beginTextBlock: ");
			}

			public void renderText(TextRenderInfo textRenderInfo) {
				stringBuffer.append(textRenderInfo.getText());
				String text = textRenderInfo.getText();
//                Log.d(TAG, "renderText: " + text);
				if (text != null && text.contains(keyWord)) {
					// 文字在page中的横坐标、纵坐标
					Float textFloat = textRenderInfo.getBaseline().getBoundingRectange();
					result[0] = textFloat.x;
					result[1] = textFloat.y;
					Log.d(TAG, "renderText: 签章文字域绝对位置为" + result[0] + "    " + result[1]);
				}
			}

			public void endTextBlock() {
				Log.d(TAG, "endTextBlock: ");
			}

			public void renderImage(ImageRenderInfo renderInfo) {
				Log.d(TAG, "renderImage: ");
			}
		});
		Log.d(TAG, "getFontPosition: " + stringBuffer.toString());
		return result;
	}


	/**
	 * 根据pdf中的关键字，获取文字的绝对位置，并进行签章
	 *
	 * @param inputPath  未处理pdf
	 * @param targetPath 已签章pdf地址
	 * @param imagePath  签章图片地址
	 * @param inputPath  pdf中的关键字
	 * @param pageNum    pdf页数,可传null，默认设置最大页数
	 * @return float的x与y值
	 * @throws IOException
	 */
	public static void addSignImg(String inputPath, String targetPath, final String imagePath,
	                              final String keyWord, Integer pageNum) throws IOException,
			DocumentException {
		PdfReader pdfReader = new PdfReader(inputPath);
		// 读图片
		final Image image = Image.getInstance(imagePath);
		// 根据域的大小缩放图片
		image.scaleToFit(120, 120);

		if (null == pageNum) {
			pageNum = pdfReader.getNumberOfPages();
		}
		new PdfReaderContentParser(pdfReader).processContent(pageNum, new RenderListener() {
			public void beginTextBlock() {

			}

			public void renderText(TextRenderInfo textRenderInfo) {
				String text = textRenderInfo.getText();
				if (text != null && text.contains(keyWord)) {
					// 文字在page中的横坐标、纵坐标
					Rectangle2D.Float textFloat =
							textRenderInfo.getBaseline().getBoundingRectange();
					float x = textFloat.x;
					float y = textFloat.y;
					// 设置图片位置
					image.setAbsolutePosition(x + 50f, y - 30f);
				}
			}

			public void endTextBlock() {

			}

			public void renderImage(ImageRenderInfo renderInfo) {

			}
		});
		// 获取操作的页面
		PdfStamper stamper = new PdfStamper(pdfReader, new FileOutputStream(targetPath));
		PdfContentByte under = stamper.getOverContent(pageNum);
		under.addImage(image);
		stamper.close();
		pdfReader.close();
	}

}
