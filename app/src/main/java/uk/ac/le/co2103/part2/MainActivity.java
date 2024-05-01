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
    private static final int ADD_SHOPPINGLIST_REQUEST_CODE = 1;

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
        String text = data.getStringExtra("Text");
        Log.d(TAG, "#####Received item: " + text);
        Uri imageUri;
        String imageUriString = data.getStringExtra("ImageUri");
        if (Objects.equals(imageUriString, "")) {
            int resourceId = R.drawable.shoppingcart; // Your drawable resource ID
            imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + getResources().getResourcePackageName(resourceId)
                    + '/' + getResources().getResourceTypeName(resourceId)
                    + '/' + getResources().getResourceEntryName(resourceId));
        } else {
            imageUri = Uri.parse(imageUriString);
        }
        Log.d(TAG, "### Received image URI: " + imageUri);
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
        // Retrieve the clicked shopping list from the ViewModel
        ShoppingList clickedShoppingList = shoppingListViewModel.getAllShoppingLists().getValue().get(position);

        // Show a confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this shopping list?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Delete the shopping list and associated products
                        deleteShoppingList(clickedShoppingList);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteShoppingList(ShoppingList shoppingList) {
        shoppingListViewModel.deleteShoppingListWithProducts(shoppingList);
    }

}
