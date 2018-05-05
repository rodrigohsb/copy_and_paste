package br.com.rodrigohsb.challenge

import com.google.gson.annotations.SerializedName

/**
 * @rodrigohsb
 */
data class MyResponseObject (val id: String,
                             @SerializedName("urls")
                             val urlsObject: MyUrlsObject)