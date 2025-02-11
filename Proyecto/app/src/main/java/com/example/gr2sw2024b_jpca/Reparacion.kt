package org.example

import android.os.Parcel
import android.os.Parcelable

class Reparacion(
    var id: Int,
    var titulo: String,
    var descripcion: String,
    var costo: Double,
    var vehiculoId: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readInt()
    )

    override fun toString(): String {
        return "🔩 $nombre 🤑 $presupuesto"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(titulo)
        parcel.writeString(descripcion)
        parcel.writeDouble(costo)
        parcel.writeInt(vehiculoId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Reparacion> {
        override fun createFromParcel(parcel: Parcel): Reparacion {
            return Reparacion(parcel)
        }

        override fun newArray(size: Int): Array<Reparacion?> {
            return arrayOfNulls(size)
        }
    }
}