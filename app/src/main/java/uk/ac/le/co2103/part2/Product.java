package uk.ac.le.co2103.part2;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "products",
        foreignKeys = @ForeignKey(entity = ShoppingList.class,
                parentColumns = "listId",
                childColumns = "listId",
                onDelete = ForeignKey.CASCADE))
public class Product {

    @PrimaryKey(autoGenerate = true)
    private int productId;


    @ForeignKey(entity = ShoppingList.class,
            parentColumns = "listId",
            childColumns = "listId")
    private int listId; // Foreign key referencing ShoppingList

    @NonNull
    private String name;

    @NonNull
    private int quantity;

    @NonNull
    private String unit;

    public Product(int listId, @NonNull String name, int quantity, @NonNull String unit) {
        this.listId = listId;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @NonNull
    public String getUnit() {
        return unit;
    }

    public void setUnit(@NonNull String unit) {
        this.unit = unit;
    }
// Getters and setters
}
