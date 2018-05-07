package br.com.rodrigohsb.challenge.entry

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @rodrigohsb
 */
@Parcelize
data class Photo(val id: String, val smallUrl: String, val regularUrl: String): Parcelable