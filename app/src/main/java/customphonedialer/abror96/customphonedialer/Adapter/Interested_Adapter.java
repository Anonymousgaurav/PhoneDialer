package customphonedialer.abror96.customphonedialer.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import customphonedialer.abror96.customphonedialer.Model.Interested_Model;
import customphonedialer.abror96.customphonedialer.R;

public class Interested_Adapter extends RecyclerView.Adapter<Interested_Adapter.InterestedHolder> {


    Context context;
    public List<Interested_Model> lists;

    public Interested_Adapter(Context context, List<Interested_Model> lists) {
        this.context = context;
        this.lists = lists;
    }


    @Override
    public InterestedHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_interested, viewGroup, false);
        return new InterestedHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull InterestedHolder interestedHolder, int position)
    {
            interestedHolder.interested_tView.setText(lists.get(position).getImobilenum());
    }

    @Override
    public int getItemCount() {
        return lists.size();

    }





    public class InterestedHolder extends RecyclerView.ViewHolder
    {
        TextView interested_tView;

        public InterestedHolder(@NonNull View itemView)
        {
            super(itemView);
            interested_tView = itemView.findViewById(R.id.interested_tView);
        }
    }
}
