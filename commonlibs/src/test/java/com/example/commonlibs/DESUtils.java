package com.example.commonlibs;

import com.example.commonlibs.utils.HexUtils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * des，3des 加解密
 */
public class DESUtils {

	private static final String KEY_ALGORITHM_3DES = "DESede";
	private static final String CIPHER_ALGORITHM_3DES = "DESede/ECB/NoPadding";


	private static final String KEY_ALGORITHM_DES = "DES";
	private static final String CIPHER_ALGORITHM_DES = "DES/ECB/NoPadding";


	/**
	 * DES加密
	 *
	 * @param data     需要加密的数据
	 * @param password 密钥
	 * @return
	 */
	public static byte[] desEncrypt(byte[] data, byte[] password) {

		try {
			SecureRandom random = new SecureRandom();
			DESKeySpec keySpec = new DESKeySpec(password);
			//创建一个密钥工厂，用来将DESedeKeySpec进行转换
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM_DES);
			SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);

			//使用Cipher进行实际的加密过程
			Cipher des = Cipher.getInstance(CIPHER_ALGORITHM_DES);
			des.init(Cipher.ENCRYPT_MODE, secretKey, random);
			//进行加密操作
			return des.doFinal(data);

		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return null;
	}


	/**
	 * DES解密，将数据还原到DES加密的状态
	 *
	 * @param data     需要解密的数据
	 * @param password 密钥
	 * @return
	 */
	public static byte[] desDecrypt(byte[] data, byte[] password) {
		try {
			//安全随机数
			SecureRandom random = new SecureRandom();
			DESKeySpec keySpec = new DESKeySpec(password);
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM_DES);
			SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
			Cipher des = Cipher.getInstance(CIPHER_ALGORITHM_DES);
			des.init(Cipher.DECRYPT_MODE, secretKey, random);
			return des.doFinal(data);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return null;
	}


	/**
	 * 双倍长des加密
	 * ***************************双倍长DES加密中，密钥有32个字节。
	 * <p>
	 * 步骤如下：
	 * <p>
	 * 1、使用密钥的前16字节，对数据DATA进行加密，得到加密的结果TMP1；
	 * <p>
	 * 2、使用密钥的后16字节，对第一的计算结果TMP1，进行解密，得到解密的结果TMP2；
	 * <p>
	 * 3、再次使用密钥的前16字节，对第二次的计算结果TMP2，进行加密，得到加密的结果DEST。DEST就为最终的结果。
	 *
	 * @return 加密后数据
	 * @throws Exception
	 */
	public static byte[] doubleDesEncrypt(String data, String password) throws Exception {


		//使用密钥的前16字节，对数据DATA进行加密，得到加密的结果TMP1;
		byte[] temp1 = desEncrypt(HexUtils.hexStringToBytes(data),
				HexUtils.hexStringToBytes(password.substring(0, 16)));
		//使用密钥的后16字节，对第一的计算结果TMP1，进行解密，得到解密的结果TMP2
		byte[] temp2 = desDecrypt(temp1,
				HexUtils.hexStringToBytes(password.substring(16, 32)));
		//再次使用密钥的前16字节，对第二次的计算结果TMP2，进行加密，得到加密的结果DEST。DEST就为最终的结果
		byte[] dest = desEncrypt(temp2,
				HexUtils.hexStringToBytes(password.substring(0, 16)));
		return dest;
	}


	/**
	 * 3DES加密
	 *
	 * @param data     待加密数据
	 * @param password 密钥 24字节
	 * @return
	 */
	public static byte[] tribleDesEncrypt_ECB(byte[] data, byte[] password) {
		try {
			SecretKey secretKey = new SecretKeySpec(password, KEY_ALGORITHM_3DES);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_3DES);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return cipher.doFinal(data);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return null;
	}

	/**
	 * 3DES解密
	 *
	 * @param data     需要进行解密的原始数据
	 * @param password 密钥 24字节
	 * @return
	 */
	public static byte[] des3Decrypt_ECB(byte[] data, byte[] password) {
		try {
			SecretKey secretKey = new SecretKeySpec(password, KEY_ALGORITHM_3DES);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_3DES);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return cipher.doFinal(data);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return null;
	}


	/**
	 * 3DES CBC 加密
	 *
	 * @param iv       初始向量
	 * @param data     需要进行解密的原始数据
	 * @param password 密钥 24字节
	 * @return
	 */
	public static byte[] des3Encrypt_CBC(byte[] iv, byte[] data, byte[] password) {
		try {
			IvParameterSpec param = new IvParameterSpec(iv);
			SecretKey secretKey = new SecretKeySpec(password, KEY_ALGORITHM_3DES);
			Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, param);
			return cipher.doFinal(data);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return null;
	}


}
