package uk.ac.le.co2103.part2;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class ShoppingListAdapter extends ListAdapter<ShoppingList, ShoppingListAdapter.ShoppingListViewHolder> {

    private static OnItemClickListener listener;

    public ShoppingListAdapter() {
        super(new ShoppingListDiff());
    }

    // Interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(int position);

        void onItemLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        ShoppingListAdapter.listener = listener;
    }

    @NonNull
    @Override
    public ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_one_shopping_list, parent, false);
        return new ShoppingListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListViewHolder holder, int position) {
        ShoppingList currentShoppingList = getItem(position);
        holder.bind(currentShoppingList);
    }

    static class ShoppingListViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final ImageView imageView;

        private final RelativeLayout relativeLayout;

        private ShoppingListViewHolder( View itemView) {
            super(itemView);
            this.nameTextView = itemView.findViewById(R.id.name);
            this.imageView = itemView.findViewById(R.id.imageview);
            relativeLayout = itemView.findViewById(R.id.relative_layout);


            // Set click listeners for both TextView and ImageView
            relativeLayout.setOnClickListener(v -> onItemClick(getAdapterPosition()));


        }

        private void onItemClick(int position) {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position);

            }
        }

        public void bind(ShoppingList shoppingList) {
            nameTextView.setText(shoppingList.getName());
            imageView.setImageURI(Uri.parse(shoppingList.getImage()));
        }
    }

    private static class ShoppingListDiff extends DiffUtil.ItemCallback<ShoppingList> {

        @Override
        public boolean areItemsTheSame(@NonNull ShoppingList oldItem, @NonNull ShoppingList newItem) {
            return oldItem.getListId() == newItem.getListId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull ShoppingList oldItem, @NonNull ShoppingList newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getImage().equals(newItem.getImage());
        }
    }
}
