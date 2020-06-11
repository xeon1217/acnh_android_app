package com.example.anch_kotiln.Model.DTO

/**
 * 객체 설명 - 물고기
 *
 * shadow: 출현할 때 그림자 크기
 */

class FishDTO(
    _imageIconResource: String,
    _imageCritterpediaResource: String,
    _imageFurnitureResource: String,
    _name: String,
    _where: Where,
    //_how: How,
    _weather: Weather,
    _dateInput: String,
    _timeInput: String,
    _sellPrice: Int,
    _shadow: Shadow,
    _fin: Boolean,
    _description: String,
    _size: String
) : CreatureDTO(
    Type.FISH,
    _imageIconResource,
    _imageCritterpediaResource,
    _imageFurnitureResource,
    _name,
    _where,
    //_how,
    _weather,
    _dateInput,
    _timeInput,
    Tag.FISH,
    _sellPrice,
    Catalog.NOTFORSALE,
    _description,
    _size,
    "낚시를 통해"
) {
    enum class Shadow(_size: String) { //크기
        SMALL("작음"),
        LITTLE_SMALL("약간 작음"),
        MEDIUM("중간"),
        LITTLE_BIG("약간 큼"),
        BIG("큼"),
        VERYBIG("아주 큼"),
        THIN("가늘음");

        private var shadow: String = _size
        override fun toString(): String {
            return shadow
        }
    }

    val shadow: Shadow = _shadow
    val fin: Boolean = _fin
}