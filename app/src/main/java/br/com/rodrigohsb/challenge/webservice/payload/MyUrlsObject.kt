package br.com.rodrigohsb.challenge.webservice.payload

import com.google.gson.annotations.SerializedName

/**
 * @rodrigohsb
 */
data class MyUrlsObject (@SerializedName("small")val smallImage: String,
                         @SerializedName("regular") val regularImage: String)