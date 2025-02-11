package org.example

import android.os.Parcel
import android.os.Parcelable

class Vehiculo(
    var id: Int,
    var nombre: String,
    var fechaFundacion: String,
    var esActiva: Boolean,
    var ingresosAnuales: Double,
    var latitud: Double,       // Nuevo campo
    var longitud: Double      // Nuevo campo
) : Parcelable {

    // Constructor modificado para leer todos los campos del Parcel
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readDouble(),
        parcel.readDouble(), // Lee latitud
        parcel.readDouble()   // Lee longitud
    )

    override fun toString(): String {
        return "🚘 $nombre 📅 $fechaFundacion 🤑 $ingresosAnuales"
    }

    // Escribe los nuevos campos en el Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeString(fechaFundacion)
        parcel.writeByte(if (esActiva) 1 else 0)
        parcel.writeDouble(ingresosAnuales)
        parcel.writeDouble(latitud)   // Escribe latitud
        parcel.writeDouble(longitud)  // Escribe longitud
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Vehiculo> {
        override fun createFromParcel(parcel: Parcel): Vehiculo {
            return Vehiculo(parcel)
        }

        override fun newArray(size: Int): Array<Vehiculo?> {
            return arrayOfNulls(size)
        }
    }
}