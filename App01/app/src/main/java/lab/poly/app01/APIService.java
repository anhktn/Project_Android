package lab.poly.app01;

import java.util.List;

import lab.poly.app01.models.Model;
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

    //chú ý list sách chưa có gì trong API
    @GET("/api/sach/")
    Call<List<Model>> getList();

    @GET("/api/sach/search")
    Call<List<Model>> search(@Query("ten_ph36088") String key);

    @Multipart
    @POST("/api/sach")
    Call<Model> add( @Part("ten_ph36088") RequestBody ten_ph36088, @Part("the_loai_ph36088") RequestBody the_loai_ph36088, @Part("gia_ph36088") RequestBody gia_ph36088, @Part("so_luong_ban_ph36088") RequestBody so_luong_ban_ph36088, @Part("ngay_ban_ph36088") RequestBody ngay_ban_ph36088, @Part MultipartBody.Part hinh_anh_ph36088);

    @Multipart
    @PUT("/api/sach/{id}")
    Call<Model> update(@Path("id") String id, @Part("ten_ph36088") RequestBody ten_ph36088, @Part("the_loai_ph36088") RequestBody the_loai_ph36088, @Part("gia_ph36088") RequestBody gia_ph36088, @Part("so_luong_ban_ph36088") RequestBody so_luong_ban_ph36088, @Part("ngay_ban_ph36088") RequestBody ngay_ban_ph36088, @Part MultipartBody.Part hinh_anh_ph36088);

    @DELETE("/api/sach/{id}")
    Call<Void> delete(@Path("id") String id);

    //sếp Giảm
    @GET("/api/sach/sortDesc")
    Call<List<Model>> sortDesc();
    @GET("/api/sach/sortAsc")
    Call<List<Model>> sortAsc();
}