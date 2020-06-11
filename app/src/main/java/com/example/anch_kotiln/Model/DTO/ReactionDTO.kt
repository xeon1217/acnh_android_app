package com.example.anch_kotiln.Model.DTO

/**
 * 객체 설명 - 감정표현
 *
 * imageIconResource: 아이콘 이미지
 * source: 얻는 경로
 * sourceNote: 얻기 위해 필요한 조건
 */

class ReactionDTO(
    _imageIconResource: String,
    _name: String,
    _source: String,
    _sourceNote: String
) : ObjectDTO(Type.REACTION, _imageIconResource, _name) {
    val source: String = _source
    val sourceNote: String = _sourceNote
}