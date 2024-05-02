package uk.ac.le.co2103.part2;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class ProductRepository {

    private ProductDao productDao;
    private LiveData<List<Product>> productsByListId;

    ProductRepository(Application application) {
        ShoppingListDB db = ShoppingListDB.getDatabase(application);
        productDao = db.productDao();
    }

    LiveData<List<Product>> getProductsByListId(int listId) {
        return productDao.getProductsByListId(listId);
    }

    Product getProductById(int productId) {
        return productDao.getProductById(productId);
    }

    void insert(Product product) {
        ShoppingListDB.databaseWriteExecutor.execute(() -> {
            productDao.insert(product);
        });
    }

    void updateQuantity(int productId, int quantity) {
        ShoppingListDB.databaseWriteExecutor.execute(() -> {
            productDao.updateQuantity(productId, quantity);
        });
    }

    void deleteProduct(int productId) {
        ShoppingListDB.databaseWriteExecutor.execute(() -> {
            productDao.deleteProduct(productId);
        });
    }

    // Getter for ProductDao
    public ProductDao getProductDao() {
        return productDao;
    }

    // Getter for LiveData<List<Product>> productsByListId
    public LiveData<List<Product>> getProductsByListIdLiveData(int listId) {
        if (productsByListId == null) {
            productsByListId = productDao.getProductsByListId(listId);
        }
        return productsByListId;
    }



    int getProductCountByNameAndListId(String name, int listId) {
        return productDao.getProductCountByNameAndListId(name, listId);
    }
}
