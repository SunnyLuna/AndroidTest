package com.example.commonlibs;

import com.example.commonlibs.utils.ByteUtils;
import com.example.commonlibs.utils.HexUtils;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


	@Test
	public void encryptData() {
		String version = "01";//报文版本
		String encryptType = "02";//报文加密类型
		String type = "8014";//报文类型
		String flow = "FFFFFFFFFF";//报文唯一流水
		String machine_manufacturer = "0100";//机具厂商
		String ItemId = "00000000000000000000";//ItemId(产品型号
		String supplierID = "00000000000000000000";//SupplierID（供应商）
		String acquirerNumber = "41130001";//收单机构编号
		String branchNumber = "01000001";//分公司编号
		String lineNumber = "00000111";//线路编号
		String carNumber = "00010001";//车辆编号
		String driverNumber = "00666666";//司机编号
		String signStatus = "01";//司机签到状态(00:表示未签到或已签退 01:已签到)
		String deviceNumber = "330000000001";//设备编号
		String psamNumber = "0000000000000000";//PSAM卡编号
		String iccNumber = "2020202020202020202020202020202020202020";//流量卡ICCID号
		String dataTransmissionTime = "20190802134310";//数据传输时间
		String endFlag = "00";//数据传输结束标志
		String responseCode = "00";//交易应答码
		String reservedDomain = "0000000000";//保留域
		MessageHeadBean messageHeadBean = new MessageHeadBean(version, encryptType, type, flow,
				machine_manufacturer, ItemId, supplierID, acquirerNumber, branchNumber, lineNumber
				, carNumber, driverNumber, signStatus, deviceNumber, psamNumber, iccNumber,
				dataTransmissionTime, endFlag, responseCode, reservedDomain);
		String head = messageHeadBean.getMessageHead();
		System.out.println("组装的报文头信息: " + head);

		String encryptionScheme = "01";//加密方案
		String length = "0045";//报文长度
		String terminalKeyVersion = "1908010000";//终端密钥版本
		String mak0 = "22222222222222222222222222222222";
		String mak1 = "F40379AB9E0EC533F40379AB9E0EC533";
		String key = "0000000000000000";
		String enk0 = "11111111111111111111111111111111";
		String enk1 = "F40379AB9E0EC533F40379AB9E0EC533";

		//计算加密密钥  ENK0加密ENK1
		byte[] enkBytes = DESUtils.desEncrypt(HexUtils.hexStringToBytes(enk1),
				HexUtils.hexStringToBytes(enk0));
		String enkSecretKey = HexUtils.toHexString(enkBytes);
		System.out.println("计算加密密钥  ENK0加密ENK1: " + enkSecretKey);
		//计算ENK1 的 CHECKVALUE  32B7723F
		byte[] enkCheckValueBytes =
				DESUtils.desEncrypt(HexUtils.hexStringToBytes(key),
						HexUtils.hexStringToBytes(mak1));
		byte[] enkData = new byte[4];
		System.arraycopy(enkCheckValueBytes, 0, enkData, 0, 4);
		String enkCheckValue = HexUtils.toHexString(enkData);
		System.out.println("enkCheckValue: " + enkCheckValue);

		//计算加密密钥   MAK0 加 密 MAK1
		byte[] makBytes = DESUtils.desEncrypt(HexUtils.hexStringToBytes(mak1),
				HexUtils.hexStringToBytes(mak0));
		String makSecretKey = HexUtils.toHexString(makBytes);
		System.out.println("计算加密密钥  MAK0 加 密 MAK1: " + makSecretKey);
		//计算mak1 的 CHECKVALUE  32B7723F
		byte[] makCheckValueBytes =
				DESUtils.desEncrypt(HexUtils.hexStringToBytes(key),
						HexUtils.hexStringToBytes(mak1));
		byte[] makData = new byte[4];
		System.arraycopy(makCheckValueBytes, 0, makData, 0, 4);
		String makCheckValue = HexUtils.toHexString(makData);
		System.out.println("makCheckValue: " + makCheckValue);
		ActiveKeyBean activeKeyBean = new ActiveKeyBean(encryptionScheme, length,
				terminalKeyVersion, enkSecretKey, enkCheckValue, makSecretKey, makCheckValue,
				"0000000000");
		String message = activeKeyBean.getMessage();
		System.out.println("组装的报文体信息: " + message);
		//设备编号和流量卡号异或得到初始因子
		String init = getInit(deviceNumber, iccNumber);
		System.out.println("初始因子: " + init);
	}

	private String getInit(String deviceNumber, String iccNumber) {
		byte[] deviceBytes = HexUtils.hexStringToBytes(deviceNumber);
		byte[] iccBytes = HexUtils.hexStringToBytes(iccNumber);
		deviceBytes = addZero(deviceBytes, 8, true);
		System.out.println("deviceNumber: " + HexUtils.toHexString(deviceBytes));
		byte[] icc = new byte[8];
		System.arraycopy(iccBytes, iccBytes.length - 8, icc, 0, 8);
		System.out.println("iccNumber: " + HexUtils.toHexString(icc));
		byte[] bytes = ByteUtils.bytesXOR(deviceBytes, icc);
		return HexUtils.toHexString(bytes);
	}

	/**
	 * @param deviceBytes 原数组
	 * @param bytes       满足的字节长度
	 * @param isFront     是否左补0
	 * @return
	 */
	@NotNull
	private byte[] addZero(byte[] deviceBytes, int bytes, Boolean isFront) {
		if (deviceBytes.length % bytes != 0) {
			int iFillLen = bytes - deviceBytes.length % bytes;
			byte[] bFillData = new byte[iFillLen];
			for (int i = 0; i < iFillLen; i++) {
				bFillData[i] = 0x00;
			}
			if (isFront) {
				deviceBytes = ByteUtils.concatAll(bFillData, deviceBytes);
			} else {
				deviceBytes = ByteUtils.concatAll(deviceBytes, bFillData);
			}
		}
		return deviceBytes;
	}

	@Test
	public void checkValue() {
		String version = "01";//报文版本
		String encryptType = "02";//报文加密类型
		String type = "8014";//报文类型
		String flow = "FFFFFFFFFF";//报文唯一流水
		String machine_manufacturer = "0100";//机具厂商
		String ItemId = "00000000000000000000";//ItemId(产品型号
		String supplierID = "00000000000000000000";//SupplierID（供应商）
		String acquirerNumber = "41130001";//收单机构编号
		String branchNumber = "01000001";//分公司编号
		String lineNumber = "00000111";//线路编号
		String carNumber = "00010001";//车辆编号
		String driverNumber = "00666666";//司机编号
		String signStatus = "01";//司机签到状态(00:表示未签到或已签退 01:已签到)
		String deviceNumber = "330000000001";//设备编号
		String psamNumber = "0000000000000000";//PSAM卡编号
		String iccNumber = "2020202020202020202020202020202020202020";//流量卡ICCID号
		String dataTransmissionTime = "20190802134310";//数据传输时间
		String endFlag = "00";//数据传输结束标志
		String responseCode = "00";//交易应答码
		String reservedDomain = "0000000000";//保留域

		String head =
				version + encryptType + type + flow + machine_manufacturer + ItemId + supplierID
						+ acquirerNumber + branchNumber + lineNumber + carNumber + driverNumber
						+ signStatus + deviceNumber + psamNumber + iccNumber + dataTransmissionTime
						+ endFlag + responseCode + reservedDomain;
		System.out.println("head: " + head);
		String encryptionScheme = "01";//加密方案
		String length = "0045";//报文长度
		String terminalKeyVersion = "1908010000";//终端密钥版本
		String mak0 = "22222222222222222222222222222222";
		String key = "0000000000000000";
		String enk0 = "11111111111111111111111111111111";
		String mak1 = "F40379AB9E0EC533F40379AB9E0EC533";
		String enk1 = "F40379AB9E0EC533F40379AB9E0EC533";
		//计算MAK1、ENK1 的 CHECKVALUE  32B7723F
		byte[] bytes = DESUtils.desEncrypt(HexUtils.hexStringToBytes(key),
				HexUtils.hexStringToBytes(mak1));
		byte[] data = new byte[4];
		System.arraycopy(bytes, 0, data, 0, 4);
		String s = HexUtils.toHexString(data);
		System.out.println(s);
		//mak0加密mak1   B3DCDAEB16DAB9A4B3DCDAEB16DAB9A4
		byte[] bytes1 = DESUtils.desEncrypt(HexUtils.hexStringToBytes(mak1),
				HexUtils.hexStringToBytes(mak0));
		String mak = HexUtils.toHexString(bytes1);
		System.out.println(mak);
		//使用ENK0加密ENK1得到：9A51A44222DAFDCF9A51A44222DAFDCF
		byte[] bytes2 = DESUtils.desEncrypt(HexUtils.hexStringToBytes(enk1),
				HexUtils.hexStringToBytes(enk0));
		String enk = HexUtils.toHexString(bytes2);
		System.out.println(enk);

		String body = encryptionScheme + length + terminalKeyVersion + enk
				+ s + mak + s + reservedDomain;
		System.out.println("body: " + body);
	}

	@Test
	public void cal() {
		String test = "0000330000000001";
		String test1 = "2020202020202020";
		byte[] bytes = ByteUtils.bytesXOR(HexUtils.hexStringToBytes(test),
				HexUtils.hexStringToBytes(test1));
		System.out.println(HexUtils.toHexString(bytes));
	}

	@Test
	public void change() {
//		String dataStr =
//				"00A101028014FFFFFFFFFF0100000000000000000000000000000000000000000041130001010000010000011100010001006666660133000000000100000000000000002020202020202020202020202020202020202020201908021343100000000000000001004519080100009A51A44222DAFDCF9A51A44222DAFDCF32B7723FB3DCDAEB16DAB9A4B3DCDAEB16DAB9A432B7723F00000000004B0BB5A8E72AC4F1";
		String dataStr =
				"01028015ffffffffff010000000000000000000000000000000000000000004113000101000001000001110001000100666666013300000000010000000000000000202020202020202020202020202020202020202020190802134310000000000000000100451908010000b9d959191bd4a2d2dba7a5064e456b7ffb9ace20b9d959191bd4a2d22d729c8bdd488a5abef48ae70000000000";
		System.out.println("原始数据:  " + dataStr);
		byte[] bytes = HexUtils.hexStringToBytes(dataStr, false);
		System.out.println("转换数据:  " + HexUtils.toHexString(bytes, false));

	}


	@Test
	public void calDCMac() {
		//德卡数据
//		String key = "20201119185806919663444505138032";
//		String dataStr =
//				"01028014060918423181102019060402697172000020190521100000000000000003070000000000000200000000000000000000000000041273000000000000000030303030303030303030303030303030303030302021060918423101000000000000010045FFFFFFFFFF45E1A2CE2692F4ACB8A7B7022FA5312ECAE7E24B37D733EFD28A2B0E56CB726E3FDF213E96DB006BFFFFFFFFFF";
//		String initialFactor = "3030303030342243";

		//测试数据
		String key = "22222222222222222222222222222222";
//		String dataStr =
//				"01028014FFFFFFFFFF0100000000000000000000000000000000000000000041130001010000010000011100010001006666660133000000000100000000000000002020202020202020202020202020202020202020201908021343100000000000000001004519080100009A51A44222DAFDCF9A51A44222DAFDCF32B7723FB3DCDAEB16DAB9A4B3DCDAEB16DAB9A432B7723F0000000000";
		String dataStr =
				"01028015ffffffffff010000000000000000000000000000000000000000004113000101000001000001110001000100666666013300000000010000000000000000202020202020202020202020202020202020202020190802134310000000000000000100451908010000b9d959191bd4a2d2dba7a5064e456b7ffb9ace20b9d959191bd4a2d22d729c8bdd488a5abef48ae70000000000";
		String initialFactor = "2020132020202021";

		byte[] dataBytes = HexUtils.hexStringToBytes(dataStr);
		System.out.println("转换:  " + HexUtils.toHexString(dataBytes));
		byte[] bytes = addZero(dataBytes, 8, false);
		System.out.println("强制补0x80再用0x00补齐8的倍数得到:  " + HexUtils.toHexString(bytes));
		byte[] keyBytes = HexUtils.hexStringToBytes(key);
		byte[] k8 = new byte[8];
		System.arraycopy(keyBytes, 0, k8, 0, 8);
		System.out.println("key的前8个字节:  " + HexUtils.toHexString(k8));
		//初始向量
		byte[] initBytes = HexUtils.hexStringToBytes(initialFactor);
		System.out.println("初始向量:  " + HexUtils.toHexString(initBytes));
		byte[] data = new byte[8];
		for (int i = 0; i < bytes.length / 8; i++) {
			System.arraycopy(bytes, i * 8, data, 0, 8);
			byte[] bytesXOR = ByteUtils.bytesXOR(initBytes, data);
			System.out.println(HexUtils.toHexString(initBytes) + " 异或: " + HexUtils.toHexString(data) + ":  " + HexUtils.toHexString(bytesXOR));
			if (i == bytes.length / 8 - 1) {
				byte[] bytes1 = ByteUtils.concatAll(keyBytes, k8);
				initBytes = DESUtils.tribleDesEncrypt_ECB(bytesXOR, bytes1);
				System.out.println(HexUtils.toHexString(bytes1) + " 加密: " + HexUtils.toHexString(bytesXOR) + ":  " + HexUtils.toHexString(initBytes));
			} else {
				initBytes = DESUtils.desEncrypt(bytesXOR, k8);
				System.out.println(HexUtils.toHexString(k8) + " 加密: " + HexUtils.toHexString(bytesXOR) + ":  " + HexUtils.toHexString(initBytes));
			}
		}


	}
}