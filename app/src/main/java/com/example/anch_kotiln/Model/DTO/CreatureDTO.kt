package com.example.anch_kotiln.Model.DTO

import com.example.anch_kotiln.Utility.Common

/**
 * 객체 설명 - 생물 관련(물고기/곤충)
 *
 * imageFullResource: 풀 사이즈 이미지
 * imageFurnitureResource: 배치할 경우의 이미지
 * where: 출현 장소
 * how: 잡는 방법
 * weather: 출현하는 날씨
 * dateValue: 출현 기간, 검색을 위해 사용됨
 * dateText: 출현 기간, 텍스트로 표현하기 위해 사용됨
 * timValue: 출현 시간, 검색을 위해 사용됨
 * timeText: 출현 시간, 텍스트로 표현하기 위해 사용됨
 */

open class CreatureDTO(
    _type: Type,
    _imageIconResource: String,
    _imageCritterpediaResource: String,
    _imageFurnitureResource: String,
    _name: String,
    _where: Where,
    _weather: Weather,
    _dateInput: String,
    _timeInput: String,
    _tag: Tag,
    _sellPrice: Int,
    _catalog: Catalog,
    _description: String,
    _size: String,
    _source: String
) : ItemDTO(
    _type,
    _imageIconResource,
    _name,
    _tag,
    0,
    _sellPrice,
    _description,
    _catalog,
    _size,
    _source
) {
    enum class Where(_where: String) {
        ANYWHERE("아무데나"),
        FIELD("들판"),
        GALBAGE("바닥에 둔 쓰레기"),
        DECAY_DAIKON("바닥에 둔 썩은 무"), // 바닥에 둔 썩은 무,
        RESIDENT_HEAD("가려운 주민 머리"), // 가려운 주민 머리,
        SURROUNDING_LIGHT("빛 주변"), // 빛 주변,
        SNOW_BALL("눈덩이"),
        SHAKE_A_TREE("나무 흔들기"), // 나무 흔들기,
        STUMP("그루터기"), // 그루터기,
        PARMTREE("야자기둥"), // 야자기둥,
        TREE("나무기둥"), // 나무기둥,
        UNDER_ROCK("바위 밑"), // 바위 밑,
        UNDER_TREE("나무 밑"), // 나무 밑
        UNDER_GROUND("땅 속"),
        ON_THE_FLOWER("꽃 위"), // 꽃 위,
        ON_THE_WHITE_FLOWER("흰 꽃 위"), // 흰 꽃 위,
        SURROUNDING_FLOWER("꽃 주변"), // 꽃 주변,
        SURROUNDING_HYBIRD_FLOWER("교배 꽃 주변"), // 교배 꽃 주변,
        BEACH_ROCK("해변바위"),// 해변바위,
        RAIN_ROCK("비가 오는 날의 바위"), // 비오는 날 바위,
        RAIN_SEA("비가 오는 날의 바다"), // 비오는 날 바다,
        REVER("강"), // 강,
        POND("연못"), // 연못,
        REVER_AND_POND("강/연못"), //강/연못
        BLUFF_RIVER("절벽 위 강"), // 절벽 위 강,
        BLUFF_POND("절벽 위 연못"), // 절벽 위 연못,
        BLUFF_REVER_AND_POND("절벽 위 강, 연못"),
        REVER_MONUTH("강 하구"), // 강 하구
        SEA("바다"), // 바다,
        DOCK("부두"), // 부두,
        BEACH("해변");

        private var where: String = _where
        override fun toString(): String {
            return where
        }
    }

    enum class How(how: String) {
    }

    enum class Weather(_weather: String) {
        ANY_WEATHER(""),
        ANY_EXCEPT_RAIN("(비 X)"),
        RAIN_ONLY("(비 O)");

        private var weather: String = _weather
        override fun toString(): String {
            return weather
        }
    }

    val imageCritterpediaResource: String = _imageCritterpediaResource
    val imageFurnitureResource: String = _imageFurnitureResource
    val where: Where = _where
    //val how: How = _how
    val weather: Weather = _weather
    val dataValue: BooleanArray = Common.transStringToBoolean(12, _dateInput).first
    val dateText: String = Common.transStringToBoolean(12, _dateInput).second
    val timeValue: BooleanArray = Common.transStringToBoolean(24, _timeInput).first
    val timeText: String = Common.transStringToBoolean(24, _timeInput).second
}