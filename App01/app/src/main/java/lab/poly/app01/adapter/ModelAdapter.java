package lab.poly.app01.adapter;

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

import lab.poly.app01.IClick;
import lab.poly.app01.R;
import lab.poly.app01.models.Model;

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
        View view = layoutInflater.inflate(R.layout.item,parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTen.setText("Tên: " + listData.get(position).getTen_ph36088());
        holder.tvTheLoai.setText("Thể loại: " + listData.get(position).getThe_loai_ph36088());
        holder.tvGia.setText("Gia: " + listData.get(position).getGia_ph36088());
        holder.tvSoLuongBan.setText("Số luong bán: " + listData.get(position).getSo_luong_ban_ph36088());
        holder.tvNgayBan.setText("Ngày bán: " + listData.get(position).getNgay_ban_ph36088());

        if ((listData.get(position).getHinh_anh_ph36088()) == null) {
            Glide.with(context).load(R.drawable.man).into(holder.ivImage);
        }else {
            Glide.with(context).load(listData.get(position).getHinh_anh_ph36088()).into(holder.ivImage);
        }

        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                iClick.onDeleteClick(position);
            }

        });
        holder.ivUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                iClick.onEditClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTen, tvTheLoai, tvGia, tvSoLuongBan, tvNgayBan;
        ImageView ivImage, ivDel, ivUp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTen = itemView.findViewById(R.id.tvTen);
            tvTheLoai = itemView.findViewById(R.id.tvTheLoai);
            tvGia = itemView.findViewById(R.id.tvGia);
            tvSoLuongBan = itemView.findViewById(R.id.tvSoLuongBan);
            tvNgayBan = itemView.findViewById(R.id.tvNgayBan);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivDel = itemView.findViewById(R.id.ivDel);
            ivUp = itemView.findViewById(R.id.ivUp);
        }
    }
}
