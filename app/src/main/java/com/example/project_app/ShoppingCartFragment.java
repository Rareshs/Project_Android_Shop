package com.example.project_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

public class ShoppingCartFragment extends Fragment implements ShoppingCartAdapter.OnQuantityChangeListener {

    private RecyclerView cartRecyclerView;
    private ShoppingCartAdapter adapter;
    private ShoppingCartViewModel shoppingCartViewModel;
    private Button checkoutButton;
    private Button clearCartButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        shoppingCartViewModel = new ViewModelProvider(requireActivity()).get(ShoppingCartViewModel.class);

        cartRecyclerView = rootView.findViewById(R.id.cartRecyclerView);
        checkoutButton = rootView.findViewById(R.id.checkoutButton);
        clearCartButton = rootView.findViewById(R.id.clearCartButton);

        adapter = new ShoppingCartAdapter(requireContext(), new ArrayList<>(), this);
        cartRecyclerView.setAdapter(adapter);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        shoppingCartViewModel.getCartProducts().observe(getViewLifecycleOwner(), new Observer<Map<Product, Integer>>() {
            @Override
            public void onChanged(Map<Product, Integer> cartProducts) {
                adapter.setCartProducts(new ArrayList<>(cartProducts.entrySet()));
            }
        });

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToCheckout();
            }
        });

        clearCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingCartViewModel.clearCart();
            }
        });

        return rootView;
    }

    private void navigateToCheckout() {
        Fragment checkoutFragment = new CheckoutFragment();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, checkoutFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onReduceQuantity(Product product) {
        shoppingCartViewModel.removeProductFromCart(product);
    }
}
