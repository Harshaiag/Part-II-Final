package uk.ac.le.co2103.part2;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;



@Entity(tableName = "shoppinglist")
public class ShoppingList {

    @PrimaryKey(autoGenerate = true)
    private int listId;


    @ColumnInfo(name = "name")

    private String name;
    private String image;

    // Constructor
    public ShoppingList(@NonNull String name, String image) {
        this.name = name;
        this.image = image;
    }



    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
