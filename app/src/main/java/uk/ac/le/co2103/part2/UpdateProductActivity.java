package uk.ac.le.co2103.part2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class UpdateProductActivity extends AppCompatActivity {

    private EditText productNameEditText;
    private EditText quantityEditText;
    private Button minusButton;
    private Button plusButton;
    private Button saveButton;

    private ProductViewModel productViewModel;
    private Product productToUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        productNameEditText = findViewById(R.id.editTextProductName);
        quantityEditText = findViewById(R.id.editTextQuantity);
        minusButton = findViewById(R.id.buttonMinus);
        plusButton = findViewById(R.id.buttonPlus);
        saveButton = findViewById(R.id.buttonSave);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        int productId = getIntent().getIntExtra("productId", -1);

        // Fetch product details synchronously from the repository
        productToUpdate = productViewModel.getRepo().getProductDao().getProductById(productId);

        if (productToUpdate != null) {
            productNameEditText.setText(productToUpdate.getName());
            quantityEditText.setText(String.valueOf(productToUpdate.getQuantity()));
        } else {
            // Handle the case when the product is not found or null
            Log.e("UpdateProductActivity", "Product not found or null");
        }

        minusButton.setOnClickListener(view -> {
            int quantity = Integer.parseInt(quantityEditText.getText().toString());
            if (quantity > 0) {
                quantity--;
                quantityEditText.setText(String.valueOf(quantity));
            }
        });

        plusButton.setOnClickListener(view -> {
            int quantity = Integer.parseInt(quantityEditText.getText().toString());
            quantity++;
            quantityEditText.setText(String.valueOf(quantity));
        });

        saveButton.setOnClickListener(view -> {
            // Save the updated product
            String name = productNameEditText.getText().toString();
            int quantity = Integer.parseInt(quantityEditText.getText().toString());
            String unit = productToUpdate.getUnit();

            // Update product details
            productToUpdate.setName(name);
            productToUpdate.setQuantity(quantity);

            // Update product synchronously using the repository
            productViewModel.getRepo().getProductDao().updateProduct(productToUpdate);

            // Return to ShoppingListActivity
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}
