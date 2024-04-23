package lab.poly.thiand103_1704.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import lab.poly.thiand103_1704.IClick;
import lab.poly.thiand103_1704.R;
import lab.poly.thiand103_1704.models.Model;

public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.ViewHolder> {
    private Context context;
    private List<Model> listData;
    private IClick iClick;

    public ModelAdapter(Context context, List<Model> listData, IClick iClick) {
        this.context = context;
        this.listData = listData;
        this.iClick = iClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTen.setText("Tên thiết bị: " + listData.get(position).getPh36088_ten_thietbi());
        if (listData.get(position).getPh36088_trang_thai() == 1) {
            holder.tvTrangThai.setText("Trạng thái: Còn hàng");
        } else {
            holder.tvTrangThai.setText("Trạng thái: Hết hàng");
        }
        holder.tvNgayNhap.setText("Ngày nhập: " + listData.get(holder.getAdapterPosition()).getPh36088_ngay_nhap());
        if ((listData.get(position).getPh36088_hinh_anh()) == null) {
            Glide.with(context).load(R.drawable.man).into(holder.ivImage);
        }else {
            Glide.with(context).load(listData.get(holder.getAdapterPosition()).getPh36088_hinh_anh()).into(holder.ivImage);
        }
        holder.ivUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClick.onEditClick(position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClick.onChiTietClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTen, tvTrangThai, tvNgayNhap;
        ImageView ivImage, ivUp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTen = itemView.findViewById(R.id.tvTen);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            tvNgayNhap = itemView.findViewById(R.id.tvNgayNhap);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivUp = itemView.findViewById(R.id.ivUp);

        }
    }
}
