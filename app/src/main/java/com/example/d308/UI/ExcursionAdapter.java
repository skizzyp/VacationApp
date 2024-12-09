package com.example.d308.UI;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.d308.R;
import com.example.d308.entity.Excursion;
import com.example.d308.entity.Vacation;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {

    class ExcursionViewHolder extends RecyclerView.ViewHolder{
        private final TextView excursionItemView;
        private final TextView dateItemView;
        private ExcursionViewHolder(View itemView) {
            super(itemView);
            excursionItemView=itemView.findViewById(R.id.textView3);
            dateItemView=itemView.findViewById(R.id.textView9);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    final Excursion current = mExcursions.get(position);
                    Intent intent=new Intent(context, ExcursionDetails.class);
                    intent.putExtra("id",current.getExcursionID());
                    intent.putExtra("date", current.getDate());
                    intent.putExtra("title", current.getTitle());
                    intent.putExtra("vacationID", current.getVacationID());
                    intent.putExtra("excursionID", current.getExcursionID());
                    context.startActivity(intent);

                }
            });
        }
    }
    private final Context context;
    private List<Excursion> mExcursions;
    private final LayoutInflater mInflator;

    public ExcursionAdapter(Context context){
        mInflator=LayoutInflater.from(context);
        this.context=context;
    }



    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflator.inflate(R.layout.excursion_list_item,parent, false);
        return new ExcursionViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        if(mExcursions != null) {
            Excursion current = mExcursions.get(position);
            String title = current.getTitle();
            int vacationID = current.getVacationID();
            String date = current.getDate();
            holder.excursionItemView.setText(title);
            holder.dateItemView.setText(date);
        }else{
            holder.excursionItemView.setText("No Excursion Name");
        }
    }

    public void setExcursions(List<Excursion> excursions) {
        mExcursions=excursions;

    }
    @Override
    public int getItemCount() {
        if(mExcursions != null) {
            return mExcursions.size();
    } else {
        return 0;}
    }
}
