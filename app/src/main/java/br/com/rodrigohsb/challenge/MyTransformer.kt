package br.com.rodrigohsb.challenge

import br.com.rodrigohsb.challenge.entry.Photo
import br.com.rodrigohsb.challenge.webservice.payload.MyResponseObject


/**
 * @rodrigohsb
 */
class MyTransformer {

    fun convert(myResponseObject: List<MyResponseObject>) : List<Photo> {

        val photos = arrayListOf<Photo>()

        for (item in myResponseObject){

            val id = item.id
            val regular = item.urlsObject.regularImage
            val small = item.urlsObject.smallImage
            photos.add(Photo(id, small, regular))
        }
        return photos
    }
}