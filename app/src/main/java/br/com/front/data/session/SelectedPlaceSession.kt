package br.com.front.data.session

import br.com.front.data.model.nearby.NearbyPlaceResponse

object SelectedPlaceSession {
    var place: NearbyPlaceResponse? = null

    fun clear() {
        place = null
    }
}
