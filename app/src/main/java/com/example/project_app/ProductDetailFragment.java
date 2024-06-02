package com.example.project_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class ProductDetailFragment extends Fragment {

    private static final String ARG_PRODUCT = "product";
    private Product product;
    private ShoppingCartViewModel shoppingCartViewModel;

    public static ProductDetailFragment newInstance(Product product) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PRODUCT, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = getArguments().getParcelable(ARG_PRODUCT);
        }
        shoppingCartViewModel = new ViewModelProvider(requireActivity()).get(ShoppingCartViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        TextView nameTextView = view.findViewById(R.id.nameTextView);
        TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);
        TextView priceTextView = view.findViewById(R.id.priceTextView);
        ImageView imageView = view.findViewById(R.id.imageView);
        Spinner sizeSpinner = view.findViewById(R.id.sizeSpinner);
        Button addToCartButton = view.findViewById(R.id.addToCartButton);

        if (product != null) {
            nameTextView.setText(product.getName());
            descriptionTextView.setText(product.getDescription());
            priceTextView.setText(String.format("$%.2f", product.getPrice()));
            imageView.setImageResource(product.getImageResourceId());

            // Set up the size spinner
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                    R.array.product_sizes, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sizeSpinner.setAdapter(adapter);

            // Set up the add to cart button
            addToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String selectedSize = sizeSpinner.getSelectedItem().toString();
                    product.setSize(selectedSize);  // Assuming your Product class has a setSize method
                    shoppingCartViewModel.addProductToCart(product);
                    showAddedToCartDialog();
                }
            });
        }

        return view;
    }

    private void showAddedToCartDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Added to Cart")
                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
