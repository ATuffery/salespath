package fr.iutrodez.salespathapp.contact;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import fr.iutrodez.salespathapp.R;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private ArrayList<Contact> contacts;

    public ContactAdapter(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.tvName.setText(contact.getName());
        holder.tvDetails.setText(contact.getAddress());
        if (contact.noCheckbox()) {
            holder.cbContact.setVisibility(View.GONE);
        } else {
            holder.cbContact.setChecked(contact.isChecked());
            holder.cbContact.setOnCheckedChangeListener((buttonView, isChecked) -> {
                contact.setChecked(isChecked ? ContactCheckbox.CHECKED : ContactCheckbox.UNCHECKED);
            });
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDetails;
        CheckBox cbContact;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDetails = itemView.findViewById(R.id.tv_details);
            cbContact = itemView.findViewById(R.id.cb_contact);
        }
    }
}
