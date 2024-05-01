package uk.ac.le.co2103.part2;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;


@Dao
public interface ShoppingDao {
    @Insert
    void insert(ShoppingList shoppingList);

    @Transaction
    @Query("DELETE FROM shoppinglist WHERE listId = :listId")
    void deleteShoppingListWithProducts(int listId);

    @Query("SELECT * FROM shoppinglist ORDER BY name ASC")
    LiveData<List<ShoppingList>> getShoppingList();

    @Query("DELETE FROM shoppinglist")
    void deleteAll();

    @Query("SELECT * FROM shoppinglist WHERE name = :name LIMIT 1")
    LiveData<ShoppingList> getShoppingListByName(String name);

}
