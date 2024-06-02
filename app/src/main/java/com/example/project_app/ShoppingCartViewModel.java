package com.example.project_app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCartViewModel extends ViewModel {

    // LiveData to hold the map of products and their quantities
    private final MutableLiveData<Map<Product, Integer>> cartProducts = new MutableLiveData<>(new HashMap<>());

    // Returns the LiveData that can be observed for changes in the cart products
    public LiveData<Map<Product, Integer>> getCartProducts() {
        return cartProducts;
    }

    // Adds a product to the cart, or updates its quantity if it already exists in the cart
    public void addProductToCart(Product product) {
        Map<Product, Integer> currentCart = cartProducts.getValue();
        if (currentCart != null) {
            int quantity = currentCart.getOrDefault(product, 0);
            currentCart.put(product, quantity + 1); // Increment the quantity
            cartProducts.setValue(new HashMap<>(currentCart)); // Update the LiveData with a new map instance
        }
    }

    // Removes a product from the cart
    public void removeProductFromCart(Product product) {
        Map<Product, Integer> currentCart = cartProducts.getValue();
        if (currentCart != null) {
            if (currentCart.containsKey(product)) {
                int quantity = currentCart.get(product);
                if (quantity > 1) {
                    currentCart.put(product, quantity - 1); // Decrement the quantity
                } else {
                    currentCart.remove(product); // Remove the product if the quantity is 1
                }
                cartProducts.setValue(new HashMap<>(currentCart)); // Update the LiveData with a new map instance
            }
        }
    }

    // Clears all products from the cart
    public void clearCart() {
        Map<Product, Integer> currentCart = cartProducts.getValue();
        if (currentCart != null) {
            currentCart.clear();
            cartProducts.setValue(new HashMap<>(currentCart)); // Update the LiveData with a new map instance
        }
    }
}
