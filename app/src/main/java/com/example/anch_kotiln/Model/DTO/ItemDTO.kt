package com.example.anch_kotiln.Model.DTO

/**
 * 객체 설명 - 아이템 객체
 *
 * 이미지
 * 판매가격
 * 사이즈(배치시)
 *
 */

open class ItemDTO(
    _type: Type,
    _imageIconResource: String,
    _name: String,
    _tag: Tag,
    _buyPrice: Int,
    _sellPrice: Int,
    _description: String,
    _catalog: Catalog,
    _size: String,
    _source: String
) : ObjectDTO(_type, _imageIconResource, _name) {
    enum class Tag(_tag: String) {
        INSECT("곤충"),
        FISH("물고기"),
        ART(""),
        FOSSIL("");

        val tag: String = _tag
        override fun toString(): String {
            return tag
        }
    }
    enum class Catalog(_catalog: String) {
        FORSALE("판매품"),
        NOTFORSALE("비매품");

        val catalog: String = _catalog
        override fun toString(): String {
            return catalog
        }
    }
    val tag: Tag = _tag
    val buyPrice: Int = _buyPrice
    val sellPrice: Int = _sellPrice
    val catalog: Catalog = _catalog
    val description: String = _description
    val size: String = _size
    val source: String = _source
}