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
    private int image;

    // Constructor
    public ShoppingList(@NonNull String name, int image) {
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
