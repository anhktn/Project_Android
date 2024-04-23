package lab.poly.thiand103_1704;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import lab.poly.thiand103_1704.adapter.ModelAdapter;
import lab.poly.thiand103_1704.models.Model;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity implements IClick{
    File file = null;
    RecyclerView rccModel;
    List<Model> listData;
    ModelAdapter modelAdapter;
    Button btnAdd;
    ImageView img_add;
    ImageView img_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        rccModel = findViewById(R.id.rccModel);
        btnAdd = findViewById(R.id.btnAdd);
        EditText edtSearch = findViewById(R.id.edtSearch);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rccModel.setLayoutManager(layoutManager);
        listData = new ArrayList<>();
        modelAdapter = new ModelAdapter(this, listData, this);
        rccModel.setAdapter(modelAdapter);

        getListData();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAdd();
            }
        });
    }

    private void getListData() {
        Gson gson = new GsonBuilder().setLenient().create();

        //khởi tạo retrofit Clinet
        Retrofit retrofit = new Retrofit.Builder().baseUrl(APIService.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        //tạo interface
        APIService apiService = retrofit.create(APIService.class);
        //tạo đối tượng Call
        Call<List<Model>> objCall = apiService.getList();
        objCall.enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                if (response.isSuccessful()) {
                    listData.clear();
                    listData.addAll(response.body());
                    Log.d("Home", listData.toString());
                    modelAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {

                Log.e("Home", t.getMessage());
            }
        });
    }

    @Override
    public void onChiTietClick(int position) {
        CT(listData.get(position));
    }
    private void chooseImageAdd() {
        if (ContextCompat.checkSelfPermission(Home.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            getImageAdd.launch(intent);

        } else {
            ActivityCompat.requestPermissions(Home.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    //Hàm chọn hình
    private void chooseImageUpdate() {
        if (ContextCompat.checkSelfPermission(Home.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            getImageUpdate.launch(intent);

        } else {
            ActivityCompat.requestPermissions(Home.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    //hàm kết quả sau khi lấy hình
    ActivityResultLauncher<Intent> getImageAdd = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == RESULT_OK) {
                        Uri path = o.getData().getData();
                        file = createFileFormUri(path, "ph36088_hinh_anh");
                        //Glide để load hình
                        Glide.with(Home.this).load(file).thumbnail(Glide.with(Home.this).load(R.drawable.ing)).centerCrop()//ceter cắt ảnh
                                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(img_add);
                    }
                }
            }
    );
    ActivityResultLauncher<Intent> getImageUpdate = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == RESULT_OK) {
                        Uri path = o.getData().getData();
                        file = createFileFormUri(path, "ph36088_hinh_anh");
                        //Glide để load hình
                        Glide.with(Home.this).load(file).thumbnail(Glide.with(Home.this).load(R.drawable.ing)).centerCrop()//ceter cắt ảnh
                                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(img_up);
                    }
                }
            }
    );

    //hàm tạo file hình từ uri
    private File createFileFormUri(Uri path, String name) {
        File _file = new File(Home.this.getCacheDir(), name + ".png");
        try {
            InputStream in = Home.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onEditClick(int position) {
        Model model = listData.get(position);
        if (position != RecyclerView.NO_POSITION) {
            // Gọi phương thức xóa của Adapter khi người dùng chọn xóa
            showDialogUpdate(model);
            Toast.makeText(Home.this, "Cập nhật", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialogUpdate(Model model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);

        // Inflate layout cho dialog
        LayoutInflater Inflater = LayoutInflater.from(this);
        View dialogView = Inflater.from(this).inflate(R.layout.dialog_up, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

       TextInputEditText edtTen = dialogView.findViewById(R.id.edtTen);
        TextInputEditText edtMoTa = dialogView.findViewById(R.id.edtMoTa);
        TextInputEditText edtNgayNgap = dialogView.findViewById(R.id.edtNgayNhap);
        TextInputEditText edtTrangThai = dialogView.findViewById(R.id.edtTrangThai);



        Button btnUpdate = dialogView.findViewById(R.id.btnAdd_up);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel_up);
        TextInputLayout errTen = dialogView.findViewById(R.id.errTen);
        TextInputLayout errMoTa = dialogView.findViewById(R.id.errMoTa);
        TextInputLayout errNgayNgap = dialogView.findViewById(R.id.errNgayNgap);
        TextInputLayout errTrangThai = dialogView.findViewById(R.id.errTrangThai);

        img_up = dialogView.findViewById(R.id.img_up);

        edtTen.setText(model.getPh36088_ten_thietbi());
        edtMoTa.setText(model.getPh36088_mota());
        edtNgayNgap.setText("" + model.getPh36088_ngay_nhap());
        edtTrangThai.setText("" + model.getPh36088_trang_thai());


        if (model.getPh36088_hinh_anh() == null) {
            Glide.with(this).load(R.drawable.man).into(img_up);
        } else {
            Glide.with(this).load(model.getPh36088_hinh_anh()).into(img_up);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        img_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImageUpdate();
            }
        });
        // Xử lý sự kiện khi nhấn nút cập nhật trong dialog
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            //validate

            @Override
            public void onClick(View view) {
                String ten = edtTen.getText().toString();
                String mota = edtMoTa.getText().toString();
                String ngaynhap = edtNgayNgap.getText().toString();
                String trangthai = edtTrangThai.getText().toString();


                if (ten.isEmpty() && mota.isEmpty() && ngaynhap.isEmpty() && trangthai.isEmpty() ) {
                    errTen.setError("Không bỏ trống thông tin");
                   errMoTa.setError("Không bỏ trống tin");
                    errNgayNgap.setError("Không bỏ trống tin");
                    errTrangThai.setError("Không bỏ trống tin");

                    return;
                }
                if (ten.isEmpty()) {
                    errTen.setError("Không bỏ trống thông tin");
                    return;
                } else {
                    errTen.setError(null);
                }
                if (mota.isEmpty()) {
                    errMoTa.setError("Không bỏ trống thông tin");
                    return;
                } else {
                    errMoTa.setError(null);
                }
                if (trangthai.isEmpty()) {
                    errTrangThai.setError("Không bỏ trống thông tin");
                    return;
                } else {
                    errTrangThai.setError(null);
                }
                if (ngaynhap.isEmpty()) {
                    errNgayNgap.setError("Không bỏ trống thông tin");
                    return;
                } else {
                    errNgayNgap.setError(null);
                }


                if (!trangthai.isEmpty()) {
                    try {
                        int g = Integer.parseInt(trangthai);
                    } catch (NumberFormatException e) {
                        errTrangThai.setError("Giá phải là số");
                        return;
                    }
                }

                //validate ngày
                String regex = "^(?:19|20)\\d{2}/(?:0[1-9]|1[0-2])/(?:0[1-9]|[12][0-9]|3[01])$";

                if (!ngaynhap.matches(regex)) {
                    errNgayNgap.setError("Ngày bán phải là YYYY/MM/DD vd: 2024/04/17");
                    return;
                }

                RequestBody tenReq = RequestBody.create(MediaType.parse("multipart/form-data"), ten);
                RequestBody motar = RequestBody.create(MediaType.parse("multipart/form-data"), mota);
                RequestBody nn = RequestBody.create(MediaType.parse("multipart/form-data"), ngaynhap);
                RequestBody tt = RequestBody.create(MediaType.parse("multipart/form-data"), trangthai);


                MultipartBody.Part multipartBody;
                if (file != null) {
                    RequestBody requesrFile = RequestBody.create(MediaType.parse("image/*"), file);
                    multipartBody = MultipartBody.Part.createFormData("hinh_anh_ph36088", file.getName(), requesrFile);
                    //"image" là cùng tên với key trong mutipart
                } else {
                    multipartBody = null;
                }

                Retrofit retrofit = new Retrofit.Builder().baseUrl(APIService.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                APIService apiService = retrofit.create(APIService.class);
                Call<Model> call = apiService.update(model.get_id(), tenReq,motar, multipartBody, nn, tt);

                call.enqueue(new Callback<Model>() {
                    @Override
                    public void onResponse(Call<Model> call, Response<Model> response) {
                        if (response.isSuccessful()) {
                            // Cập nhật thành công
                            Call<List<Model>> call1 = apiService.getList();
                            call1.enqueue(new Callback<List<Model>>() {
                                @Override
                                public void onResponse(Call<List<Model>> call1, Response<List<Model>> response) {
                                    if (response.isSuccessful()) {
                                        List<Model> fetchedFlowers = response.body();
                                        if (fetchedFlowers != null) {
                                            listData.clear(); // Xóa dữ liệu cũ
                                            listData.addAll(fetchedFlowers); // Thêm dữ liệu mới
                                            modelAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                                            alertDialog.dismiss();
                                            file = null;
                                        }
                                    } else {
                                        Log.e("Home", "Failed to get flower: " + response.message());
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Model>> call, Throwable t) {
                                    Log.e("Home", t.getMessage());
                                }
                            });

                        } else {
                            Toast.makeText(Home.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Model> call, Throwable t) {
                        // Xử lý khi gữp lỗi trong quá trình gửi yêu cầu
                        Log.e("Update flower", "Error updating flower: " + t.getMessage());
                    }
                });
            }
        });
        // Hiển thị dialog
        alertDialog.show();

    }
    private void showDialogAdd() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add, null);
        dialogBuilder.setView(dialogView);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        TextInputEditText edtTen = dialogView.findViewById(R.id.edtTen);
        TextInputEditText edtMoTa = dialogView.findViewById(R.id.edtMoTa);
        TextInputEditText edtNgayNgap = dialogView.findViewById(R.id.edtNgayNhap);
        TextInputEditText edtTrangThai = dialogView.findViewById(R.id.edtSLBan);

        Button btnUpdate = dialogView.findViewById(R.id.btnAdd_add);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel_add);
        TextInputLayout errTen = dialogView.findViewById(R.id.errTen);
        TextInputLayout errMoTa = dialogView.findViewById(R.id.errMoTa);
        TextInputLayout errNgayNgap = dialogView.findViewById(R.id.errNgayNgap);
        TextInputLayout errTrangThai = dialogView.findViewById(R.id.errTrangThai);

        img_add = dialogView.findViewById(R.id.img_add);

        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImageAdd();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ten = edtTen.getText().toString();
                String mota = edtMoTa.getText().toString();
                String ngaynhap = edtNgayNgap.getText().toString();
                String trangthai = edtTrangThai.getText().toString();


                if (ten.isEmpty() && mota.isEmpty() && ngaynhap.isEmpty() && trangthai.isEmpty() ) {
                    errTen.setError("Không bỏ trống thông tin");
                    errMoTa.setError("Không bỏ trống tin");
                    errNgayNgap.setError("Không bỏ trống tin");
                    errTrangThai.setError("Không bỏ trống tin");

                    return;
                }
                if (ten.isEmpty()) {
                    errTen.setError("Không bỏ trống thông tin");
                    return;
                } else {
                    errTen.setError(null);
                }
                if (mota.isEmpty()) {
                    errMoTa.setError("Không bỏ trống thông tin");
                    return;
                } else {
                    errMoTa.setError(null);
                }
                if (trangthai.isEmpty()) {
                    errTrangThai.setError("Không bỏ trống thông tin");
                    return;
                } else {
                    errTrangThai.setError(null);
                }
                if (ngaynhap.isEmpty()) {
                    errNgayNgap.setError("Không bỏ trống thông tin");
                    return;
                } else {
                    errNgayNgap.setError(null);
                }


                if (!trangthai.isEmpty()) {
                    try {
                        int g = Integer.parseInt(trangthai);
                    } catch (NumberFormatException e) {
                        errTrangThai.setError("Giá phải là số");
                        return;
                    }
                }

                //validate ngày
                String regex = "^(?:19|20)\\d{2}/(?:0[1-9]|1[0-2])/(?:0[1-9]|[12][0-9]|3[01])$";

                if (!ngaynhap.matches(regex)) {
                    errNgayNgap.setError("Ngày bán phải là YYYY/MM/DD vd: 2024/04/17");
                    return;
                }

                RequestBody tenReq = RequestBody.create(MediaType.parse("multipart/form-data"), ten);
                RequestBody motar = RequestBody.create(MediaType.parse("multipart/form-data"), mota);
                RequestBody nn = RequestBody.create(MediaType.parse("multipart/form-data"), ngaynhap);
                RequestBody tt = RequestBody.create(MediaType.parse("multipart/form-data"), trangthai);

                MultipartBody.Part multipartBody;

                if (file != null) {
                    RequestBody requesrFile = RequestBody.create(MediaType.parse("image/*"), file);
                    multipartBody = MultipartBody.Part.createFormData("hinh_anh_ph36088", file.getName(), requesrFile);
                    //"avatar" là cùng tên với key trong mutipart
                } else {
                    multipartBody = null;
                }
                Retrofit retrofit = new Retrofit.Builder().baseUrl(APIService.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                APIService apiService = retrofit.create(APIService.class);
                Call<Model> call = apiService.add(tenReq, motar, multipartBody, nn, tt);
                call.enqueue(new Callback<Model>() {
                    @Override
                    public void onResponse(Call<Model> call, Response<Model> response) {
                        if (response.isSuccessful()) {
                            Log.d("Add", "Add success");
                            getListData();
                            file = null;
                            alertDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Model> call, Throwable t) {
                        Log.d("Add", "Add fail");
                    }
                });
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
    private void CT(Model model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);

        // Inflate layout cho dialog
        LayoutInflater Inflater = LayoutInflater.from(this);
        View dialogView = Inflater.from(this).inflate(R.layout.ct, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextInputEditText edtTen = dialogView.findViewById(R.id.edtTen);
        TextInputEditText edtMoTa = dialogView.findViewById(R.id.edtMoTa);
        TextInputEditText edtNgayNgap = dialogView.findViewById(R.id.edtNgayNhap);
        TextInputEditText edtTrangThai = dialogView.findViewById(R.id.edtTrangThai);

        TextInputLayout errTen = dialogView.findViewById(R.id.errTen);
        TextInputLayout errMoTa = dialogView.findViewById(R.id.errMoTa);
        TextInputLayout errNgayNgap = dialogView.findViewById(R.id.errNgayNgap);
        TextInputLayout errTrangThai = dialogView.findViewById(R.id.errTrangThai);

        img_up = dialogView.findViewById(R.id.img_up);

        edtTen.setText(model.getPh36088_ten_thietbi());
        edtMoTa.setText(model.getPh36088_mota());
        edtNgayNgap.setText("" + model.getPh36088_ngay_nhap());
        edtTrangThai.setText("" + model.getPh36088_trang_thai());


        if (model.getPh36088_hinh_anh() == null) {
            Glide.with(this).load(R.drawable.man).into(img_up);
        } else {
            Glide.with(this).load(model.getPh36088_hinh_anh()).into(img_up);
        }

        // Hiển thị dialog
        alertDialog.show();

    }
}