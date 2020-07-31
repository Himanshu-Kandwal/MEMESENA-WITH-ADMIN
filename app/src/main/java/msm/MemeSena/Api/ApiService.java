package msm.MemeSena.Api;

import com.google.gson.JsonObject;

import io.reactivex.Single;
import retrofit2.http.POST;

public interface ApiService {

    @POST("pack.php")
    Single<JsonObject> getAllStickers();
}