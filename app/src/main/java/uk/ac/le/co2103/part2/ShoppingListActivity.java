package uk.ac.le.co2103.part2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShoppingListActivity extends AppCompatActivity implements ProductAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private int shoppingListId;
    private ProductViewModel productViewModel;

    private static final int ADD_PRODUCT_REQUEST_CODE = 1;
    private ActivityResultLauncher<Intent> addProductLauncher;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        // Retrieve shopping list ID from intent
        shoppingListId = getIntent().getIntExtra("shoppingListId", -1);

        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter();
        recyclerView.setAdapter(adapter);

        // Set item click listener
        adapter.setOnItemClickListener(this);

        // Initialize ViewModel
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        // Observe the list of products and update the UI
        productViewModel.getProductsByListId(shoppingListId).observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                adapter.submitList(products);
            }
        });

        // Initialize ActivityResultLauncher for AddProductActivity
        addProductLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    String name = data.getStringExtra("productName");
                    int quantity = data.getIntExtra("productQuantity", 0);
                    String unit = data.getStringExtra("productUnit");

                    // Add the product to the shopping list
                    addProductToDatabase(name, quantity, unit);
                }
            }
        });

        // Floating action button to add a new product
        findViewById(R.id.fabAddProduct).setOnClickListener(view -> {
            Intent intent = new Intent(ShoppingListActivity.this, AddProductActivity.class);
            addProductLauncher.launch(intent);
        });
    }

    private void addProductToDatabase(String name, int quantity, String unit) {
        Product product = new Product(shoppingListId, name, quantity, unit);
        productViewModel.insert(product);
    }

    @Override
    public void onItemClick(int position) {
        // Get the clicked product
        Product product = adapter.getCurrentList().get(position);

        // Show a toast message with a brief description of the item
        String message = "Name: " + product.getName() + ", Quantity: " + product.getQuantity() + " " + product.getUnit();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
