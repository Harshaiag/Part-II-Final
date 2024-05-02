package uk.ac.le.co2103.part2;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements ShoppingListAdapter.OnItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ShoppingListViewModel shoppingListViewModel;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupRecyclerView();
        setupFab();

        setupActivityResultLauncher();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ShoppingListAdapter adapter = new ShoppingListAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this); // Set the click listener
        shoppingListViewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);
        shoppingListViewModel.getAllShoppingLists().observe(this, adapter::submitList);
    }

    private void setupFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CreateListActivity.class);
            launcher.launch(intent);
        });
    }

    private void setupActivityResultLauncher() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                assert result.getData() != null;
                handleActivityResult(result.getData());
            } else {
                Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleActivityResult(Intent data) {
        if (data == null) {
            Log.e(TAG, "Intent data is null");
            return;
        }

        String text = data.getStringExtra("Text");
        if (text == null || text.isEmpty()) {
            Log.e(TAG, "Text is null or empty");
            return;
        }

        Log.d(TAG, "Received item: " + text);

        // Check if shopping list already exists
        if (shoppingListViewModel.getShoppingListCountByName(text) >= 1) {
            Toast.makeText(this, "Shopping list already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri imageUri;
        String imageUriString = data.getStringExtra("ImageUri");
        if (imageUriString == null || imageUriString.isEmpty()) {
            int resourceId = R.drawable.shoppingcart; // Default drawable resource ID
            imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + getResources().getResourcePackageName(resourceId)
                    + '/' + getResources().getResourceTypeName(resourceId)
                    + '/' + getResources().getResourceEntryName(resourceId));
        } else {
            imageUri = Uri.parse(imageUriString);
        }

        Log.d(TAG, "Received image URI: " + imageUri);

        // Create and insert shopping list
        ShoppingList shoppingList = new ShoppingList(text, imageUri.toString());
        shoppingListViewModel.insert(shoppingList);
    }


    @Override
    public void onItemClick(int position) {
        // Retrieve the clicked shopping list from the ViewModel
        ShoppingList clickedShoppingList = shoppingListViewModel.getAllShoppingLists().getValue().get(position);

        // Display the text of the clicked shopping list
        Toast.makeText(this, "Clicked: " + clickedShoppingList.getName(), Toast.LENGTH_SHORT).show();

        // Start the ShoppingListActivity with the clicked shopping list ID
        Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
        intent.putExtra("shoppingListId", clickedShoppingList.getListId()); // Pass the shopping list ID as an extra
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ShoppingList clickedShoppingList = shoppingListViewModel.getAllShoppingLists().getValue().get(position);
                deleteShoppingList(clickedShoppingList);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();


    }

    private void deleteShoppingList(ShoppingList shoppingList) {
        shoppingListViewModel.deleteShoppingListWithProducts(shoppingList);
    }
}
