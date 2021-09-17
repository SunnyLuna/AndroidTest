package com.decard.mqttlibs.emqtt

/**
 * 人员数据字段
 * @author ZJ
 * created at 2020/9/22 16:37
 * status 1 正常  2：被禁用   3：未签约
 */
class UserInfoEntity {
    var card_id: String = ""
    var name: String = ""
    var uid: String = ""
    var status: String = ""
    override fun toString(): String {
        return "UserInfoEntity(card_id='$card_id', name='$name', uid='$uid', status='$status')"
    }
}