package lab.poly.thiand103_1704;

import java.util.List;

import lab.poly.thiand103_1704.models.Model;
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
    String BASE_URL = "http://10.24.4.104:3000/";

    //chú ý list sách chưa có gì trong API
    @GET("/api/thietbi/")
    Call<List<Model>> getList();

    @GET("/api/thietbi/search")
    Call<List<Model>> search(@Query("ten_ph36088") String key);

    @Multipart
    @POST("/api/thietbi")
    Call<Model> add(@Part("ph36088_ten_thietbi") RequestBody ph36088_ten_thietbi, @Part("ph36088_mota") RequestBody ph36088_mota, @Part MultipartBody.Part ph36088_hinh_anh, @Part("ph36088_ngay_nhap") RequestBody ph36088_ngay_nhap, @Part("ph36088_trang_thai") RequestBody ph36088_trang_thai);

    @Multipart
    @PUT("/api/thietbi/{id}")
    Call<Model> update(@Path("id") String id, @Part("ph36088_ten_thietbi") RequestBody ph36088_ten_thietbi, @Part("ph36088_mota") RequestBody ph36088_mota, @Part MultipartBody.Part ph36088_hinh_anh, @Part("ph36088_ngay_nhap") RequestBody ph36088_ngay_nhap, @Part("ph36088_trang_thai") RequestBody ph36088_trang_thai);

}
