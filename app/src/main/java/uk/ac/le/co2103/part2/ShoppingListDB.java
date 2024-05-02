package uk.ac.le.co2103.part2;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ShoppingList.class, Product.class}, version = 3, exportSchema = false)
public abstract class ShoppingListDB extends RoomDatabase {

    public abstract ShoppingDao shoppingDao();
    public abstract ProductDao productDao();

    private static volatile ShoppingListDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 15;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ShoppingListDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ShoppingListDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ShoppingListDB.class, "shoppingcart_db")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                ShoppingDao shoppingDao = INSTANCE.shoppingDao();
                ProductDao productDao = INSTANCE.productDao();

                // Optional: initialize the database with some default data if needed
                // For example:


            });
        }
    };
}
