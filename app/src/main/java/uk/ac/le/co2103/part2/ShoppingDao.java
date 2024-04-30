package uk.ac.le.co2103.part2;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface ShoppingDao {
    @Insert
    void insert(ShoppingList shoppingList);

    @Query("SELECT * FROM shoppinglist ORDER BY name ASC")
    LiveData<List<ShoppingList>> getShoppingList();

    @Query("DELETE FROM shoppinglist")
    void deleteAll();
}
