package com.example.project_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment implements ProductAdapter.OnProductClickListener, ProductAdapter.OnFavoritesButtonClickListener, ProductAdapter.OnAddToCartButtonClickListener, ProductAdapter.OnRemoveFavoriteClickListener {

    private boolean[] selectedOptions;
    private Product[] products = new Product[6];
    private List<Product> productList = new ArrayList<>();
    private ProductAdapter adapter;
    private SharedViewModel sharedViewModel;
    private ShoppingCartViewModel shoppingCartViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedOptions = OptionDialogFragment.getSavedOptions(requireContext(), 9);

        // Initialize products (Replace these with your actual product data)
        products[0] = new Product("Product 1", "Description 1", 10.99, R.drawable.tshirt_black, "T-shirt", "Black", "male");
        products[1] = new Product("Product 2", "Description 2", 15.99, R.drawable.jeans_blue, "Jeans", "Blue", "male");
        products[2] = new Product("Product 3", "Description 3", 20.99, R.drawable.tshirt_white, "T-shirt", "White", "female");
        products[3] = new Product("Product 4", "Description 4", 45.99, R.drawable.jeans_blue_2, "Jeans", "Blue", "female");
        products[4] = new Product("Product 5", "Description 5", 10.99, R.drawable.jacket_yellow, "Jacket", "Yellow", "female");
        products[5] = new Product("Product 6", "Description 6", 49.99, R.drawable.jacket_black, "Jacket", "Black", "male");

        productList.addAll(Arrays.asList(products));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        shoppingCartViewModel = new ViewModelProvider(requireActivity()).get(ShoppingCartViewModel.class);

        Button filterButton = rootView.findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();
            }
        });

        RecyclerView recyclerView = rootView.findViewById(R.id.productsRecyclerView);
        adapter = new ProductAdapter(requireContext(), productList, this, this, this, this, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        return rootView;
    }

    private void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Sort By");

        String[] options = {"Price (Ascending)", "Price (Descending)", "Category (T-shirt)", "Category (Jeans)", "Category (Jacket)", "Color (Black)", "Color (Blue)", "Color (Yellow)"};
        builder.setMultiChoiceItems(options, selectedOptions, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                selectedOptions[which] = isChecked;
            }
        });

        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                applyFilters();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void applyFilters() {
        productList.clear();
        productList.addAll(Arrays.asList(products));

        for (int i = 0; i < selectedOptions.length; i++) {
            if (selectedOptions[i]) {
                switch (i) {
                    case 0:
                        Collections.sort(productList, new Comparator<Product>() {
                            @Override
                            public int compare(Product p1, Product p2) {
                                return Double.compare(p1.getPrice(), p2.getPrice());
                            }
                        });
                        break;
                    case 1:
                        Collections.sort(productList, new Comparator<Product>() {
                            @Override
                            public int compare(Product p1, Product p2) {
                                return Double.compare(p2.getPrice(), p1.getPrice());
                            }
                        });
                        break;
                    case 2:
                        filterByCategory("T-shirt");
                        break;
                    case 3:
                        filterByCategory("Jeans");
                        break;
                    case 4:
                        filterByCategory("Jacket");
                        break;
                    case 5:
                        filterByColor("Black");
                        break;
                    case 6:
                        filterByColor("Blue");
                        break;
                    case 7:
                        filterByColor("Yellow");
                        break;
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void filterByCategory(String category) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                filteredList.add(product);
            }
        }
        productList.clear();
        productList.addAll(filteredList);
    }

    private void filterByColor(String color) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getColor().equalsIgnoreCase(color)) {
                filteredList.add(product);
            }
        }
        productList.clear();
        productList.addAll(filteredList);
    }

    @Override
    public void onProductClick(Product product) {
        Fragment productDetailFragment = ProductDetailFragment.newInstance(product);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, productDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onFavoritesButtonClick(Product product) {
        if (!isProductInFavorites(product)) {
            sharedViewModel.addFavoriteProduct(product);
            showAddedToFavoritesDialog();
        } else {
            showAlreadyInFavoritesDialog();
        }
    }

    private boolean isProductInFavorites(Product product) {
        List<Product> favorites = sharedViewModel.getFavoriteProducts().getValue();
        if (favorites != null) {
            for (Product favorite : favorites) {
                if (favorite.getName().equals(product.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void showAddedToFavoritesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Added to Favorites")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlreadyInFavoritesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("This product is already in Favorites")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onAddToCartButtonClick(Product product) {
        showSizeSelectionDialog(product);
    }

    private void showSizeSelectionDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        View view = getLayoutInflater().inflate(R.layout.dialog_select_size, null);
        Spinner sizeSpinner = view.findViewById(R.id.size_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.product_sizes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(adapter);

        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedSize = sizeSpinner.getSelectedItem().toString();
                product.setSize(selectedSize);
                shoppingCartViewModel.addProductToCart(product);
                showAddedToCartDialog();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void showAddedToCartDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Added to Cart")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onRemoveFavoriteClick(Product product) {
        sharedViewModel.removeFavoriteProduct(product);
        showRemovedFromFavoritesDialog();
    }

    private void showRemovedFromFavoritesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Removed from Favorites")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
