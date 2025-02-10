package org.example

import android.os.Parcel
import android.os.Parcelable

class Proyecto(
    var id: Int,
    var nombre: String,
    var descripcion: String,
    var presupuesto: Double,
    var empresaId: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readInt()
    )

    override fun toString(): String {
        return "🚀 $nombre 🤑 $presupuesto"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeString(descripcion)
        parcel.writeDouble(presupuesto)
        parcel.writeInt(empresaId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Proyecto> {
        override fun createFromParcel(parcel: Parcel): Proyecto {
            return Proyecto(parcel)
        }

        override fun newArray(size: Int): Array<Proyecto?> {
            return arrayOfNulls(size)
        }
    }
}