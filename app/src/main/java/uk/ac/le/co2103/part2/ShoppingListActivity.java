package uk.ac.le.co2103.part2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        productViewModel.getProductsByListId(shoppingListId).observe(this, products -> adapter.submitList(products));

        // Initialize ActivityResultLauncher for AddProductActivity
        addProductLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    String name = data.getStringExtra("productName");
                    int quantity = data.getIntExtra("productQuantity", 0);
                    String unit = data.getStringExtra("productUnit");

                    // Add the product to the shopping list in a separate thread
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
        // Perform database operation in a separate thread
        new Thread(() -> {
            // Check if the product already exists in the database for the current shopping list ID
            int productCount = productViewModel.getProductCountByNameAndListId(name, shoppingListId);

            if (productCount > 0) {
                // Product already exists, display toast message
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Product Already Exists", Toast.LENGTH_SHORT).show());
            } else {
                // Product does not exist, insert it into the database
                Product product = new Product(shoppingListId, name, quantity, unit);
                productViewModel.insert(product);
            }
        }).start();
    }


    @Override
    public void onItemClick(int position) {
        // Get the clicked product
        Product product = adapter.getCurrentList().get(position);

        // Show a toast message with a brief description of the item
        String message = "Name: " + product.getName() + ", Quantity: " + product.getQuantity() + " " + product.getUnit();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(int position) {
        Product product = adapter.getCurrentList().get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options");
        builder.setMessage("Choose an option:");

        builder.setPositiveButton("Edit", (dialog, which) -> {
            // Edit the product in a separate thread using a Handler
            new Handler(Looper.getMainLooper()).post(() -> {
                Intent intent = new Intent(ShoppingListActivity.this, UpdateProductActivity.class);
                intent.putExtra("productId", product.getProductId());
                startActivity(intent);
            });
        });

        builder.setNegativeButton("Delete", (dialog, which) -> {
            // Delete the product from the database
            deleteProduct(product);
        });

        builder.setNeutralButton("Cancel", null);
        builder.show();
    }

    private void deleteProduct(Product product) {
        productViewModel.deleteProduct(product.getProductId());
        Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show();
    }
}
