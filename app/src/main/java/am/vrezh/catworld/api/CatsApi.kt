package am.vrezh.catworld.api

import am.vrezh.catworld.api.models.Cat
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CatsApi {

    @Headers("x-api-key: dd813871-1545-4523-9719-a873b6b1ec3b")
    @GET("images/search")
    fun getCats(
        @Query("size") size: String,
        @Query("order") order: String,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("format") format: String,
    ): Observable<ArrayList<Cat>>

}
