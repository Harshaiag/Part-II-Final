package uk.ac.le.co2103.part2;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "products",
        foreignKeys = @ForeignKey(entity = ShoppingList.class,
                parentColumns = "listId",
                childColumns = "listId",
                onDelete = ForeignKey.CASCADE))

public class Product {

    @NonNull
    private int listId;

    @NonNull
    private String name;

    @NonNull
    private int quantity;

    @NonNull
    private String unit;

    // Constructor
    public Product(int listId, @NonNull String name, int quantity, @NonNull String unit) {
        this.listId = listId;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    // Getters and setters


    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    // @NonNull
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

    // @NonNull
    public String getUnit() {
        return unit;
    }

    public void setUnit(@NonNull String unit) {
        this.unit = unit;
    }
}
