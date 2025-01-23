package org.example

import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDate

class Empresa(
    var id: Int,
    var nombre: String,
    var fechaFundacion: String,
    var esActiva: Boolean,
    var ingresosAnuales: Double
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readDouble()
    )

    override fun toString(): String {
        return "🏠 $nombre 📅 $fechaFundacion 🤑 $ingresosAnuales"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeString(fechaFundacion)
        parcel.writeByte(if (esActiva) 1 else 0)
        parcel.writeDouble(ingresosAnuales)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Empresa> {
        override fun createFromParcel(parcel: Parcel): Empresa {
            return Empresa(parcel)
        }

        override fun newArray(size: Int): Array<Empresa?> {
            return arrayOfNulls(size)
        }
    }
}