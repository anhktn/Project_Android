package lab.poly.lab5_and103;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lab.poly.lab5_and103.model.FlowerModel;
import lab.poly.lab5_and103.services.APIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlowerAdapter extends RecyclerView.Adapter<FlowerAdapter.ViewHolder> {
    private Context context;
    private List<FlowerModel> listFlower;

public FlowerAdapter(Context context, List<FlowerModel> listFlower) {
    this.context = context;
    this.listFlower = listFlower;
}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_flower, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtSTT.setText( String.valueOf(position + 1) + ".");
        holder.txtName.setText( listFlower.get(position).getName());

        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //comfirm Delete
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa");
                builder.setMessage("Bạn muốn xóa");


                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int position = holder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // Gọi phương thức xóa của Adapter khi người dùng chọn xóa
                            deleteFlower(position);
                            Toast.makeText(context, "Xóa", Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();

                alertDialog.show();
            }
            //show

        });
        holder.ivUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                FlowerModel flower = listFlower.get(position);
                if (position != RecyclerView.NO_POSITION) {
                    // Gọi phương thức xóa của Adapter khi người dùng chọn xóa
                    showUpdateDialog(flower);
                    Toast.makeText(context, "Cập nhật", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFlower.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtSTT;
        ImageView ivDel, ivUpd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtSTT = itemView.findViewById(R.id.txtSTT);
            ivDel = itemView.findViewById(R.id.ivDel);
            ivUpd = itemView.findViewById(R.id.ivUpd);
        }
    }

    public void deleteFlower(int position) {
        //khởi tạo retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d("Delete Fruit", "Deleting fruit at position: " + position);
        //tạo interface
        APIService apiService = retrofit.create(APIService.class);
        // Gọi phương thức Retrofit để thực hiện yêu cầu DELETE tới máy chủ
        FlowerModel flowerToDelete = listFlower.get(position);
        Call<Void> call = apiService.deleteFlower(flowerToDelete.get_id());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xóa hoa khỏi danh sách và cập nhật RecyclerView
                    listFlower.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                } else {
                    // Xử lý khi nhận được phản hồi lỗi từ máy chủ
                    Log.e("Delete Flower", "Failed to delete flower: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý khi gặp lỗi trong quá trình gửi yêu cầu
                Log.e("Delete Flower", "Error deleting flower: " + t.getMessage());
            }
        });
    }

    // Phương thức để hiển thị dialog cập nhật
    private void showUpdateDialog(FlowerModel flower) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Inflate layout cho dialog
        LayoutInflater Inflater = LayoutInflater.from(this.context);
        View view = Inflater.from(this.context).inflate(R.layout.dialog_update, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        // Khởi tạo EditText và gán giá trị hiện tại của sản phẩm vào
        EditText editTextName = view.findViewById(R.id.edtName_up);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);
        Button btnCancel = view.findViewById(R.id.btnCancel_up);

        editTextName.setText(flower.getName());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        // Xử lý sự kiện khi nhấn nút cập nhật trong dialog
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = editTextName.getText().toString();

                // Tạo đối tượng được cập nhật
                FlowerModel updateFlower = new FlowerModel(newName);

                // khởi tạo Retrofit để cập nhật
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(APIService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                // khởi tạo APIService
                APIService apiService = retrofit.create(APIService.class);

                //Gọi API
                Call<FlowerModel> call = apiService.updateFlower(flower.get_id(), updateFlower);
                call.enqueue(new Callback<FlowerModel>() {
                    @Override
                    public void onResponse(Call<FlowerModel> call, Response<FlowerModel> response) {
                        if (response.isSuccessful()) {
                            // Cập nhật thành công
                            Call<List<FlowerModel>> call1 = apiService.getFlowerList();
                            call1.enqueue(new Callback<List<FlowerModel>>() {
                                @Override
                                public void onResponse(Call<List<FlowerModel>> call1, Response<List<FlowerModel>> response) {
                                    if (response.isSuccessful()) {
                                        List<FlowerModel> fetchedFlowers = response.body();
                                        if (fetchedFlowers != null) {
                                            listFlower.clear(); // Xóa dữ liệu cũ
                                            listFlower.addAll(fetchedFlowers); // Thêm dữ liệu mới
                                            notifyDataSetChanged(); // Cập nhật RecyclerView
                                        }
                                    } else {
                                        Log.e("Home", "Failed to get flower: " + response.message());
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<FlowerModel>> call, Throwable t) {
                                    Log.e("Home", t.getMessage());
                                }
                            });
                            Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<FlowerModel> call, Throwable t) {
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
