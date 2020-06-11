package com.example.anch_kotiln.Model.DTO

/**
 * 객체 설명 - 화석
 */
class FossilDTO(
    _name: String,
    _imageIconResource: String,
    _tag: Tag,
    _sellPrice: Int,
    _description: String,
    _size: String
) : ItemDTO(
    Type.FOSSIL,
    _imageIconResource,
    _name,
    _tag,
    0,
    _sellPrice,
    _description,
    Catalog.NOTFORSALE,
    _size,
    "땅파기로 얻음"
) {

}