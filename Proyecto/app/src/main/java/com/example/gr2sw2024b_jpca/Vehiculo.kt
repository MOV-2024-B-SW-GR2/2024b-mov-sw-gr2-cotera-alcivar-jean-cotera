package org.example

import android.os.Parcel
import android.os.Parcelable

class Vehiculo(
    var id: Int,
    var placa: String,
    var fechaCompra: String,
    var usaDiesel: Boolean,
    var precio: Double
) : Parcelable {

    // Constructor modificado para leer todos los campos del Parcel
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readDouble()
    )

    override fun toString(): String {
        return "🚘 $placa 📅 $fechaCompra 🤑 $precio"
    }

    // Escribe los nuevos campos en el Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(placa)
        parcel.writeString(fechaCompra)
        parcel.writeByte(if (usaDiesel) 1 else 0)
        parcel.writeDouble(precio)
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