package am.vrezh.catworld.api

import am.vrezh.catworld.api.models.Cats
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface CatsApi {

    @GET("")
    fun getCats(): Observable<Cats>
}
