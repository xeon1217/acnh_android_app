package com.example.anch_kotiln.Utility

import android.os.AsyncTask
import androidx.core.app.ActivityCompat.finishAffinity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

class Common {
    companion object {
        val soundSearcher = SoundSearcher()

        fun exit() {
            System.runFinalization()
            exitProcess(0)
        }

        fun getCurrentTime(): Int {
            val currentTime =
                SimpleDateFormat("HH", Locale.KOREA).format(Calendar.getInstance().time)
            return currentTime.toInt()
        }

        fun getCurrentTimeStr(): String {
            var currentTime = getCurrentTime()
                .toString()
            if (currentTime.toInt() < 10) {
                currentTime = "0$currentTime"
            }
            return currentTime
        }

        fun getCurrentDate(): Int {
            val currentData =
                SimpleDateFormat("MM", Locale.KOREA).format(Calendar.getInstance().time)
            return currentData.toInt()
        }

        fun transStringToBoolean(
            _inputDataLen: Int,
            _inputRawData: String
        ): Pair<BooleanArray, String> {
            var dataTempArr: List<String> = _inputRawData.split(",")
            var inputDataArr = BooleanArray(_inputDataLen)
            var outputProcessData: String = ""
            var pos: Int = 0

            if (inputDataArr.size == 12) { //월
                if (_inputRawData == "13") {
                    outputProcessData = "1년 내내"
                    for (i in inputDataArr.indices) {
                        inputDataArr[i] = true
                    }
                    return Pair(inputDataArr, outputProcessData)
                }
            }

            if (inputDataArr.size == 24) { //시간
                if (_inputRawData == "25") {
                    outputProcessData = "하루종일"
                    for (i in inputDataArr.indices) {
                        inputDataArr[i] = true
                    }
                    return Pair(inputDataArr, outputProcessData)
                }
            }

            while (pos < dataTempArr.size) {
                var dataTempSubArr = dataTempArr.get(pos++).split("-")

                if (dataTempSubArr.size == 1) {
                    inputDataArr[dataTempSubArr.get(0).toInt() - 1] = true
                    if (inputDataArr.size == 12) {
                        if (dataTempSubArr.get(0).toInt() < 10) {
                            outputProcessData = "0${dataTempSubArr.get(0).toInt()}월"
                        } else {
                            outputProcessData = "${dataTempSubArr.get(0).toInt()}월"
                        }
                    }
                    if (inputDataArr.size == 24) {
                        if (dataTempSubArr.get(0).toInt() < 10) {
                            outputProcessData = "0${dataTempSubArr.get(0).toInt()}시"
                        } else {
                            outputProcessData = "${dataTempSubArr.get(0).toInt()}시"
                        }
                    }
                    return Pair(inputDataArr, outputProcessData)
                }

                var start: Int = dataTempSubArr.get(0).toInt() - 1
                var end: Int = dataTempSubArr.get(1).toInt() - 1

                if (start < end) {
                    for (i in (start)..end) {
                        inputDataArr[i] = true
                    }
                }

                if (start > end) {
                    for (i in (start)..(inputDataArr.size - 1)) {
                        inputDataArr[i] = true
                    }

                    for (i in 0..end) {
                        inputDataArr[i] = true
                    }
                }

                if (inputDataArr.size == 12) {
                    if (start < 9) {
                        outputProcessData += "0${start + 1}월 ~ "
                    } else {
                        outputProcessData += "${start + 1}월 ~ "
                    }
                    if (end < 9) {
                        outputProcessData += "0${end + 1}월"
                    } else {
                        outputProcessData += "${end + 1}월"
                    }
                }
                if (inputDataArr.size == 24) {
                    if (start < 9) {
                        outputProcessData += "0${start + 1}시 ~ "
                    } else {
                        outputProcessData += "${start + 1}시 ~ "
                    }
                    if (end < 9) {
                        outputProcessData += "0${end + 1}시"
                    } else {
                        outputProcessData += "${end + 1}시"
                    }
                }
                if (pos < dataTempArr.size) {
                    if (inputDataArr.size == 12) {
                        outputProcessData += ", "
                    }
                    if (inputDataArr.size == 24) {
                        outputProcessData += ", "
                    }
                }
            }
            return Pair(inputDataArr, outputProcessData)
        }
    }

    class SoundSearcher { //출처: https://jhb.kr/122 [JHB의 삽질 이야기]
        private val HANGUL_BEGIN_UNICODE: Char = 44032.toChar()
        private val HANGUL_LAST_UNICODE: Char = 55203.toChar()
        private val HANGUL_BASE_UNIT: Char = 588.toChar()//각자음 마다 가지는 글자수 //자음
        private val INITIAL_SOUND: CharArray = charArrayOf('ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ')

        private fun isInitialSound (searchChar: Char): Boolean {
            INITIAL_SOUND.forEach {
                if(it == searchChar) {
                    return true
                }
            }
            return false
        }

        private fun getInitialSound(c: Char): Char {
            var hangulBegin: Int = (c - HANGUL_BEGIN_UNICODE)
            var pos = hangulBegin.div(HANGUL_BASE_UNIT.toInt())
            return INITIAL_SOUND[pos]
        }

        private fun isHangul(c: Char): Boolean {
            return HANGUL_BEGIN_UNICODE <= c && c <= HANGUL_LAST_UNICODE
        }

        fun matchString(value: String, search: String): Boolean {
            var pos: Int = 0
            var searchOf: Int = value.length - search.length
            var searchLength: Int = search.length

            if(searchOf < 0) { //검색어가 더 길다면 false를 반환
                return false
            }

            for(i in search) {
                pos = 0
                while (pos < searchLength) {
                    if (isInitialSound(search[pos]) && isHangul(value[pos])) { //만약 현재 char이 초성이고 value가 한글이면
                        if (getInitialSound(value[pos]) == search[pos]) { //각각의 초성끼리 같은지 비교한다
                            pos++
                        } else {
                            break
                        }
                    } else { //char이 초성이 아니라면
                        if (value[pos] == search[pos]) { //그냥 같은지 비교한다.
                            pos++
                        } else {
                            break
                        }
                    }
                }
                if (pos == searchLength) return true //모두 일치한 결과를 찾으면 true를 리턴한다.
            }
            return false
        }
    }
}