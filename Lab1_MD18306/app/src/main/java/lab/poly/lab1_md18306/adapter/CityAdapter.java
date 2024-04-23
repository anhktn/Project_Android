package lab.poly.lab1_md18306.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lab.poly.lab1_md18306.Model.City;
import lab.poly.lab1_md18306.R;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    private Context context;

    private ArrayList<City> list;

    public CityAdapter(Context context, ArrayList<City> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.item_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtcountry.setText("Country: " + list.get(position).getCountry());
        holder.txtname.setText("Name: " + list.get(position).getName());
        holder.txtpopulation.setText("Population: " + String.valueOf(list.get(position).getPopulation()));
        if (list.get(position).getCapital()) {
            holder.txtcapital.setText("Captital: có");
        } else
            holder.txtcapital.setText("Captital: không");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtname, txtpopulation, txtcapital, txtcountry;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtname = itemView.findViewById(R.id.txtname);
            txtpopulation = itemView.findViewById(R.id.txtpopulation);
            txtcapital = itemView.findViewById(R.id.txtcapital);
            txtcountry = itemView.findViewById(R.id.txtcountry);

        }
    }
}
