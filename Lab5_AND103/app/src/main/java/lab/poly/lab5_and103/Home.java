package lab.poly.lab5_and103;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import lab.poly.lab5_and103.model.FlowerModel;
import lab.poly.lab5_and103.services.APIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity {
    RecyclerView rccFlower;
    List<FlowerModel> listFlower;
    FlowerAdapter flowerAdapter;
    FloatingActionButton btnAdd;
    EditText edtSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        rccFlower = findViewById(R.id.rccFruit);
        btnAdd = findViewById(R.id.btnAdd);
        edtSearch = findViewById(R.id.edtSearch);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rccFlower.setLayoutManager(layoutManager);
        listFlower = new ArrayList<>();
        flowerAdapter = new FlowerAdapter(this, listFlower);
        rccFlower.setAdapter(flowerAdapter);

        getListFlower();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String key = charSequence.toString();
                if (key.length()== 0 && key.contains("") ) {
                    getListFlower();
                }
                if (!key.isEmpty()) {
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(APIService.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                    APIService apiService = retrofit.create(APIService.class);
                    Call<List<FlowerModel>> call = apiService.searchFlower(key);
                    call.enqueue(new Callback<List<FlowerModel>>() {
                        @Override
                        public void onResponse(Call<List<FlowerModel>> call, Response<List<FlowerModel>> response) {
                            if(response.isSuccessful()) {
                                listFlower.clear();
                                listFlower.addAll(response.body());

                                flowerAdapter = new FlowerAdapter(Home.this, listFlower);
                                rccFlower.setAdapter(flowerAdapter);

                                Log.d("Home", "Search successful");
                            }
                        }

                        @Override
                        public void onFailure(Call<List<FlowerModel>> call, Throwable t) {

                            Log.e("Home", t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddStudentDialog();
            }
        });

    }


    private void showAddStudentDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add, null);
        dialogBuilder.setView(dialogView);

        TextInputEditText edtName = dialogView.findViewById(R.id.edtName);

        Button btnAdd = dialogView.findViewById(R.id.btnAdd_add);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel_add);
        AlertDialog alertDialog = dialogBuilder.create();
        TextInputLayout errName = dialogView.findViewById(R.id.errName);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();

                if (name.isEmpty()) {
                    errName.setError("Name is required");
                    return;
                } else {
                    errName.setError(null);
                }
                FlowerModel newFlower = new FlowerModel(name);
                //Khởi tạo retrofit
                Retrofit retrofit = new Retrofit.Builder().baseUrl(APIService.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                //Khởi tạo APIService
                APIService apiService = retrofit.create(APIService.class);
                //Call
                Call<FlowerModel> objCall = apiService.addFlower(newFlower);
                objCall.enqueue(new Callback<FlowerModel>() {
                    @Override
                    public void onResponse(Call<FlowerModel> call, Response<FlowerModel> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(Home.this, "Add success", Toast.LENGTH_SHORT).show();
                            //gọi hàm lấy lại danh sách
                            getListFlower();
                        } else {
                            Toast.makeText(Home.this, "Add failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<FlowerModel> call, Throwable t) {
                        Toast.makeText(Home.this, "Add failed", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.dismiss();

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void getListFlower() {
        //tạo converter
        Gson gson = new GsonBuilder().setLenient().create();

        //khởi tạo retrofit Clinet
        Retrofit retrofit = new Retrofit.Builder().baseUrl(APIService.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        //tạo interface
        APIService apiService = retrofit.create(APIService.class);
        //tạo đối tượng Call
        Call<List<FlowerModel>> objCall = apiService.getFlowerList();
        objCall.enqueue(new Callback<List<FlowerModel>>() {
            @Override
            public void onResponse(Call<List<FlowerModel>> call, Response<List<FlowerModel>> response) {
                if (response.isSuccessful()) {
                    listFlower.clear();
                    listFlower.addAll(response.body());
                    flowerAdapter.notifyDataSetChanged();
                    Log.e("Home", "Success");
                }
            }

            @Override
            public void onFailure(Call<List<FlowerModel>> call, Throwable t) {

                Log.e("Home", t.getMessage());
            }
        });
    }


}