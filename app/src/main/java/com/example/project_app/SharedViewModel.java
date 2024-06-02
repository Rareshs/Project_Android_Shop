package com.example.project_app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<Product>> favoriteProducts = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Product>> getFavoriteProducts() {
        return favoriteProducts;
    }

    public void addFavoriteProduct(Product product) {
        List<Product> currentFavorites = favoriteProducts.getValue();
        if (currentFavorites != null && !currentFavorites.contains(product)) {
            currentFavorites.add(product);
            favoriteProducts.setValue(currentFavorites);
        }
    }

    public void removeFavoriteProduct(Product product) {
        List<Product> currentFavorites = favoriteProducts.getValue();
        if (currentFavorites != null && currentFavorites.contains(product)) {
            currentFavorites.remove(product);
            favoriteProducts.setValue(currentFavorites);
        }
    }
}
