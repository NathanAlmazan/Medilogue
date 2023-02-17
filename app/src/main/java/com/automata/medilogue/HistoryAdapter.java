package com.automata.medilogue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.automata.medilogue.databinding.ItemHistoryBinding;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.viewHolder> {

    static class viewHolder extends RecyclerView.ViewHolder{
        public viewHolder(@NonNull ItemHistoryBinding itemView) {
            super(itemView.getRoot());
        }
    }

    List<Item> items;

    public HistoryAdapter(List<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new HistoryViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.nameView.setText(items.get(position).getName());
        holder.dateView.setText(items.get(position).getDate());
        holder.resultView.setText(items.get(position).getResult());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
