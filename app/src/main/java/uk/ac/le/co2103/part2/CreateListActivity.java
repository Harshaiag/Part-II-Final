package uk.ac.le.co2103.part2;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class CreateListActivity extends AppCompatActivity {
    private static final String TAG = CreateListActivity.class.getSimpleName();
    public ActivityResultLauncher<Intent> launcher;

    private EditText editTextItem;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        imageView = findViewById(R.id.imageView);
        Button galleryButton = findViewById(R.id.gallery);
        editTextItem = findViewById(R.id.editTextName);
        Button createButton = findViewById(R.id.createButton);

       // setupGalleryButton(galleryButton);
        setupCreateButton(createButton);
        setupActivityResultLauncher();
    }

   /* private void setupGalleryButton(Button galleryButton) {
        galleryButton.setOnClickListener(view -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            launcher.launch(galleryIntent);
        });
    }*/

    private void setupCreateButton(Button createButton) {
        createButton.setOnClickListener(view -> {
            String text = editTextItem.getText().toString().trim();

            if (TextUtils.isEmpty(text)) {
                Log.i(TAG, "Empty text field could be controlled in UI (Save button disabled)");
                setResult(RESULT_CANCELED);
            } else {
                Log.i(TAG, "Adding item to list");
                Intent replyIntent = new Intent();
                replyIntent.putExtra("Text", text);
               //  replyIntent.putExtra("imageAlpha", imageView.getImageAlpha());
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }

    private void setupActivityResultLauncher() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            imageView.setImageURI(selectedImageUri);
                        }
                    }
                });
    }
}
