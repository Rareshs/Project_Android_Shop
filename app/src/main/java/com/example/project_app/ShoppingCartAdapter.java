package com.example.project_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.CartViewHolder> {

    private List<Map.Entry<Product, Integer>> cartProductList;
    private Context context;
    private OnQuantityChangeListener onQuantityChangeListener;

    public ShoppingCartAdapter(Context context, List<Map.Entry<Product, Integer>> cartProductList, OnQuantityChangeListener onQuantityChangeListener) {
        this.context = context;
        this.cartProductList = cartProductList;
        this.onQuantityChangeListener = onQuantityChangeListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_product, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Map.Entry<Product, Integer> entry = cartProductList.get(position);
        Product product = entry.getKey();
        int quantity = entry.getValue();
        holder.bind(product, quantity);
    }

    @Override
    public int getItemCount() {
        return cartProductList.size();
    }

    public void setCartProducts(List<Map.Entry<Product, Integer>> cartProductList) {
        this.cartProductList = cartProductList;
        notifyDataSetChanged();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView quantityTextView;
        private TextView priceTextView;
        private ImageView imageView;
        private Button reduceQuantityButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            imageView = itemView.findViewById(R.id.imageView);
            reduceQuantityButton = itemView.findViewById(R.id.reduceQuantityButton);

            reduceQuantityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Map.Entry<Product, Integer> entry = cartProductList.get(position);
                        onQuantityChangeListener.onReduceQuantity(entry.getKey());
                    }
                }
            });
        }

        public void bind(Product product, int quantity) {
            nameTextView.setText(product.getName());
            quantityTextView.setText("Quantity: " + quantity);
            priceTextView.setText("Price: $" + String.format("%.2f", product.getPrice() * quantity));
            imageView.setImageResource(product.getImageResourceId());
        }
    }

    public interface OnQuantityChangeListener {
        void onReduceQuantity(Product product);
    }
}
