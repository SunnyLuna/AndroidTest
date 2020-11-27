package com.decard.pdflibs;

import android.os.Environment;
import android.util.Log;

import com.itextpdf.awt.geom.Rectangle2D.Float;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ContentByteUtils;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfContentStreamProcessor;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取特殊字符关键字的位置工具类
 *
 * @author ZJ
 * created at 2020/11/5 16:27
 */

public class SealXYUtil {
    private static final String TAG = "----SealXYUtil";

    public static void xyxxxx() throws IOException {
        //1.给定文件
        File pdfFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Logan.pdf");
        //2.定义一个byte数组，长度为文件的长度
        byte[] pdfData = new byte[(int) pdfFile.length()];
        //3.IO流读取文件内容到byte数组
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(pdfFile);
            inputStream.read(pdfData);
        } catch (IOException e) {
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        //4.指定关键字
        String keyword = "写日志";
        //5.调用方法，给定关键字和文件
        List<float[]> positions = findKeywordPostions(pdfData, keyword);
        //6.返回值类型是 List<float[]> 每个list元素代表一个匹配的位置，分别为 float[0]所在页码 float[1]所在x轴 float[2]所在y轴
        System.out.println("total:" + positions.size());
        Log.d(TAG, "total:" + positions.size());
        if (positions != null && positions.size() > 0) {
            for (float[] position : positions) {
                Log.d(TAG, "pageNum: " + (int) position[0]);
                Log.d(TAG, "x: " + position[1]);
                Log.d(TAG, "y: " + position[2]);
            }
        }
    }

    /**
     * findKeywordPostions
     *
     * @param pdfData 通过IO流 PDF文件转化的byte数组
     * @param keyword 关键字
     * @return List<float [ ]> : float[0]:pageNum float[1]:x float[2]:y
     * @throws IOException
     */
    public static List<float[]> findKeywordPostions(byte[] pdfData, String keyword) throws IOException {
        List<float[]> result = new ArrayList<>();
        List<PdfPageContentPositions> pdfPageContentPositions = getPdfContentPostionsList(pdfData);
        Log.d(TAG, "findKeywordPostions: ");
        for (PdfPageContentPositions pdfPageContentPosition : pdfPageContentPositions) {
            List<float[]> charPositions = findPositions(keyword, pdfPageContentPosition);
            if (charPositions == null || charPositions.size() < 1) {
                continue;
            }
            result.addAll(charPositions);
        }
        return result;
    }

    private static List<PdfPageContentPositions> getPdfContentPostionsList(byte[] pdfData) throws IOException {
        PdfReader reader = new PdfReader(pdfData);
        List<PdfPageContentPositions> result = new ArrayList<>();
        int pages = reader.getNumberOfPages();
        for (int pageNum = 1; pageNum <= pages; pageNum++) {
            float width = reader.getPageSize(pageNum).getWidth();
            float height = reader.getPageSize(pageNum).getHeight();
            PdfRenderListener pdfRenderListener = new PdfRenderListener(pageNum, width, height);
            //解析pdf，定位位置
            PdfContentStreamProcessor processor = new PdfContentStreamProcessor(pdfRenderListener);
            PdfDictionary pageDic = reader.getPageN(pageNum);
            PdfDictionary resourcesDic = pageDic.getAsDict(PdfName.RESOURCES);
            try {
                processor.processContent(ContentByteUtils.getContentBytesForPage(reader, pageNum), resourcesDic);
            } catch (IOException e) {
                reader.close();
                throw e;
            }
            String content = pdfRenderListener.getContent();
            Log.d(TAG, "getPdfContentPostionsList: " + content);
            List<CharPosition> charPositions = pdfRenderListener.getcharPositions();
            List<float[]> positionsList = new ArrayList<>();
            for (CharPosition charPosition : charPositions) {
                float[] positions = new float[]{charPosition.getPageNum(), charPosition.getX(), charPosition.getY()};
                positionsList.add(positions);
            }
            PdfPageContentPositions pdfPageContentPositions = new PdfPageContentPositions();
            pdfPageContentPositions.setContent(content);
            pdfPageContentPositions.setPostions(positionsList);
            result.add(pdfPageContentPositions);
        }
        reader.close();
        return result;
    }

    private static List<float[]> findPositions(String keyword, PdfPageContentPositions pdfPageContentPositions) {
        List<float[]> result = new ArrayList<>();
        String content = pdfPageContentPositions.getContent();
        List<float[]> charPositions = pdfPageContentPositions.getPositions();
        for (int pos = 0; pos < content.length(); ) {
            int positionIndex = content.indexOf(keyword, pos);
            if (positionIndex == -1) {
                break;
            }
            float[] postions = charPositions.get(positionIndex);
            result.add(postions);
            pos = positionIndex + 1;
        }
        return result;
    }

    private static class PdfPageContentPositions {
        private String content;
        private List<float[]> positions;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<float[]> getPositions() {
            return positions;
        }

        public void setPostions(List<float[]> positions) {
            this.positions = positions;
        }
    }

    private static class PdfRenderListener implements RenderListener {
        private int pageNum;
        private float pageWidth;
        private float pageHeight;
        private StringBuilder contentBuilder = new StringBuilder();
        private List<CharPosition> charPositions = new ArrayList<>();

        public PdfRenderListener(int pageNum, float pageWidth, float pageHeight) {
            this.pageNum = pageNum;
            this.pageWidth = pageWidth;
            this.pageHeight = pageHeight;
        }

        public void beginTextBlock() {
        }

        public void renderText(TextRenderInfo renderInfo) {
            List<TextRenderInfo> characterRenderInfos = renderInfo.getCharacterRenderInfos();
            for (TextRenderInfo textRenderInfo : characterRenderInfos) {
                String word = textRenderInfo.getText();
                Log.d(TAG, "renderText: " + word);
                if (word.length() > 1) {
                    word = word.substring(word.length() - 1, word.length());
                }
                Log.d(TAG, "renderText: word:" + word);
                Float rectangle = textRenderInfo.getAscentLine().getBoundingRectange();
                float x = (float) rectangle.getX();
                float y = (float) rectangle.getY();
//    float x = (float)rectangle.getCenterX();
////    float y = (float)rectangle.getCenterY();
////    double x = rectangle.getMinX();
////    double y = rectangle.getMaxY();
                //这两个是关键字在所在页面的XY轴的百分比
                float xPercent = Math.round(x / pageWidth * 10000) / 10000f;
                float yPercent = Math.round((1 - y / pageHeight) * 10000) / 10000f;
//    CharPosition charPosition = new CharPosition(pageNum, xPercent, yPercent);
                CharPosition charPosition = new CharPosition(pageNum, x, y);
                charPositions.add(charPosition);
                contentBuilder.append(word);
            }
        }

        public void endTextBlock() {
        }

        public void renderImage(ImageRenderInfo renderInfo) {
        }

        public String getContent() {
            return contentBuilder.toString();
        }

        public List<CharPosition> getcharPositions() {
            return charPositions;
        }
    }

    private static class CharPosition {
        private int pageNum = 0;
        private float x = 0;
        private float y = 0;

        public CharPosition(int pageNum, float x, float y) {
            this.pageNum = pageNum;
            this.x = x;
            this.y = y;
        }

        public int getPageNum() {
            return pageNum;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        @Override
        public String toString() {
            return "[pageNum=" + this.pageNum + ",x=" + this.x + ",y=" + this.y + "]";
        }
    }
}
