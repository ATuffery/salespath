package fr.iutrodez.salespathapp.card;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.iutrodez.salespathapp.R;

public class CardWithTwoLinesAdapteur extends RecyclerView.Adapter<CardWithTwoLinesAdapteur.ItineraryViewHolder> {
    private List<CardWithTwoLines> itemList;

    public CardWithTwoLinesAdapteur(List<CardWithTwoLines> itemList) {
        this.itemList = itemList;
    }

    public static class ItineraryViewHolder extends RecyclerView.ViewHolder {
        TextView cardName, cardStatus, cardLine1, cardLine2;
        Button cardBtn;

        public ItineraryViewHolder(View itemView) {
            super(itemView);
            cardName = itemView.findViewById(R.id.cardName);
            cardStatus = itemView.findViewById(R.id.cardStatus);
            cardLine1 = itemView.findViewById(R.id.cardLine1);
            cardLine2 = itemView.findViewById(R.id.cardLine2);
            cardBtn = itemView.findViewById(R.id.cardBtn);
        }
    }
    @NonNull
    @Override
    public ItineraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_with_two_lines, parent, false);
        return new ItineraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItineraryViewHolder holder, int position) {
        CardWithTwoLines item = itemList.get(position);
        holder.cardName.setText(item.getTitle());
        holder.cardStatus.setText(item.getStatus());
        holder.cardLine1.setText(item.getLine1());
        holder.cardLine2.setText(item.getLine2());
        holder.cardBtn.setText(item.getBtnText());

        holder.cardBtn.setOnClickListener(v -> {
            if (item.getOnClickAction() != null) {
                item.getOnClickAction().run();
            }
        });
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
