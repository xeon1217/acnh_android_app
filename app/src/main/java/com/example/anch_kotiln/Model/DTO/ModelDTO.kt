package com.example.anch_kotiln.Model.DTO

import com.example.anch_kotiln.Utility.Common

/**
 * 객체 설명 - 어댑터와 바인딩하기 위해 쓰여지는 객체
 * title - 어댑터의 카테고리로 쓰일 이름
 * mData - 데이터가 들어가있는 객체 배열, 바인딩 이후 형변환을 통해 데이터가 쓰이기 위함
 */

class ModelDTO (_data: ArrayList<ObjectDTO>) {
    var title: String = ""
    val mData: ArrayList<ObjectDTO> = _data

    init{
        if(mData.size > 0) {
            when(mData[0].type) {
                ObjectDTO.Type.VILLAGER -> title = (mData[0] as VillagerDTO).species.toString()
                ObjectDTO.Type.ART -> {
                    title = if((mData[0] as ArtDTO).existFake) {
                        "가품이 존재하는 미술품"
                    } else {
                        "진품만 존재하는 미술품"
                    }
                }
                ObjectDTO.Type.FOSSIL -> title = (mData[0] as FossilDTO).tag.toString()
                ObjectDTO.Type.FISH -> title = (mData[0] as FishDTO).tag.toString()
                ObjectDTO.Type.INSECT -> title = (mData[0] as InsectDTO).tag.toString()
                ObjectDTO.Type.REACTION -> title = "리액션"
            }
        }
    }

    fun filter(constraint: CharSequence?) : ArrayList<ObjectDTO> {
        val result: ArrayList<ObjectDTO> = ArrayList()
        mData.forEach{
            if(Common.soundSearcher.matchString(it.name, constraint.toString()) || it.name.toUpperCase().contains(constraint.toString().toUpperCase())) {
                result.add(it)
            }
        }
        return result
    }
}