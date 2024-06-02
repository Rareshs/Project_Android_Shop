package com.example.project_app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteFragment extends Fragment {

    private RecyclerView favoritesRecyclerView;
    private ProductAdapter adapter;
    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        favoritesRecyclerView = rootView.findViewById(R.id.favoritesRecyclerView);
        adapter = new ProductAdapter(requireContext(), null, null, null, null, new ProductAdapter.OnRemoveFavoriteClickListener() {
            @Override
            public void onRemoveFavoriteClick(Product product) {
                sharedViewModel.removeFavoriteProduct(product);
            }
        }, true);
        favoritesRecyclerView.setAdapter(adapter);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        sharedViewModel.getFavoriteProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                Log.d("FavoriteFragment", "onChanged() called with " + products.size() + " products.");
                adapter.setProducts(products);
            }
        });

        return rootView;
    }
}
