package com.example.commonlibs.tvl;

import android.util.SparseArray;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TLVUtils {


    public static class TLV {

        private String tag;
        private int len;
        private String value;


        public TLV(String tag, int len, String value) {
            this.tag = tag;
            this.len = len;
            this.value = value;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public int getLen() {
            return len;
        }

        public void setLen(int len) {
            this.len = len;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


    public static SparseArray<TLV> unpack(String src) {

        SparseArray<TLV> tlvSparseArray = new SparseArray<>();

        int pos = 0;
        int srcLen = src.length();
        while (pos < srcLen) {

            String tag = src.substring(pos, pos + 4);
            pos += 4;

            int len = Integer.parseInt(src.substring(pos, pos + 4), 16);
            pos += 4;


            String value = src.substring(pos, pos + (len * 2));
            pos += (len * 2);

            TLV tlv = new TLV(tag, len, value);
            tlvSparseArray.put(Integer.parseInt(tag), tlv);


        }

        return tlvSparseArray;

    }


    public static HashMap<Integer, TLV> unpackToHashMap(String src) {

        HashMap<Integer, TLV> tlvHashMap = new HashMap<>();

        int pos = 0;
        int srcLen = src.length();
        while (pos < srcLen) {

            String tag = src.substring(pos, pos + 4);
            pos += 4;

            int len = Integer.parseInt(src.substring(pos, pos + 4), 16);
            pos += 4;
            String value = src.substring(pos, pos + (len * 2));
            pos += (len * 2);
            TLV tlv = new TLV(tag, len, value);
            System.out.println("T :" + tlv.getTag() + "  L :" + tlv.getLen() + " V: " + value);
            tlvHashMap.put(Integer.parseInt(tag), tlv);
        }

        return tlvHashMap;
    }


    public static String build(int id, String parameterHex) {
        if (parameterHex != null && !parameterHex.isEmpty()) {
            return new StringBuilder()
                    .append(String.format("%04d", id))
                    .append(String.format("%04X", parameterHex.length() / 2))
                    .append(parameterHex)
                    .toString();
        }

        return "";
    }

//    private static String build(String id,String parameter){
//        byte[] bytes;
//        String hex;
//        String len;
//
//        if(!parameter.isEmpty()){
//            StringBuilder sb = new StringBuilder();
//            bytes = parameter.getBytes();
//            len = String.format("%04X",bytes.length);
//            hex = HexUtils.toHexString(bytes);
//            return sb.append(id)
//                    .append(len)
//                    .append(hex)
//                    .toString();
//        }
//
//        return "";
//    }


    public static String build(TLV tlv) {
        String tag;
        String hex;
        String len;

        if (tlv != null) {
            StringBuilder sb = new StringBuilder();
            tag = tlv.getTag();
            len = String.format("%04X", tlv.getLen());
            hex = tlv.getValue();
            return sb.append(tag)
                    .append(len)
                    .append(hex)
                    .toString();
        }

        return "";
    }


    public static String buildFromMap(HashMap<Integer, TLV> tlvHashMap) {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator iterator = tlvHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            TLV tlv = (TLV) entry.getValue();
            stringBuilder.append((tlv == null) ? "" : TLVUtils.build(tlv));

        }
        return stringBuilder.toString();
    }


}
