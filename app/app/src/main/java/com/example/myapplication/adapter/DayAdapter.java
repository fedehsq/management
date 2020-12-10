package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.day.Day;

import java.util.ArrayList;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {

    public final ArrayList<Day> days;

    private View.OnClickListener mOnItemClickListener;


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView companyTextView;
        private final TextView truckTextView;
        private final TextView dateTextView;
        private final ImageButton deleteImageButton;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            companyTextView = view.findViewById(R.id.company_text_view);
            truckTextView = view.findViewById(R.id.truck_brand_text_view);
            dateTextView = view.findViewById(R.id.date_text_view);
            deleteImageButton = view.findViewById(R.id.delete_image_button);
            view.setTag(this);
            deleteImageButton.setTag(this);
            view.setOnClickListener(mOnItemClickListener);
            deleteImageButton.setOnClickListener(mOnItemClickListener);
        }

        // get the items shown in the view holder
        public TextView getCompanyTextView() {
            return companyTextView;
        }

        public TextView getTruckTextView() {
            return truckTextView;
        }

        public TextView getDateTextView() {
            return dateTextView;
        }

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param days ArrayList<Day> containing the data to populate views to be used
     * by RecyclerView.
     */
    public DayAdapter(ArrayList<Day> days) {
        this.days = days;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.day_list_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getCompanyTextView().setText(days.get(position).company);
        viewHolder.getTruckTextView().setText(days.get(position).truck.getTruckBrand());
        viewHolder.getDateTextView().setText(days.get(position).date);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return days.size();
    }

    //TODO: Step 2 of 4: Assign itemClickListener to your local View.OnClickListener variable
    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }
}
