package edu.northeastern.numad22fa_team15.firebaseStickerHistoryRecyclerUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

import edu.northeastern.numad22fa_team15.R;
import edu.northeastern.numad22fa_team15.model.StickerRecord;

public class StickerHistoryAdapter  extends RecyclerView.Adapter<StickerHistoryViewHolder> {

    private List<StickerRecord> results;
    private final Context context;

    public StickerHistoryAdapter(@NonNull List<StickerRecord> results, @NonNull Context context) {
        this.results = results;
        this.context = context;
    }

    @NonNull
    @Override
    public StickerHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sticker_record, parent, false);
        return new StickerHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerHistoryViewHolder holder, int position) {
        // TODO: Need to consider the edge case that different versions of the app may contain different stickers.

        holder.itemStickerImage.setImageResource(results.get(position).getStickerResourceID());
        holder.itemStickerID.setText(String.valueOf(results.get(position).getStickerResourceID()));
        holder.itemSenderUsername.setText(results.get(position).getSender());
        // Convert timestamp long value to Date to String
        long timestampLong = results.get(position).getTimestamp();
        String timestamp = new Date(timestampLong).toString();
        System.out.println(timestamp);
        holder.itemTimestamp.setText(timestamp);
    }

    /**
     * Return the number of sticker records in the result list.
     * @return the number of sticker records in the result list
     */
    @Override
    public int getItemCount() {
        return results.size();
    }
}
