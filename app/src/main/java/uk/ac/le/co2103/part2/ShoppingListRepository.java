package uk.ac.le.co2103.part2;
import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ShoppingListRepository {
    private static ShoppingDao shoppingDao;
    private ProductDao productDao;
    private LiveData<List<ShoppingList>> allItems;

    ShoppingListRepository(Application application) {
        ShoppingListDB db = ShoppingListDB.getDatabase(application);
        shoppingDao = db.shoppingDao();
        productDao = db.productDao();
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

    void insert(Product product) {
        ShoppingListDB.databaseWriteExecutor.execute(() -> {
            productDao.insert(product);
        });
    }

    LiveData<ShoppingList> getShoppingListByName(String name) {
        return shoppingDao.getShoppingListByName(name);
    }

    public static void deleteShoppingListWithProducts(ShoppingList shoppingList) {
        ShoppingListDB.databaseWriteExecutor.execute(() -> {
            shoppingDao.deleteShoppingListWithProducts(shoppingList.getListId());
        });
    }

}
