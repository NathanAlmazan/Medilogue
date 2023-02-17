package com.automata.medilogue;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryViewHolder extends RecyclerView.ViewHolder {

    TextView nameView, dateView, resultView;

    public HistoryViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
        super(itemView);

        nameView = itemView.findViewById(R.id.tvName);
        dateView = itemView.findViewById(R.id.tvDate);
        resultView = itemView.findViewById(R.id.tvResult);
    }
}
