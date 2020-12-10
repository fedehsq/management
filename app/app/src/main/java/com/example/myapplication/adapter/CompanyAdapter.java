package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.company.Company;

import java.util.ArrayList;

public class CompanyAdapter extends
        RecyclerView.Adapter<com.example.myapplication.adapter.CompanyAdapter.ViewHolder> {

    private final ArrayList<Company> companies;

    private View.OnClickListener mOnClickListener;

    public void setOnClickListner(View.OnClickListener myClickListener) {
        mOnClickListener = myClickListener;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView companyTextView;
        private final ImageButton deleteImageButton;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            companyTextView = view.findViewById(R.id.simple_text_view);
            deleteImageButton = view.findViewById(R.id.delete_image_button);

            view.setTag(this);
            deleteImageButton.setTag(this);

            view.setOnClickListener(mOnClickListener);
            deleteImageButton.setOnClickListener(mOnClickListener);
        }

        // get the items shown in the view holder
        public TextView getCompanyTextView() {
            return companyTextView;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param companies ArrayList<Day> containing the data to populate views to be used
     * by RecyclerView.
     */
    public CompanyAdapter(ArrayList<Company> companies) {
        this.companies = companies;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public com.example.myapplication.adapter.CompanyAdapter.ViewHolder onCreateViewHolder(
            ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_view_layout, viewGroup, false);

        return new com.example.myapplication.adapter.CompanyAdapter.ViewHolder(
                view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(
            com.example.myapplication.adapter.CompanyAdapter.ViewHolder viewHolder,
            final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getCompanyTextView().setText(companies.get(position).companyName);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return companies.size();
    }
}