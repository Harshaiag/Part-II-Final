package uk.ac.le.co2103.part2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddProductActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextQuantity;
    private Spinner spinnerUnit;
    private Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product_activity);

        // Initialize views
        editTextName = findViewById(R.id.editTextName);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        spinnerUnit = findViewById(R.id.spinner);
        buttonAdd = findViewById(R.id.btnAdd);

        // Set up spinner
        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.units_array,
                android.R.layout.simple_spinner_item
        );
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(unitAdapter);

        // Set up click listener for add button
        buttonAdd.setOnClickListener(view -> {
            // Get input values
            String name = editTextName.getText().toString().trim();
            String quantityStr = editTextQuantity.getText().toString().trim();
            String unit = spinnerUnit.getSelectedItem().toString();

            // Validate inputs
            if (name.isEmpty() || quantityStr.isEmpty()) {
                Toast.makeText(AddProductActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(quantityStr);

            // Create intent to pass back data to ShoppingListActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("productName", name);
            resultIntent.putExtra("productQuantity", quantity);
            resultIntent.putExtra("productUnit", unit);
            setResult(RESULT_OK, resultIntent);
            finish(); // Close the activity and return to ShoppingListActivity
        });
    }
}
