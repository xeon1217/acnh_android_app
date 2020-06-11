package com.example.anch_kotiln.Model.DTO

/**
 *
_type: Type,
_name: String,
_imageIconResource: Int,
_tag: Tag,
_sellPrice: Int,
_catalog: Catalog,
_description: String,
_size: String,
_source: String
 */
class ArtDTO(
    _imageGenuineResource: String,
    _imageFakeResource: String,
    _name: String,
    _realArtworkName: String,
    _artist: String,
    //_buyPrice: Int,
    //_sellPrice: Int,
    _description: String,
    _size: String,
    _existFake: Boolean
    //_source: String
) : ItemDTO(
    ObjectDTO.Type.ART,
    "",
    _name,
    ItemDTO.Tag.ART,
    4980,
    //_buyPrice,
    1245,
    //_sellPrice,
    _description,
    Catalog.NOTFORSALE,
    _size,
    "여욱에게서 구입"
) {
    val imageGenuineResource: String = _imageGenuineResource
    val imageFakeResource: String = _imageFakeResource
    val realArtworkName: String = _realArtworkName
    val artist: String = _artist
    val existFake: Boolean = _existFake
}