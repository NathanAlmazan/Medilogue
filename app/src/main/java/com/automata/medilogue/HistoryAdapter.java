package com.automata.medilogue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;
import com.automata.medilogue.databinding.ItemHistoryBinding;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.viewHolder> {

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
//        holder.itemView.t
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class viewHolder extends RecyclerView.ViewHolder{

        ItemHistoryBinding item;

        public viewHolder(@NonNull ItemHistoryBinding itemView) {
            super(itemView.getRoot());

            item = itemView;


        }

    }

    List<Item> items;

    public HistoryAdapter(List<Item> items) {
        this.items = items;
    }


}
