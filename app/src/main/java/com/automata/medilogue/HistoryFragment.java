package com.automata.medilogue;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.automata.medilogue.databinding.HistoryFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    private HistoryFragmentBinding binding;

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = HistoryFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        RecyclerView recyclerView = binding.rvHistoryFrag;

        List<Item> items = new ArrayList<Item>();
        items.add(new Item("Kyle", "11/21/02", "May sakit ka pre"));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new HistoryAdapter(items));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


