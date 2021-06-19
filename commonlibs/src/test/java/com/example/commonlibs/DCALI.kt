package com.example.commonlibs

import com.example.commonlibs.utils.ByteUtils
import com.example.commonlibs.utils.HexUtils
import org.junit.Test

class DCALI {
    /**
     * @param deviceBytes 原数组
     * @param bytes       满足的字节长度
     * @param isFront     是否左补0
     * @return
     */
    private fun addZero(deviceBytes: ByteArray, bytes: Int, isFront: Boolean): ByteArray {
        var deviceBytes = deviceBytes
        if (deviceBytes.size % bytes != 0) {
            val iFillLen = bytes - deviceBytes.size % bytes
            val bFillData = ByteArray(iFillLen)
            for (i in 0 until iFillLen) {
                bFillData[i] = 0x00
            }
            deviceBytes = if (isFront) {
                ByteUtils.concatAll(bFillData, deviceBytes)
            } else {
                ByteUtils.concatAll(deviceBytes, bFillData)
            }
        }
        return deviceBytes
    }

    @Test
    fun deal8014() {
        //德卡数据
//		String key = "20201119185806919663444505138032";
//		String dataStr =
//				"01028014060918423181102019060402697172000020190521100000000000000003070000000000000200000000000000000000000000041273000000000000000030303030303030303030303030303030303030302021060918423101000000000000010045FFFFFFFFFF45E1A2CE2692F4ACB8A7B7022FA5312ECAE7E24B37D733EFD28A2B0E56CB726E3FDF213E96DB006BFFFFFFFFFF";
//		String initialFactor = "3030303030342243";

        //测试数据
        val key = "22222222222222222222222222222222"
        var dataStr =
            "01028014FFFFFFFFFF0100000000000000000000000000000000000000000041130001010000010000011100010001006666660133000000000100000000000000002020202020202020202020202020202020202020201908021343100000000000000001004519080100009A51A44222DAFDCF9A51A44222DAFDCF32B7723FB3DCDAEB16DAB9A4B3DCDAEB16DAB9A432B7723F0000000000"
        val initialFactor = "2020132020202021"
        calDCMac(key, dataStr, initialFactor, true)
    }

    @Test
    fun deal8015() {
        val data =
            "00a101028015ffffffffff010000000000000000000000000000000000000000004113000101000001000001110001000100666666013300000000010000000000000000202020202020202020202020202020202020202020190802134310000000000000000100451908010000b9d959191bd4a2d2dba7a5064e456b7ffb9ace20b9d959191bd4a2d22d729c8bdd488a5abef48ae70000000000769513bd0d1050eb"
        val mak0 = "22222222222222222222222222222222"
        val initialFactor = "2020132020202021"
        val oldMac = data.substring(data.length - 16, data.length)
        println("返回的mac值：$oldMac")
        val calData = data.substring(4, data.length - 16)
        println("参与计算的数据：$calData")
        val calMac = calDCMac(mak0, calData, initialFactor, false)
        println("计算的mac值：$calMac")

        val enk2 = data.substring(220, 252)
        println("enk2的值：$enk2")
        val enkValue = data.substring(252, 260)
        println("enkValue的值：$enkValue")
        val mak2 = data.substring(260, 292)
        println("mak2的值：$mak2")
        val makValue = data.substring(292, 300)
        println("makValue的值：$makValue")

        val mak1 = "F40379AB9E0EC533F40379AB9E0EC533"
        val enk1 = "F40379AB9E0EC533F40379AB9E0EC533"
        val makDecrypt = DESUtils.desDecrypt(
            HexUtils.hexStringToBytes(mak2, false),
            HexUtils.hexStringToBytes(mak1)
        )
        println("MAK1解密MAK2的密文得到明文,MAK2：${HexUtils.toHexString(makDecrypt)}")
        val enkDecrypt = DESUtils.desDecrypt(
            HexUtils.hexStringToBytes(enk2, false),
            HexUtils.hexStringToBytes(enk1)
        )
        println("MAK1解密MAK2的密文得到明文,ENK2：${HexUtils.toHexString(enkDecrypt)}")
        val key = "0000000000000000"
        //计算mak1 的 CHECKVALUE  32B7723F
        val makCheckValueBytes = DESUtils.doubleDesEncrypt(
            key,
            HexUtils.toHexString(makDecrypt)
        )
        val makData = ByteArray(4)
        System.arraycopy(makCheckValueBytes, 0, makData, 0, 4)
        val makCheckValue = HexUtils.toHexString(makData,false)
        println("makCheckValue: $makCheckValue")
    }

    @Test
    fun checkValue() {
        val key = "0000000000000000"
        val mak1 = "20190802134310847188138650003368"
        //计算mak1 的 CHECKVALUE  32B7723F
        val doubleDesString = DESUtils.doubleDesEncrypt(key, mak1)
        val makData = ByteArray(4)
        System.arraycopy(doubleDesString, 0, makData, 0, 4)
        val makCheckValue = HexUtils.toHexString(makData)
        println("makCheckValue: $makCheckValue")
    }

    fun calDCMac(SecretKey: String, data: String, initialFactor: String, isUpper: Boolean): String {
        val dataStr = data + "80"
        val dataBytes = HexUtils.hexStringToBytes(dataStr, isUpper)
        println("添加80后得到:  " + HexUtils.toHexString(dataBytes, isUpper))
        val bytes: ByteArray = addZero(dataBytes, 8, false)
        println("强制补0x80再用0x00补齐8的倍数得到:  " + HexUtils.toHexString(bytes, isUpper))
        val keyBytes = HexUtils.hexStringToBytes(SecretKey, isUpper)
        val k8 = ByteArray(8)
        System.arraycopy(keyBytes, 0, k8, 0, 8)
        println("key的前8个字节:  " + HexUtils.toHexString(k8, isUpper))
        //初始向量
        var initBytes = HexUtils.hexStringToBytes(initialFactor, isUpper)
        println("初始向量:  " + HexUtils.toHexString(initBytes, isUpper))
        val data = ByteArray(8)
        for (i in 0 until bytes.size / 8) {
            System.arraycopy(bytes, i * 8, data, 0, 8)
            val bytesXOR = ByteUtils.bytesXOR(initBytes, data)
            println(
                HexUtils.toHexString(initBytes, isUpper) + " 异或: " + HexUtils.toHexString(
                    data,
                    isUpper
                ) + ":  " + HexUtils.toHexString(
                    bytesXOR, isUpper
                )
            )
            if (i == bytes.size / 8 - 1) {
                val bytes1 = ByteUtils.concatAll(keyBytes, k8)
                initBytes = DESUtils.tribleDesEncrypt_ECB(bytesXOR, bytes1)
                println(
                    HexUtils.toHexString(bytes1, isUpper) + " 加密: " + HexUtils.toHexString(
                        bytesXOR,
                        isUpper
                    ) + ":  " + HexUtils.toHexString(
                        initBytes, isUpper
                    )
                )
            } else {
                initBytes = DESUtils.desEncrypt(bytesXOR, k8)
                println(
                    HexUtils.toHexString(k8, isUpper) + " 加密: " + HexUtils.toHexString(
                        bytesXOR,
                        isUpper
                    ) + ":  " + HexUtils.toHexString(
                        initBytes, isUpper
                    )
                )
            }
        }
        return HexUtils.toHexString(initBytes, isUpper)
    }
}