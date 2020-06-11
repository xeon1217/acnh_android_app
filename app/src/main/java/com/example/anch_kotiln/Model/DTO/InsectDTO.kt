package com.example.anch_kotiln.Model.DTO

class InsectDTO(
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
    _description: String,
    _size: String
) : CreatureDTO(
    Type.INSECT,
    _imageIconResource,
    _imageCritterpediaResource,
    _imageFurnitureResource,
    _name,
    _where,
    //_how,
    _weather,
    _dateInput,
    _timeInput,
    Tag.INSECT,
    _sellPrice,
    Catalog.NOTFORSALE,
    _description,
    _size,
    "채집을 통해"
) {
}