package com.example.ecommerceone;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ProductAdapter.OnCartCountChangeListener {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private EditText searchInput;
    private FloatingActionButton fabCart;
    private TextView tvCartCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.rvProducts);
        searchInput = findViewById(R.id.etSearch);
        fabCart = findViewById(R.id.fabCart);
        tvCartCount = findViewById(R.id.tvCartCount);

        // 1. Setting up ... Grid Layout (2 columns)
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // 2. Loading ... initial data
        loadProducts("");

        // 3. Updating FAB badge initially
        updateFabBadge();

        // 4. Search functionality
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Mov to Cart
        fabCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void loadProducts(String query) {
        List<Product> productList = dbHelper.getAllProducts(query);
        adapter = new ProductAdapter(productList, dbHelper, this);
        recyclerView.setAdapter(adapter);
    }

    // Updating FAB Badge
    @Override
    public void onCartCountChanged() {
        updateFabBadge();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFabBadge();
    }

    private void updateFabBadge() {
        int count = dbHelper.getCartCount();

        if (count > 0) {
            tvCartCount.setVisibility(View.VISIBLE);
            tvCartCount.setText(String.valueOf(count));
        } else {
            tvCartCount.setVisibility(View.GONE);
        }
    }
}