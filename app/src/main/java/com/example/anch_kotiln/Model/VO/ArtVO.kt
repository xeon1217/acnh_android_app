package com.example.anch_kotiln.Model.VO

data class ArtVO (
    var korName: String,
    var engName: String,
    var existFake: Boolean,
    var category: String,
    var buyPrice: String,
    var sellPrice: String,
    var size: String,
    var realArtworkTitle: String,
    var artist: String,
    var fakeDescription: String,
    var museumDescription: String,
    var versionAdded: String,
    var versionUnlocked: String,
    var interact: Boolean,
    var tag: String,
    var catalog: String
)