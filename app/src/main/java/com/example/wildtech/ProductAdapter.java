package com.example.wildtech;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<item> itemList;

    public ProductAdapter(Context context, List<item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        if (itemList != null) {
            item product = itemList.get(position);

            // Bind product data to the views in the layout
            holder.titleTextView.setText(product.getName());
            holder.locationTextView.setText(product.getLocation());
            holder.priceTextView.setText(product.getPrice());
            holder.ratingTextView.setText(String.valueOf(product.getRating()));

            Picasso.get().load(product.getImageURL()).fit().into(holder.imageView);

            // Set an OnClickListener for the item
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Launch the Item_details activity and pass the product ID
                    Intent intent = new Intent(context, Item_details.class);
                    intent.putExtra("product_id", product.getProductID());
                    context.startActivity(intent);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView locationTextView;
        TextView priceTextView;
        TextView ratingTextView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.popular_img);
            titleTextView = itemView.findViewById(R.id.popular_text);
            locationTextView = itemView.findViewById(R.id.location_text);
            priceTextView = itemView.findViewById(R.id.price_text);
            ratingTextView = itemView.findViewById(R.id.rating_text);
        }
    }


}

