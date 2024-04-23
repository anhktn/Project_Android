package lab.poly.thi_1704.adapter;

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

import lab.poly.thi_1704.IClick;
import lab.poly.thi_1704.R;
import lab.poly.thi_1704.models.Thi_1704;

public class ThiAdapter extends RecyclerView.Adapter<ThiAdapter.ViewHolder> {
    private Context context;
    private List<Thi_1704> listThi;
    private IClick iClick;

    public ThiAdapter(Context context, List<Thi_1704> listThi, IClick iClick) {
        this.context = context;
        this.listThi = listThi;
        this.iClick = iClick;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_thi,parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvHoTen.setText("Họ tên: " + listThi.get(position).getHoten_ph36088());
        holder.tvMonThi.setText("Môn thi: " + listThi.get(position).getMon_thi_ph36088());
        holder.tvNgayThi.setText("Ngày thi: " + listThi.get(position).getNgay_thi_ph36088());
        holder.tvCaThi.setText("Ca thi: " + listThi.get(position).getCa_thi_ph36088());
        if ((listThi.get(position).getHinh_anh_ph36088()) == null) {
            Glide.with(context).load(R.drawable.man).into(holder.ivImage);
        }else {
            Glide.with(context).load(listThi.get(position).getHinh_anh_ph36088()).into(holder.ivImage);
        }
        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                iClick.onDeleteClick(position);
            }

        });
        holder.ivUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                iClick.onEditClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listThi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHoTen, tvMonThi, tvNgayThi, tvCaThi;
        ImageView ivDel, ivUpd, ivImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHoTen = itemView.findViewById(R.id.txtHoTen);
            tvMonThi = itemView.findViewById(R.id.txtMonThi);
            tvNgayThi = itemView.findViewById(R.id.txtNgayThi);
            tvCaThi = itemView.findViewById(R.id.txtCaThi);
            ivDel = itemView.findViewById(R.id.ivDel);
            ivUpd = itemView.findViewById(R.id.ivUpd);
            ivImage = itemView.findViewById(R.id.ivImage);

        }
    }
}
