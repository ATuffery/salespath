package fr.iutrodez.salespathapp.route;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fr.iutrodez.salespathapp.R;
import fr.iutrodez.salespathapp.entity.Contact;
import fr.iutrodez.salespathapp.enums.ContactCheckbox;

public class RouteStepAdapter extends RecyclerView.Adapter<RouteStepAdapter.ContactViewHolder> {

    private ArrayList<Contact> contacts;

    public RouteStepAdapter(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step_route, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.tvName.setText(contact.getName());
        holder.tvDetails.setText(contact.getCompany());

        switch (contact.getStatus()) {
            case UNVISITED:
                holder.icon.setImageResource(R.drawable.unvisited);
                break;
            case VISITED:
                holder.icon.setImageResource(R.drawable.visited);
                break;
            case SKIPPED:
                holder.icon.setImageResource(R.drawable.skiped);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDetails;
        ImageView icon;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDetails = itemView.findViewById(R.id.tv_details);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}
