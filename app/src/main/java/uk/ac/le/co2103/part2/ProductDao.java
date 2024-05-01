package uk.ac.le.co2103.part2;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    void insert(Product product);

    @Query("SELECT * FROM products WHERE listId = :listId")
    LiveData<List<Product>> getProductsByListId(int listId);

    @Query("SELECT * FROM products WHERE productId = :productId")
    LiveData<Product> getProductById(int productId);

    @Query("UPDATE products SET quantity = :quantity WHERE productId = :productId")
    void updateQuantity(int productId, int quantity);

    @Query("DELETE FROM products WHERE productId = :productId")
    void deleteProduct(int productId);
}
