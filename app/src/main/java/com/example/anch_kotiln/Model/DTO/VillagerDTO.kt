package com.example.anch_kotiln.Model.DTO

import com.example.anch_kotiln.Utility.Common

/**
 * 객체 설명 - 주민
 *
 * imageIconResource: 아이콘 이미지
 * imageFullResource: 풀 사이즈 이미지
 * species: 종
 * personality: 성격
 * birthdayValue: 생일, 검색을 위해 사용됨
 * birthdayText: 생일, 텍스트로 표현하기 위해 사용됨
 * gender: 성별
 * hobby: 취미
 * catchPhrase: 말버릇
 * favoritePhrase: 좋아하는 말
 * favoriteSong: 좋아하는 노래
 */

class VillagerDTO(
    _imageIconResource: String,
    _imageFullResource: String,
    _name: String,
    _personality: Personality,
    _species: Species,
    _gender: Gender,
    _birthdayInput: String,
    _hobby: Hobby,
    _catchPhrase: String,
    _favoritePhrase: String,
    _favoriteSong: String
) : ObjectDTO(Type.VILLAGER, _imageIconResource, _name) {
    enum class Personality(_personality: String, _wakeUpTime: String, _sleepTime: String) { //성격
        LAZE("먹보", "09:00", "23:00"),
        JOCK("운동광", "07:00", "00:00"),
        CRANKY("무뚝뚝", "10:00", "04:00"),
        SMUG("느끼함", "08:30", "02:00"),
        SWEET("친절함", "06:00", "00:00"),
        PEPPY("아이돌", "09:00", "01:00"),
        SNOOTY("성숙함", "09:30", "02:00"),
        BIGSISTER("단순활발", "11:00", "03:00"),
        ALL("모든 성격", "", "");

        private var personality: String = _personality
        private var wakeUpTime: String = _wakeUpTime
        private var sleepTime: String = _sleepTime
        override fun toString(): String {
            return personality
        }

        fun getWakeTime(): String {
            return wakeUpTime
        }

        fun sleepTime(): String {
            return sleepTime
        }
    }

    enum class Species(_species: String) { //종
        DOG("개"),
        FROG("개구리"),
        ANTEATER("개미핥기"),
        GORILLA("고릴라"),
        CAT("고양이"),
        BEAR("곰"),
        LITTLE_BEAR("꼬마곰"),
        A_WOLF("늑대"),
        SQUIRREL("다람쥐"),
        CHICKEN("닭"),
        EAGLE("독수리"),
        PIG("돼지"),
        WORDS("말"),
        OCTOPUS("문어"),
        DEER("사슴"),
        LION("사자"),
        BIRD("새"),
        MOUSE("생쥐"),
        SMALL("소"),
        CROCODILE("악어"),
        AMOUNT("양"),
        GOAT("염소"),
        DUCK("오리"),
        MONKEY("원숭이"),
        KANGAROO("캥거루"),
        ELEPHANT("코끼리"),
        RHINOCEROS("코뿔소"),
        KOALA("코알라"),
        OSTRICH("타조"),
        RABBIT("토끼"),
        PENGUIN("펭귄"),
        HIPPO("하마"),
        HAMSTER("햄스터"),
        TIGER("호랑이");

        private var species: String = _species
        override fun toString(): String {
            return species
        }
    }

    enum class Gender(_gender: String) {
        MALE("♂"),
        FEMALE("♀"),
        UNKNOWN("알 수 없음");

        private val gender: String = _gender
        override fun toString(): String {
            return gender
        }
    }

    enum class Hobby(_hobby: String) {
        MUSIC("음악"),
        NATURE("자연"),
        PLAY("놀이"),
        EDUCATION("교육"),
        FASHION("패션"),
        FITNESS("운동");

        private val hobby: String = _hobby
        override fun toString(): String {
            return hobby
        }
    }

    val imageFullResource: String = _imageFullResource
    val personality: Personality = _personality
    val species: Species = _species
    val birthdayValue: BooleanArray = Common.transStringToBoolean(12, _birthdayInput.split("/")[0]).first
    val birthdayText: String = Common.transStringToBoolean(12, _birthdayInput.split("/")[0]).second + " " + _birthdayInput.split("/")[1] + "일"
    val gender: Gender = _gender
    val hobby: Hobby = _hobby
    val catchPhrase: String = _catchPhrase
    val favoritePhrase: String = _favoritePhrase
    val favoriteSong: String = _favoriteSong
}