package com.example.ecommerceone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private Button checkoutBtn;
    private TextView tvTotal;
    private CartAdapter adapter;
    private LinearLayout llCheckoutContainer;
    private LinearLayout llEmptyCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.rvCart);
        checkoutBtn = findViewById(R.id.btnCheckout);
        tvTotal = findViewById(R.id.tvTotal);

        // New views for Empty State
        llCheckoutContainer = findViewById(R.id.llCheckoutContainer);
        llEmptyCart = findViewById(R.id.llEmptyCart);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadCartItems();

        checkoutBtn.setOnClickListener(v -> processPayment());
    }

    private void loadCartItems() {
        List<Product> cartList = dbHelper.getCartItems();

        // --- LOGIC: CHECKING IF EMPTY ---
        if (cartList.isEmpty()) {
            // 1. Hid\in List
            recyclerView.setVisibility(View.GONE);
            // 2. Hid\in Checkout Container (Total + Button)
            llCheckoutContainer.setVisibility(View.GONE);
            // 3. Show\in' Empty State
            llEmptyCart.setVisibility(View.VISIBLE);
        } else {
            // 1. Show\in' List
            recyclerView.setVisibility(View.VISIBLE);
            // 2. Show\in' Checkout Container
            llCheckoutContainer.setVisibility(View.VISIBLE);
            // 3. Hide\n' Empty State
            llEmptyCart.setVisibility(View.GONE);

            // Load\in' Data
            adapter = new CartAdapter(cartList);
            recyclerView.setAdapter(adapter);
            updateTotal(cartList);
        }
    }

    private void updateTotal(List<Product> cartList) {
        double total = 0;
        for (Product p : cartList) {
            total += p.getPrice();
        }

        DecimalFormat formatter = new DecimalFormat("#,###.00");
        tvTotal.setText("Total: KES " + formatter.format(total));
    }

    private void processPayment() {

        if (adapter == null || adapter.getItemCount() == 0) {
            Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirm Payment")
                .setMessage("Do you want to proceed to payment gateway?")
                .setPositiveButton("Pay", (dialog, which) -> {
                    Toast.makeText(CartActivity.this, "Processing Payment...", Toast.LENGTH_SHORT).show();

                    new android.os.Handler().postDelayed(() -> {
                        dbHelper.clearCart();
                        loadCartItems(); // This will trigger the Empty State logic
                        Toast.makeText(CartActivity.this, "Payment Successful!", Toast.LENGTH_LONG).show();
                    }, 2000);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
        List<Product> cartList;

        public CartAdapter(List<Product> cartList) {
            this.cartList = cartList;
        }

        @Override
        public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
            Button btn = view.findViewById(R.id.btnAddToCart);
            btn.setVisibility(View.GONE);
            return new CartViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CartViewHolder holder, int position) {
            Product product = cartList.get(position);
            holder.nameTv.setText(product.getName());

            DecimalFormat formatter = new DecimalFormat("#,###.00");
            holder.priceTv.setText("KES " + formatter.format(product.getPrice()));

            holder.imageView.setImageResource(product.getImageResource());

            // --- SHOW DELETE ICON & HANDLE CLICK ---
            holder.deleteIcon.setVisibility(View.VISIBLE);
            holder.deleteIcon.setOnClickListener(v -> {
                dbHelper.deleteCartItem(product.getId());

                int positionToRemove = holder.getAdapterPosition();
                if (positionToRemove != RecyclerView.NO_POSITION) {
                    cartList.remove(positionToRemove);
                    notifyItemRemoved(positionToRemove);
                    notifyItemRangeChanged(positionToRemove, cartList.size());
                    updateTotal(cartList);

                    // CheckIN' if cart became empty after deletion
                    if (cartList.isEmpty()) {
                        loadCartItems();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return cartList.size();
        }

        class CartViewHolder extends RecyclerView.ViewHolder {
            TextView nameTv, priceTv;
            ImageView imageView, deleteIcon;

            public CartViewHolder(View itemView) {
                super(itemView);
                nameTv = itemView.findViewById(R.id.tvProductName);
                priceTv = itemView.findViewById(R.id.tvProductPrice);
                imageView = itemView.findViewById(R.id.ivProduct);
                deleteIcon = itemView.findViewById(R.id.ivDelete);
            }
        }
    }
}