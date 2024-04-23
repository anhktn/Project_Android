package lab.poly.lab1_md18306;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lab.poly.lab1_md18306.Model.City;
import lab.poly.lab1_md18306.adapter.CityAdapter;

public class ListCity extends AppCompatActivity {
    RecyclerView rccCity;
    FloatingActionButton fltADD;
    ArrayList<City> list;
    CityAdapter adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_city);

        db = FirebaseFirestore.getInstance();
        ghiDuLieu();

        rccCity = findViewById(R.id.rccCity);
        fltADD = findViewById(R.id.fltADD);

        loadData();

        fltADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCity();
            }
        });
    }

    public void loadData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rccCity.setLayoutManager(linearLayoutManager);

        CollectionReference citiesRef = FirebaseFirestore.getInstance().collection("cities");

        // Lấy dữ liệu từ Firestore
        citiesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                list = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    // Chuyển đổi mỗi tài liệu Firestore thành một đối tượng City
                    City city = documentSnapshot.toObject(City.class);
                    list.add(city);
                }
                // Đổ dữ liệu vào Adapter của RecyclerView
                adapter = new CityAdapter(ListCity.this, list);

                rccCity.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Xử lý lỗi
            }
        });
    }

    public void addCity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_add_city, null);

        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        EditText edtName = view.findViewById(R.id.edtname);
        EditText edtCountry = view.findViewById(R.id.edtcountry);
        EditText edtPopulation = view.findViewById(R.id.edtpopulation);
        CheckBox chkcapital = view.findViewById(R.id.chkcapital);
        Button btnAdd = view.findViewById(R.id.btnAdd_add);
        Button btnCancel = view.findViewById(R.id.btnCancel_add);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String country = edtCountry.getText().toString();
                String population = edtPopulation.getText().toString();
                boolean capital = chkcapital.isChecked();

                if (name.isEmpty() || country.isEmpty() || population.isEmpty()) {
                    Toast.makeText(ListCity.this, "Không bỏ trống thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Integer.parseInt(population);
                } catch (NumberFormatException e) {
                    Toast.makeText(ListCity.this, "Population phải là số", Toast.LENGTH_SHORT).show();
                    return;
                }

                City city = new City(country, name, Integer.parseInt(population), capital);
                CollectionReference citiesRef = FirebaseFirestore.getInstance().collection("cities");

                // Tạo một đối tượng Map chứa dữ liệu của thành phố mới
                Map<String, Object> cityData = new HashMap<>();
                cityData.put("name", city.getName());
                cityData.put("capital", city.getCapital());
                cityData.put("country", city.getCountry());
                cityData.put("population", city.getPopulation());
                cityData.put("createdAt", new Date());
                ;

                // Thêm dữ liệu vào Firestore
                citiesRef.add(cityData)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                loadData();
                                alertDialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }
        });
        alertDialog.show();

    }

    private void ghiDuLieu() {
        CollectionReference cities = db.collection("cities");

        Map<String, Object> data1 = new HashMap<>();
        data1.put("name", "San Francisco");
        data1.put("capital", true);
        data1.put("country", "USA");
        data1.put("population", 860000);
        data1.put("regions", Arrays.asList("west_coast", "norcal"));
        cities.document("SF").set(data1);

        Map<String, Object> data2 = new HashMap<>();
        data2.put("name", "Los Angeles");
        data1.put("capital", true);
        data2.put("country", "USA");
        data2.put("population", 3900000);
        data2.put("regions", Arrays.asList("west_coast", "socal"));
        cities.document("LA").set(data2);

        Map<String, Object> data3 = new HashMap<>();
        data3.put("name", "Washington D.C.");
        data1.put("capital", true);
        data3.put("country", "USA");
        data3.put("population", 680000);
        data3.put("regions", Arrays.asList("east_coast"));
        cities.document("DC").set(data3);

        Map<String, Object> data4 = new HashMap<>();
        data4.put("name", "Tokyo");
        data1.put("capital", false);
        data4.put("country", "Japan");
        data4.put("population", 9000000);
        data4.put("regions", Arrays.asList("kanto", "honshu"));
        cities.document("TOK").set(data4);

        Map<String, Object> data5 = new HashMap<>();
        data5.put("name", "Beijing");
        data1.put("capital", false);
        data5.put("country", "China");
        data5.put("population", 21500000);
        data5.put("regions", Arrays.asList("jingjinji", "hebei"));
        cities.document("BJ").set(data5);
    }



}