package lab.poly.thi_1704;

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
import android.text.Editable;
import android.text.TextWatcher;
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

import lab.poly.thi_1704.adapter.ThiAdapter;
import lab.poly.thi_1704.models.Thi_1704;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity implements IClick {
    File file;
    RecyclerView rccThi;
    List<Thi_1704> listThi;
    ThiAdapter thiAdapter;
    Button btnAdd;
    ImageView imgThi_add;
    ImageView imgThi_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rccThi = findViewById(R.id.rccThi);
        btnAdd = findViewById(R.id.btnAdd);
        EditText edtSearch = findViewById(R.id.edtSearch);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rccThi.setLayoutManager(layoutManager);
        listThi = new ArrayList<>();
        thiAdapter = new ThiAdapter(this, listThi, this);
        rccThi.setAdapter(thiAdapter);

        getListThi();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAdd();
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String key = charSequence.toString();
                if (key.length() == 0 && key.contains("")) {
                    getListThi();
                }
                if (!key.isEmpty()) {
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(APIService.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                    APIService apiService = retrofit.create(APIService.class);
                    Call<List<Thi_1704>> call = apiService.searchThi_1704(key);
                    call.enqueue(new Callback<List<Thi_1704>>() {
                        @Override
                        public void onResponse(Call<List<Thi_1704>> call, Response<List<Thi_1704>> response) {
                            if (response.isSuccessful()) {
                                listThi.clear();
                                listThi.addAll(response.body());

                                thiAdapter = new ThiAdapter(Home.this, listThi, Home.this);
                                rccThi.setAdapter(thiAdapter);

                                Log.d("Home", "Search successful");
                            }
                        }
                        @Override
                        public void onFailure(Call<List<Thi_1704>> call, Throwable t) {

                            Log.e("Home", t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onEditClick(int position) {
        Thi_1704 thi_1704= listThi.get(position);
        if (position != RecyclerView.NO_POSITION) {
            // Gọi phương thức xóa của Adapter khi người dùng chọn xóa
            showUpdateDialog(thi_1704);
            Toast.makeText(Home.this, "Cập nhật", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeleteClick(int position) {
        //xác nhận xóa
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn chắc chắn muốn xóa?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(APIService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Log.d("Delete Fruit", "Deleting fruit at position: " + position);
            //tạo interface
            APIService apiService = retrofit.create(APIService.class);
            // Gọi phương thức Retrofit để thực hiện yêu cầu DELETE tới máy chủ
            Thi_1704 thiDel = listThi.get(position);
            Call<Void> call = apiService.deleteThi_1704(thiDel.get_id());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Xóa hoa khỏi danh sách và cập nhật RecyclerView
                        listThi.remove(position);
                        thiAdapter.notifyDataSetChanged();
                        Toast.makeText(Home.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        // Xử lý khi nhận được phản hồi lỗi từ máy chủ
                        Log.e("Delete", "Failed to delete: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // Xử lý khi gặp lỗi trong quá trình gửi yêu cầu
                    Log.e("Delete", "Error deleting: " + t.getMessage());
                }
            });
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {
            dialogInterface.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getListThi() {
        Gson gson = new GsonBuilder().setLenient().create();

        //khởi tạo retrofit Clinet
        Retrofit retrofit = new Retrofit.Builder().baseUrl(APIService.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        //tạo interface
        APIService apiService = retrofit.create(APIService.class);
        //tạo đối tượng Call
        Call<List<Thi_1704>> objCall = apiService.getListThi_1704();
        objCall.enqueue(new Callback<List<Thi_1704>>() {
            @Override
            public void onResponse(Call<List<Thi_1704>> call, Response<List<Thi_1704>> response) {
                if (response.isSuccessful()) {
                    listThi.clear();
                    listThi.addAll(response.body());
                    Log.d("Home", listThi.toString());
                    thiAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Thi_1704>> call, Throwable t) {

                Log.e("Home", t.getMessage());
            }
        });
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
                        file = createFileFormUri(path, "hinh_anh_ph36088");
                        //Glide để load hình
                        Glide.with(Home.this).load(file).thumbnail(Glide.with(Home.this).load(R.drawable.ing)).centerCrop()//ceter cắt ảnh
                                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imgThi_add);
                    }
                }
            }
    );
    ActivityResultLauncher<Intent> getImageUpdate = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == RESULT_OK) {
                        Uri path = o.getData().getData();
                        file = createFileFormUri(path, "hinh_anh_ph36088");
                        //Glide để load hình
                        Glide.with(Home.this).load(file).thumbnail(Glide.with(Home.this).load(R.drawable.ing)).centerCrop()//ceter cắt ảnh
                                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imgThi_up);
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
    private void showDialogAdd() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add, null);
        dialogBuilder.setView(dialogView);


        TextInputEditText edtHoTen = dialogView.findViewById(R.id.edtHoTen);
        TextInputEditText edtMonThi = dialogView.findViewById(R.id.edtMonThi);
        TextInputEditText edtNgayThi = dialogView.findViewById(R.id.edtNgayThi);
        TextInputEditText edtCaThi = dialogView.findViewById(R.id.edtCaThi);
        Button btnAdd = dialogView.findViewById(R.id.btnAdd_add);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel_add);
        AlertDialog alertDialog = dialogBuilder.create();
        TextInputLayout errHoTen = dialogView.findViewById(R.id.errHoTen);
        TextInputLayout errMonThi = dialogView.findViewById(R.id.errMonThi);
        TextInputLayout errNgayThi = dialogView.findViewById(R.id.errNgayThi);
        TextInputLayout errCaThi = dialogView.findViewById(R.id.errCaThi);
        imgThi_add = dialogView.findViewById(R.id.imgThi_add);

        imgThi_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImageAdd();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hoten = edtHoTen.getText().toString();
                String monthi = edtMonThi.getText().toString();
                String ngaythi = edtNgayThi.getText().toString();
                String cathi = edtCaThi.getText().toString();

                if (hoten.isEmpty() && monthi.isEmpty() && ngaythi.isEmpty() && cathi.isEmpty()) {
                    errHoTen.setError("Không bỏ trống thông tin");
                    errMonThi.setError("Không bỏ trống thông tin");
                    errNgayThi.setError("Không bỏ trống thông tin");
                    errCaThi.setError("Không bỏ trống thông tin");
                    return;
                }
                if (hoten.isEmpty()) {
                    errHoTen.setError("Không bỏ trống thông tin");
                    return;
                } else {
                    errHoTen.setError(null);
                }
                if(monthi.isEmpty()){
                    errMonThi.setError("Không b<boost trọng này");
                    return;
                }else {
                    errMonThi.setError(null);
                }
                if(ngaythi.isEmpty()){
                    errNgayThi.setError("Không b<boost trọng này");
                    return;
                }else {
                    errNgayThi.setError(null);
                }
                if(cathi.isEmpty()){
                    errCaThi.setError("Không b<boost trọng này");
                    return;
                }else {
                    errCaThi.setError(null);
                }

                //giá phải là số
                if (!cathi.isEmpty()) {
                    try {
                        Double ct = Double.parseDouble(cathi);
                    } catch (NumberFormatException e) {
                        errCaThi.setError("Ca thi phải là số");
                        return;
                    }
                }


                RequestBody hotenReq = RequestBody.create(MediaType.parse("multipart/form-data"), hoten);
                RequestBody monthiReq = RequestBody.create(MediaType.parse("multipart/form-data"), monthi);
                RequestBody ngaythiReq = RequestBody.create(MediaType.parse("multipart/form-data"), ngaythi);
                RequestBody cathiReq = RequestBody.create(MediaType.parse("multipart/form-data"), cathi);
                MultipartBody.Part multipartBody = null;

                if (file != null) {
                    RequestBody requesrFile = RequestBody.create(MediaType.parse("image/*"), file);
                    multipartBody = MultipartBody.Part.createFormData("hinh_anh_ph36088", file.getName(), requesrFile);
                    //"avatar" là cùng tên với key trong mutipart
                } else {
                    multipartBody = null;
                }
                Retrofit retrofit = new Retrofit.Builder().baseUrl(APIService.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                APIService apiService = retrofit.create(APIService.class);
                Call<Thi_1704> call = apiService.addThi_1704(hotenReq, monthiReq, multipartBody, ngaythiReq, cathiReq);
                call.enqueue(new Callback<Thi_1704>() {
                    @Override
                    public void onResponse(Call<Thi_1704> call, Response<Thi_1704> response) {
                        if (response.isSuccessful()) {
                            Log.d("Add", "Add success");
                            getListThi();
                            alertDialog.dismiss();
                            file = null;
                            alertDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Thi_1704> call, Throwable t) {
                        Log.d("Add", "Add fail");
                    }
                });
            }
        });
        alertDialog.show();
    }

    private void showUpdateDialog(Thi_1704 thi) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);

        // Inflate layout cho dialog
        LayoutInflater Inflater = LayoutInflater.from(this);
        View view = Inflater.from(this).inflate(R.layout.dialog_update, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        // Khởi tạo EditText và gán giá trị hiện tại của sản phẩm vào
        TextInputEditText edtHoTen = view.findViewById(R.id.edtName_up);
        TextInputEditText edtMonThi = view.findViewById(R.id.edtMonThi_up);
        TextInputEditText edtNgayThi = view.findViewById(R.id.edtNgayThi_up);
        TextInputEditText edtCaThi = view.findViewById(R.id.edtCaThi_up);
        Button btnCancel = view.findViewById(R.id.btnCancel_up);
        TextInputLayout errHoTen = view.findViewById(R.id.errHoTen_up);
        TextInputLayout errMonThi = view.findViewById(R.id.errMonThi_up);
        TextInputLayout errNgayThi = view.findViewById(R.id.errNgayThi_up);
        TextInputLayout errCaThi = view.findViewById(R.id.errCaThi_up);
        imgThi_up = view.findViewById(R.id.imgThi_up);
        Button btnUp = view.findViewById(R.id.btnUpdate);


        AlertDialog alertDialog = builder.create();


        edtHoTen.setText(thi.getHoten_ph36088());
        edtMonThi.setText( thi.getMon_thi_ph36088());
        edtCaThi.setText("" + thi.getCa_thi_ph36088());
        edtNgayThi.setText(thi.getNgay_thi_ph36088());

        if(thi.getHinh_anh_ph36088() != null) {
            Glide.with(this).load(R.drawable.man).into(imgThi_up);
        }else {
            Glide.with(this).load(thi.getHinh_anh_ph36088()).into(imgThi_up);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        imgThi_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImageUpdate();
            }
        });
        // Xử lý sự kiện khi nhấn nút cập nhật trong dialog
        btnUp.setOnClickListener(new View.OnClickListener() {
            //validate

            @Override
            public void onClick(View view) {
                String hoten = edtHoTen.getText().toString();
                String monthi = edtMonThi.getText().toString();
                String ngaythi = edtNgayThi.getText().toString();
                String cathi = edtCaThi.getText().toString();

                if (hoten.isEmpty() && monthi.isEmpty() && ngaythi.isEmpty() && cathi.isEmpty()) {
                    errHoTen.setError("Không bỏ trống thông tin");
                    errMonThi.setError("Không bỏ trống thông tin");
                    errNgayThi.setError("Không bỏ trống thông tin");
                    errCaThi.setError("Không bỏ trống thông tin");
                    return;
                }
                if (hoten.isEmpty()) {
                    errHoTen.setError("Không bỏ trống thông tin");
                    return;
                } else {
                    errHoTen.setError(null);
                }
                if(monthi.isEmpty()){
                    errMonThi.setError("Không b<boost trọng này");
                    return;
                }else {
                    errMonThi.setError(null);
                }
                if(ngaythi.isEmpty()){
                    errNgayThi.setError("Không b<boost trọng này");
                    return;
                }else {
                    errNgayThi.setError(null);
                }
                if(cathi.isEmpty()){
                    errCaThi.setError("Không b<boost trọng này");
                    return;
                }else {
                    errCaThi.setError(null);
                }

                //giá phải là số
                if (!cathi.isEmpty()) {
                    try {
                        Double ct = Double.parseDouble(cathi);
                    } catch (NumberFormatException e) {
                        errCaThi.setError("Ca thi phải là số");
                        return;
                    }
                }

                RequestBody hotenReq = RequestBody.create(MediaType.parse("multipart/form-data"),hoten);
                RequestBody monthiReq = RequestBody.create(MediaType.parse("multipart/form-data"), monthi);
                RequestBody ngaythiReq = RequestBody.create(MediaType.parse("multipart/form-data"),ngaythi);
                RequestBody cathiReq = RequestBody.create(MediaType.parse("multipart/form-data"),cathi);

                MultipartBody.Part multipartBody = null;
                if (file != null) {
                    RequestBody requesrFile = RequestBody.create(MediaType.parse("image/*"), file);
                    multipartBody = MultipartBody.Part.createFormData("hinh_anh_ph36088", file.getName(), requesrFile);
                    //"image" là cùng tên với key trong mutipart
                } else {
                    multipartBody = null;
                }

                Retrofit retrofit = new Retrofit.Builder().baseUrl(APIService.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                APIService apiService = retrofit.create(APIService.class);
                Call<Thi_1704> call = apiService.updateThi_1704(thi.get_id(), hotenReq, monthiReq, multipartBody, ngaythiReq, cathiReq);

                call.enqueue(new Callback<Thi_1704>() {
                    @Override
                    public void onResponse(Call<Thi_1704> call, Response<Thi_1704> response) {
                        if (response.isSuccessful()) {
                            // Cập nhật thành công
                            Call<List<Thi_1704>> call1 = apiService.getListThi_1704();
                            call1.enqueue(new Callback<List<Thi_1704>>() {
                                @Override
                                public void onResponse(Call<List<Thi_1704>> call1, Response<List<Thi_1704>> response) {
                                    if (response.isSuccessful()) {
                                        List<Thi_1704> fetchedFlowers = response.body();
                                        if (fetchedFlowers != null) {
                                            listThi.clear(); // Xóa dữ liệu cũ
                                            listThi.addAll(fetchedFlowers); // Thêm dữ liệu mới
                                            thiAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                                            dialog.dismiss();
                                            file = null;
                                        }
                                    } else {
                                        Log.e("Home", "Failed to get flower: " + response.message());
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Thi_1704>> call, Throwable t) {
                                    Log.e("Home", t.getMessage());
                                }
                            });

                        } else {
                            Toast.makeText(Home.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Thi_1704> call, Throwable t) {
                        // Xử lý khi gữp lỗi trong quá trình gửi yêu cầu
                        Log.e("Update flower", "Error updating flower: " + t.getMessage());
                    }
                });
            }
        });
        // Hiển thị dialog
        dialog.show();
    }

}