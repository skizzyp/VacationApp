package com.example.d308.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308.R;
import com.example.d308.entity.Vacation;

import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {
    private final Context context;
    private final LayoutInflater mInflater;
    public VacationAdapter(Context context){
        mInflater= LayoutInflater.from(context);
        this.context=context;
    }
    private List<Vacation> nVacations;
    public class VacationViewHolder extends RecyclerView.ViewHolder {
        private final TextView vacationItemView;
        private final TextView startItemView;
        private final TextView endItemView;
        private VacationViewHolder(View itemView) {
            super(itemView);
            vacationItemView=itemView.findViewById(R.id.textView2);
            startItemView=itemView.findViewById(R.id.textView5);
            endItemView=itemView.findViewById(R.id.textView8);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    final Vacation current=nVacations.get(position);
                    Intent intent = new Intent(context, VacationDetails.class);
                    intent.putExtra("id", current.getVacationID());
                    intent.putExtra("title", current.getTitle());
                    intent.putExtra("start date", current.getStartDate());
                    intent.putExtra("end date", current.getEndDate());
                    intent.putExtra("place", current.getPlace());
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public VacationAdapter.VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=mInflater.inflate(R.layout.vacation_list_item,parent,false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationAdapter.VacationViewHolder holder, int position) {
        if(nVacations != null) {
            Vacation current = nVacations.get(position);
            String title = current.getTitle();
            String start = current.getStartDate();
            String end = current.getEndDate();
            holder.vacationItemView.setText(title);
            holder.startItemView.setText(start);
            holder.endItemView.setText(end);

        }
    }

    @Override
    public int getItemCount() {
        if(nVacations != null) {
            return nVacations.size();
        }else return 0;
    }

    public void setVacations(List<Vacation> vacations) {
        nVacations = vacations;
        notifyDataSetChanged();
    }
}
