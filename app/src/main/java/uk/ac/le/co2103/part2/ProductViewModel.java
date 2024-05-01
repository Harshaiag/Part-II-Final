package uk.ac.le.co2103.part2;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private ProductRepository repo;
    private LiveData<List<Product>> productsByListId;

    public ProductViewModel(Application application) {
        super(application);
        repo = new ProductRepository(application);
    }

    public LiveData<List<Product>> getProductsByListId(int listId) {
        if (productsByListId == null) {
            productsByListId = repo.getProductsByListId(listId);
        }
        return productsByListId;
    }

    public LiveData<Product> getProductById(int productId) {
        return repo.getProductById(productId);
    }

    public void insert(Product product) {
        repo.insert(product);
    }

    public void updateQuantity(int productId, int quantity) {
        repo.updateQuantity(productId, quantity);
    }

    public void deleteProduct(int productId) {
        repo.deleteProduct(productId);
    }
}
