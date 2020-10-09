package com.example.commonlibs.tvl



import com.example.commonlibs.utils.HexUtils


object TlvDealUtils {

    fun createParamAlisTLV(): String {
        return StringBuilder().append(buildAlias(2, HexUtils.toHexString("shop_num".toByteArray())))
            .append(buildAlias(3, HexUtils.toHexString("url".toByteArray())))
            .append(buildAlias(4, HexUtils.toHexString("organization_id".toByteArray())))
            .append(buildAlias(5, HexUtils.toHexString("log_project_num".toByteArray())))
            .append(buildAlias(6, HexUtils.toHexString("log_key".toByteArray())))
            .toString()
    }

    private fun buildAlias(id: Int, parameterHex: String?): String? {
        return if (parameterHex != null && !parameterHex.isEmpty()) {
            java.lang.StringBuilder().append(String.format("%04d", id))
                .append(String.format("%02X", parameterHex.length / 2))
                .append(parameterHex)
                .toString()
        } else ""
    }

}