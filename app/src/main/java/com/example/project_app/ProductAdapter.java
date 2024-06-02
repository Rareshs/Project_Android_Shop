package com.example.project_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;
    private OnProductClickListener onProductClickListener;
    private OnFavoritesButtonClickListener onFavoritesButtonClickListener;
    private OnAddToCartButtonClickListener onAddToCartButtonClickListener;
    private OnRemoveFavoriteClickListener onRemoveFavoriteClickListener;
    private boolean isFavoriteView;

    public ProductAdapter(Context context, List<Product> productList,
                          OnProductClickListener listener,
                          OnFavoritesButtonClickListener favoritesListener,
                          OnAddToCartButtonClickListener cartListener,
                          OnRemoveFavoriteClickListener removeFavoriteListener,
                          boolean isFavoriteView) {
        this.context = context;
        this.productList = productList;
        this.onProductClickListener = listener;
        this.onFavoritesButtonClickListener = favoritesListener;
        this.onAddToCartButtonClickListener = cartListener;
        this.onRemoveFavoriteClickListener = removeFavoriteListener;
        this.isFavoriteView = isFavoriteView;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product, isFavoriteView);

        holder.itemView.setOnClickListener(v -> {
            if (onProductClickListener != null) {
                onProductClickListener.onProductClick(product);
            }
        });

        if (!isFavoriteView) {
            holder.addToFavoritesButton.setVisibility(View.VISIBLE);
            holder.addToFavoritesButton.setOnClickListener(v -> {
                if (onFavoritesButtonClickListener != null) {
                    onFavoritesButtonClickListener.onFavoritesButtonClick(product);
                }
            });
        } else {
            holder.addToFavoritesButton.setVisibility(View.GONE);
        }

        holder.addToCartButton.setOnClickListener(v -> {
            if (onAddToCartButtonClickListener != null) {
                onAddToCartButtonClickListener.onAddToCartButtonClick(product);
            }
        });

        holder.removeFromFavoritesButton.setOnClickListener(v -> {
            if (onRemoveFavoriteClickListener != null) {
                onRemoveFavoriteClickListener.onRemoveFavoriteClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView descriptionTextView;
        private TextView priceTextView;
        private ImageView imageView;
        private ImageButton addToFavoritesButton;
        private ImageButton addToCartButton;
        private Button removeFromFavoritesButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            imageView = itemView.findViewById(R.id.imageView);
            addToFavoritesButton = itemView.findViewById(R.id.add_to_favorites_button);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_button);
            removeFromFavoritesButton = itemView.findViewById(R.id.remove_from_favorites_button);
        }

        public void bind(Product product, boolean isFavoriteView) {
            nameTextView.setText(product.getName());
            descriptionTextView.setText(product.getDescription());
            priceTextView.setText(String.valueOf(product.getPrice()));
            imageView.setImageResource(product.getImageResourceId());

            if (isFavoriteView) {
                addToFavoritesButton.setVisibility(View.GONE);
                removeFromFavoritesButton.setVisibility(View.VISIBLE);
            } else {
                addToFavoritesButton.setVisibility(View.VISIBLE);
                removeFromFavoritesButton.setVisibility(View.GONE);
            }
        }
    }

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public interface OnFavoritesButtonClickListener {
        void onFavoritesButtonClick(Product product);
    }

    public interface OnAddToCartButtonClickListener {
        void onAddToCartButtonClick(Product product);
    }

    public interface OnRemoveFavoriteClickListener {
        void onRemoveFavoriteClick(Product product);
    }

    public void setProducts(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }
}
