package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.Truck;

import java.util.ArrayList;

public class TruckAdapter extends
        RecyclerView.Adapter<com.example.myapplication.adapter.TruckAdapter.ViewHolder> {

    private final ArrayList<Truck> trucks;
    private View.OnClickListener mOnClickListener;

    public void setOnClickListener(View.OnClickListener myClickListener) {
        mOnClickListener = myClickListener;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder  {
        private final TextView truckTextView;
        private final ImageButton deleteImageButton;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            truckTextView = view.findViewById(R.id.simple_text_view);
            deleteImageButton = view.findViewById(R.id.delete_image_button);
            deleteImageButton.setTag(this);
            view.setTag(this);
            deleteImageButton.setOnClickListener(mOnClickListener);
            view.setOnClickListener(mOnClickListener);
        }

        // get the items shown in the view holder
        public TextView getTruckTextView() {
            return truckTextView;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param trucks ArrayList<Day> containing the data to populate views to be used
     * by RecyclerView.
     */
    public TruckAdapter(ArrayList<Truck> trucks) {
        this.trucks = trucks;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public com.example.myapplication.adapter.TruckAdapter.ViewHolder onCreateViewHolder(
            ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_view_layout, viewGroup, false);

        return new com.example.myapplication.adapter.TruckAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(
            com.example.myapplication.adapter.TruckAdapter.ViewHolder viewHolder,
            final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTruckTextView().setText(trucks.get(position).getTruckBrand());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return trucks.size();
    }
}