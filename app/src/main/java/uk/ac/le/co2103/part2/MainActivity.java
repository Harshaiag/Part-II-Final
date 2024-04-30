package uk.ac.le.co2103.part2;

import static uk.ac.le.co2103.part2.CreateListActivity.RESULT_DUPLICATE;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

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


            }


            else {
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
            // Here, you have the URI of the selected image
            // Log.d(TAG, "Received image URI: " + imageUri);
             }


            Log.d(TAG, "### Received image URI: " + imageUri);


            ShoppingList shoppingList = new ShoppingList(text, imageUri.toString()); // Assuming ShoppingList constructor accepts URI
            shoppingListViewModel.insert(shoppingList);
        }

    }


