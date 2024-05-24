package com.example.hotcue


import android.os.Parcel
import android.os.Parcelable
data class OrientacaoVotos(
    var Titulo: String? = null,
    var Descrição: String? = null,
    var Votos: Int? = null,
    var Timer: Long? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Long::class.java.classLoader) as? Long
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Titulo)
        parcel.writeString(Descrição)
        parcel.writeValue(Votos)
        parcel.writeValue(Timer)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrientacaoVotos> {
        override fun createFromParcel(parcel: Parcel): OrientacaoVotos {
            return OrientacaoVotos(parcel)
        }

        override fun newArray(size: Int): Array<OrientacaoVotos?> {
            return arrayOfNulls(size)
        }
    }
}