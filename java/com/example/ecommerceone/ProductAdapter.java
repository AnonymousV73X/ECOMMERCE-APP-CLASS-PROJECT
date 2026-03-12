package com.example.ecommerceone;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private DatabaseHelper dbHelper;

    public interface OnCartCountChangeListener {
        void onCartCountChanged();
    }
    private OnCartCountChangeListener listener;

    public ProductAdapter(List<Product> productList, DatabaseHelper dbHelper, OnCartCountChangeListener listener) {
        this.productList = productList;
        this.dbHelper = dbHelper;
        this.listener = listener;
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
        holder.nameTv.setText(product.getName());

        // --- FOm\mating PRICE WITH COMMAS ---
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        String formattedPrice = "KES " + formatter.format(product.getPrice());
        holder.priceTv.setText(formattedPrice);

        holder.imageView.setImageResource(product.getImageResource());

        holder.addBtn.setOnClickListener(v -> {
            boolean exists = dbHelper.isItemInCart(product.getId());

            if (exists) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Item in Cart")
                        .setMessage(product.getName() + " is already in your cart. Add another?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            dbHelper.addToCart(product);
                            Toast.makeText(v.getContext(), "Added another " + product.getName(), Toast.LENGTH_SHORT).show();
                            if (listener != null) listener.onCartCountChanged();
                        })
                        .setNegativeButton("No", null)
                        .show();
            } else {
                dbHelper.addToCart(product);
                Toast.makeText(v.getContext(), product.getName() + " added to cart", Toast.LENGTH_SHORT).show();
                if (listener != null) listener.onCartCountChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv, priceTv;
        ImageView imageView;
        MaterialButton addBtn;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.tvProductName);
            priceTv = itemView.findViewById(R.id.tvProductPrice);
            imageView = itemView.findViewById(R.id.ivProduct);
            addBtn = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}