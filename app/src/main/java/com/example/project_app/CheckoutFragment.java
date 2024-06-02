package com.example.project_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CheckoutFragment extends Fragment {

    private RecyclerView checkoutRecyclerView;
    private ShoppingCartAdapter adapter;
    private ShoppingCartViewModel shoppingCartViewModel;
    private TextView totalTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_checkout, container, false);

        shoppingCartViewModel = new ViewModelProvider(requireActivity()).get(ShoppingCartViewModel.class);

        checkoutRecyclerView = rootView.findViewById(R.id.checkoutRecyclerView);
        totalTextView = rootView.findViewById(R.id.totalTextView);

        adapter = new ShoppingCartAdapter(requireContext(), new ArrayList<>(), null);
        checkoutRecyclerView.setAdapter(adapter);
        checkoutRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        shoppingCartViewModel.getCartProducts().observe(getViewLifecycleOwner(), new Observer<Map<Product, Integer>>() {
            @Override
            public void onChanged(Map<Product, Integer> cartProducts) {
                List<Map.Entry<Product, Integer>> cartProductList = new ArrayList<>(cartProducts.entrySet());
                adapter.setCartProducts(cartProductList);
                updateTotal(cartProductList);
            }
        });

        return rootView;
    }

    private void updateTotal(List<Map.Entry<Product, Integer>> cartProductList) {
        double total = 0.0;
        for (Map.Entry<Product, Integer> entry : cartProductList) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        totalTextView.setText("Total: $" + String.format("%.2f", total));
    }
}
