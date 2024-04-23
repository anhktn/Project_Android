package lab.poly.thi_1704;

import java.util.List;

import lab.poly.thi_1704.models.Thi_1704;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    String BASE_URL = "http://192.168.1.12:3000/";

    @GET("/thi_1704")
    Call<List<Thi_1704>> getListThi_1704();

    @GET("/thi_1704/a/search")
    Call<List<Thi_1704>> searchThi_1704(@Query("hoten_ph36088") String key);

    @Multipart
    @POST("/thi_1704")
    Call<Thi_1704> addThi_1704(@Part("hoten_ph36088") RequestBody hoten_ph36088, @Part("mon_thi_ph36088") RequestBody mon_thi_ph36088,@Part MultipartBody.Part hinh_anh_ph36088, @Part("ngay_thi_ph36088") RequestBody ngay_thi_ph36088, @Part("ca_thi_ph36088") RequestBody ca_thi_ph36088);

    @Multipart
    @PUT("/thi_1704/{id}")
    Call<Thi_1704> updateThi_1704(@Path("id") String id, @Part("hoten_ph36088") RequestBody hoten_ph36088, @Part("mon_thi_ph36088") RequestBody mon_thi_ph36088,@Part MultipartBody.Part hinh_anh_ph36088, @Part("ngay_thi_ph36088") RequestBody ngay_thi_ph36088, @Part("ca_thi_ph36088") RequestBody ca_thi_ph36088);

    @DELETE("/thi_1704/{id}")
    Call<Void> deleteThi_1704(@Path("id") String id);
}
