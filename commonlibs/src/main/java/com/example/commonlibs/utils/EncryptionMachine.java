package com.example.commonlibs.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class EncryptionMachine {


	//初始向量
	private static String ivInfo = "2020132020202021";

	private static char[] CHARARRAY= {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	private static byte[] funStringToBcd(char[] data){
		int len = data.length;
		if(len % 2 != 0 || len == 0){
			throw new RuntimeException("数据长度错误");
		}
		byte[] outData = new byte[len >> 1];
		for(int i=0, j=0; j<len; i++){
			outData[i] = (byte) (((Character.digit(data[j], 16) & 0x0F) << 4) | (Character.digit(data[j+1], 16) & 0x0F));
			j++;
			j++;
		}

		return outData;
	}

	private static String funByteToHexString(byte[] data){
		int len = data.length;
		char[] outChar = new char[len<<1];
		for(int i=0, j=0; j<len; j++){
			outChar[i++] = CHARARRAY[(0xF0 & data[j]) >>> 4];
			outChar[i++] = CHARARRAY[data[j] & 0x0F];
		}
		String outString = new String(outChar);

		return outString;
	}


	/**
	 * 加密函数(ECB)
	 *
	 * @param data
	 *            加密数据
	 * @param key
	 *            密钥
	 * @return 返回加密后的数据
	 */
	public static byte[] ecbEncrypt(byte[] data, byte[] key) {

		try {

			// DES算法要求有一个可信任的随机数源
			SecureRandom sr = new SecureRandom();

			// 从原始密钥数据创建DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(key);

			// 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(dks);

			// DES的ECB模式
			Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");

			// 用密钥初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);

			// 执行加密操作
			byte encryptedData[] = cipher.doFinal(data);

			return encryptedData;
		} catch (Exception e) {
			throw new RuntimeException("ECB-DES算法，加密数据出错!");
		}

	}

	/**
	 * 解密函数(ECB)
	 *
	 * @param data
	 *            解密数据
	 * @param key
	 *            密钥
	 * @return 返回解密后的数据
	 */
	public static byte[] ecbDecrypt(byte[] data, byte[] key) {
		try {
			// DES算法要求有一个可信任的随机数源
			SecureRandom sr = new SecureRandom();

			// byte rawKeyData[] = /* 用某种方法获取原始密匙数据 */;

			// 从原始密匙数据创建一个DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(key);

			// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成 一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(dks);

			// DES的ECB模式
			Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");

			// 用密钥初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);

			// 正式执行解密操作
			byte decryptedData[] = cipher.doFinal(data);

			return decryptedData;
		} catch (Exception e) {
			throw new RuntimeException("ECB-DES算法，解密出错。");
		}

	}

	/**
	 * 加密函数(CBC)
	 *
	 * @param data
	 *            加密数据
	 * @param key
	 *            密钥
	 * @return 返回加密后的数据
	 */
	public static byte[] cbcEncrypt(byte[] data, byte[] key, byte[] iv) {

		try {
			// 从原始密钥数据创建DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(key);

			// 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(dks);

			// DES的CBC模式,采用NoPadding模式，data长度必须是8的倍数
			Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");

			// 用密钥初始化Cipher对象
			IvParameterSpec param = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, param);

			// 执行加密操作
			byte encryptedData[] = cipher.doFinal(data);

			return encryptedData;
		} catch (Exception e) {
			throw new RuntimeException("CBC-DES算法，加密数据出错!");
		}
	}

	public static byte[] cbc3DESEncrypt(byte[] data, byte[] key, byte[] iv) {

		try {
			// 从原始密钥数据创建DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(key);

			// 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(dks);

			// DES的CBC模式,采用NoPadding模式，data长度必须是8的倍数
			Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");

			// 用密钥初始化Cipher对象
			IvParameterSpec param = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, param);

			// 执行加密操作
			byte encryptedData[] = cipher.doFinal(data);

			return encryptedData;
		} catch (Exception e) {
			throw new RuntimeException("CBC-DES算法，加密数据出错!");
		}
	}

	/**
	 * 解密函数(CBC)
	 *
	 * @param data
	 *            解密数据
	 * @param key
	 *            密钥
	 * @return 返回解密后的数据
	 */
	@SuppressWarnings("unused")
	public static byte[] cbcDecrypt(byte[] data, byte[] key, byte[] iv) {
		try {
			// 从原始密匙数据创建一个DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(key);

			// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(dks);

			// DES的CBC模式,采用NoPadding模式，data长度必须是8的倍数
			Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");

			// 用密钥初始化Cipher对象
			IvParameterSpec param = new IvParameterSpec(iv);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, param);

			// 正式执行解密操作
			byte decryptedData[] = cipher.doFinal(data);

			return decryptedData;
		} catch (Exception e) {
			throw new RuntimeException("CBC-DES算法，解密出错。");
		}

	}

	/**
	 * 将工作密钥解为明文
	 *@param plaintextZmk
	 *		解workKey的Zmk(明文)
	 *@param ciphertextWorkingKey
	 *		workKey密文
	 *@return
	 *		返回workKey明文
	 **/
	public static String analogDecryptWorkingKey(String plaintextZmk, String ciphertextWorkingKey){
		byte[] inPlaintextZmk = funStringToBcd(plaintextZmk.toCharArray());
		byte[] inCiphertextWorkingKey = funStringToBcd(ciphertextWorkingKey.toCharArray());

		byte[] leftPlaintextZmk = ArrayUtils.subarray(inPlaintextZmk, 0, 8);
		byte[] rightPlaintextZmk = ArrayUtils.subarray(inPlaintextZmk, 8, 16);

		byte[] leftCiphertextWorkingKey = ArrayUtils.subarray(inCiphertextWorkingKey, 0, 8);
		byte[] rightCiphertextWorkingKey = ArrayUtils.subarray(inCiphertextWorkingKey, 8, 16);

		// 一、WorkingKey左部分解密 (反3DES)
		/* 1、ECB-DES解密  left key */
		byte[] ecbDecryptReturn = ecbDecrypt(leftCiphertextWorkingKey, leftPlaintextZmk);
		System.out.println(funByteToHexString(ecbDecryptReturn));
		/* 2、 ECB-DES加密  right key */
		byte[] ecbEncryptReturn = ecbEncrypt(ecbDecryptReturn, rightPlaintextZmk);
		/* 3、 ECB-DES解密  left key */
		byte[] leftPlaintextWorkingKey = ecbDecrypt(ecbEncryptReturn, leftPlaintextZmk);

		// 一、WorkingKey右部分解密 (反3DES)
		/* 1、ECB-DES解密  left key */
		ecbDecryptReturn = ecbDecrypt(rightCiphertextWorkingKey, leftPlaintextZmk);
		/* 2、 ECB-DES加密  right key */
		ecbEncryptReturn = ecbEncrypt(ecbDecryptReturn, rightPlaintextZmk);
		/* 3、 ECB-DES解密  left key */
		byte[] rightPlaintextWorkingKey = ecbDecrypt(ecbEncryptReturn, leftPlaintextZmk);

		byte[] plaintextWorkingKey = ArrayUtils.addAll(leftPlaintextWorkingKey, rightPlaintextWorkingKey);

		return funByteToHexString(plaintextWorkingKey);
	}

	/**
	 * 计算密钥校验值
	 *@param plaintextKey
	 *		明文KEY
	 *@return
	 *		返回校验值
	 **/
	public static String getCheckValueOfKey(String plaintextKey){
		byte[] desData = funStringToBcd("0000000000000000".toCharArray());
		byte[] leftPlaintextKey = funStringToBcd(plaintextKey.substring(0, 16).toCharArray());
		byte[] rightPlaintextKey = funStringToBcd(plaintextKey.substring(16, 32).toCharArray());
		/* 1、ECB-DES加密  left key */
		byte[] ecbEncryptReturn = ecbEncrypt(desData, leftPlaintextKey);

		/* 2、 ECB-DES解密  right key */
		byte[] ecbDecryptReturn = ecbDecrypt(ecbEncryptReturn, rightPlaintextKey);

		/* 3、 ECB-DES加密  left key */
		ecbEncryptReturn = ecbEncrypt(ecbDecryptReturn, leftPlaintextKey);

		return funByteToHexString(ArrayUtils.subarray(ecbEncryptReturn, 0, 3));
	}

	/**
	 * 3DES算法计算MAC
	 *@param macKey
	 *		计算mac的Key(明文)
	 *@param macData
	 *		计算mac的数据域
	 *@return
	 *		返回mac数据
	 **/
	public static String analogMacBy3Des(String macKey, String macData) {
		byte[] inMacKey = funStringToBcd(macKey.toCharArray());
		byte[] inMacData = macData.getBytes();

		if(inMacData.length % 8 != 0){
			int iFillLen = 8 - inMacData.length % 8;
			byte[] bFillData = new byte[iFillLen];
			for(int i = 0; i < iFillLen; i++){
				bFillData[i] = 0x00;
			}
			inMacData = ArrayUtils.addAll(inMacData, bFillData);
		}
		//初始变量
		byte[] ivData = funStringToBcd(ivInfo.toCharArray());
		//left key
		byte[] lKey = ArrayUtils.subarray(inMacKey, 0, 8);
		//right key
		byte[] rKey = ArrayUtils.subarray(inMacKey, 8, 16);

		/* 1、CBC-DES加密  left key */
		byte[] cbcEncryptReturn = cbcEncrypt(inMacData, lKey, ivData);
//		System.out.println(funByteToHexString(cbcEncryptReturn));
		byte[] ecbDecryptData = ArrayUtils.subarray(cbcEncryptReturn, cbcEncryptReturn.length-8, cbcEncryptReturn.length);
//		System.out.println(funByteToHexString(ecbDecryptData));

		/* 2、 ECB-DES解密  right key */
		byte[] ecbDecryptReturn = ecbDecrypt(ecbDecryptData, rKey);
//		System.out.println(funByteToHexString(ecbDecryptReturn));

		/* 3、 ECB-DES加密  left key */
		byte[] ecbEncryptReturn = ecbEncrypt(ecbDecryptReturn, lKey);

		return funByteToHexString(ecbEncryptReturn);
	}

	/**
	 * PIN加密
	 *@param pinKey
	 *		计算pin的Key
	 *@param cardNo
	 *		卡号/主账号
	 *@param pinData
	 *		明文PIN
	 *@return
	 *		返回pin密文
	 **/
	public static String analogPinEncrypt(String pinKey, String cardNo, String pinData){
		byte[] inPinKey = funStringToBcd(pinKey.toCharArray());
		//left key
		byte[] lKey = ArrayUtils.subarray(inPinKey, 0, 8);
		//right key
		byte[] rKey = ArrayUtils.subarray(inPinKey, 8, 16);

		/* xorPin 处理 */
		byte[] xorPin = funStringToBcd(String.format("%02d%-14s", pinData.length(), pinData).replace(' ', 'F').toCharArray());
		/* xorPan 处理*/
		byte[] xorPan = funStringToBcd(String.format("0000%12s", cardNo.substring(cardNo.length()-13, cardNo.length()-1)).toCharArray());
		/* xorData 处理*/
		byte[] xorData = new byte[8];
		for(int i=0; i<8; i++){
			xorData[i] = (byte) (xorPin[i] ^ xorPan[i]);
		}

		/* 1、ECB-DES加密  left key */
		byte[] ecbEncryptReturn = ecbEncrypt(xorData, lKey);
//		System.out.println(funByteToHexString(xorData) + " " + funByteToHexString(lKey));

		/* 2、 ECB-DES解密  right key */
		byte[] ecbDecryptReturn = ecbDecrypt(ecbEncryptReturn, rKey);
//		System.out.println(funByteToHexString(ecbEncryptReturn) + " " + funByteToHexString(rKey));

		/* 3、 ECB-DES加密  left key */
		ecbEncryptReturn = ecbEncrypt(ecbDecryptReturn, lKey);
//		System.out.println(funByteToHexString(ecbDecryptReturn) + " " + funByteToHexString(lKey));

		return funByteToHexString(ecbEncryptReturn);
	}

	public static void main(String[] args){
//		 mac 测试
		String macBuf = analogMacBy3Des("22222222222222222222222222222222", "01028014FFFFFFFFFF0100000000000000000000000000000000000000000041130001010000010000011100010001006666660133000000000100000000000000002020202020202020202020202020202020202020201908021343100000000000000001004519080100009A51A44222DAFDCF9A51A44222DAFDCF32B7723FB3DCDAEB16DAB9A4B3DCDAEB16DAB9A432B7723F0000000000");
		System.out.println(macBuf);

//		 pin 测试
//		String pinBuf = analogPinEncrypt("92BC5DA70DC46E9E8A380E2F85105EF8", "5123160028455874", "369");
//		System.out.println(pinBuf);
//
////		 workKey解密
//		String workkeyBuf = analogDecryptWorkingKey("33333333333234543422222222222222", "CDEECF47CF0EEBE879D7270EAF5036D9");
//		System.out.println(workkeyBuf);
//
////		 计算校验值
//		String chKeyBuf = getCheckValueOfKey("92BC5DA70DC46E9E8A380E2F85105EF8");
//		System.out.println(chKeyBuf);
	}
}
