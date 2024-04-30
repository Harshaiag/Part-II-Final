package uk.ac.le.co2103.part2;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ShoppingListRepository {
    private ShoppingDao shoppingDao;
    private LiveData<List<ShoppingList>> allItems;

    ShoppingListRepository(Application application) {
        ShoppingListDB db = ShoppingListDB.getDatabase(application);
        shoppingDao = db.itemDao();
        allItems = shoppingDao.getShoppingList();
    }

    LiveData<List<ShoppingList>> getAllItems() {
        return allItems;
    }

    void insert(ShoppingList shoppingList) {
        ShoppingListDB.databaseWriteExecutor.execute(() -> {
            shoppingDao.insert(shoppingList);
        });
    }

}
