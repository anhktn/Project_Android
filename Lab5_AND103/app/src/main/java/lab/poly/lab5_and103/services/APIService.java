package lab.poly.lab5_and103.services;

import java.util.List;

import lab.poly.lab5_and103.model.FlowerModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    public static String BASE_URL = "http://192.168.1.12:3000/";
    @GET("api/flowers")
    Call<List<FlowerModel>> getFlowerList();
    @GET("api/search")
    Call<List<FlowerModel>> searchFlower(@Query("key") String key);

    @POST("api/flowers")
    Call<FlowerModel> addFlower(@Body FlowerModel flowerModel);

    @PUT("api/flowers/{id}")
    Call<FlowerModel> updateFlower(@Path("id") String id, @Body FlowerModel flowerModel);

    @DELETE("api/flowers/{id}")
    Call<Void> deleteFlower(@Path("id") String id);
}
