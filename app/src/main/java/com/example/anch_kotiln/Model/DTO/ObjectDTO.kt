package com.example.anch_kotiln.Model.DTO

import java.io.Serializable

/**
 * 객체 설명 - DTO 최상위 객체
 *
 * type: 타입
 * name: 이름
 */

open class ObjectDTO(_type: Type, _imageIconResource: String, _name: String) : Serializable {
    enum class Type {
        VILLAGER("주민"),
        INSECT("곤충"),
        FISH("물고기"),
        ART("그림"),
        FOSSIL("화석"),
        ITEM("아이템"),
        REACTION("리액션");

        private var type: String

        constructor(_type: String) {
            type = _type
        }
    }

    val type: Type = _type
    val name: String = _name
    val imageIconResource: String = _imageIconResource
}