
import com.decard.uilibs.md.InfoBean
import com.decard.uilibs.md.ReturnBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * File Description
 * @author Dell
 * @date 2018/9/11
 *
 */
interface RetrofitService {


    @GET("api/4/news/latest")
    fun getDatas(): Observable<ReturnBean>

    @GET("api/4/news/{id}")
    fun getInfo(@Path("id") id: String): Observable<InfoBean>
}