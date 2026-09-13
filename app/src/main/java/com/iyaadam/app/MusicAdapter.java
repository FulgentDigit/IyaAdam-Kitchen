package com.iyaadam.app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

private final Context context;
private final List<Music> musicList;
private final OnMusicClickListener listener;

public interface OnMusicClickListener {
    void onMusicClick(int position);
}

public MusicAdapter(
        Context context,
        List<Music> musicList,
        OnMusicClickListener listener) {

    this.context = context;
    this.musicList = musicList;
    this.listener = listener;
}

@NonNull
@Override
public MusicViewHolder onCreateViewHolder(
        @NonNull ViewGroup parent,
        int viewType) {

    View view = LayoutInflater.from(context)
            .inflate(R.layout.music_item, parent, false);

    return new MusicViewHolder(view);
}

@Override
public void onBindViewHolder(
        @NonNull MusicViewHolder holder,
        int position) {

    Music music = musicList.get(position);

    holder.title.setText(
            music.title != null && !music.title.isEmpty()
                    ? music.title
                    : "Untitled"
    );

    holder.artist.setText(
            music.artist != null && !music.artist.isEmpty()
                    ? music.artist
                    : "IyaAdam Kitchen"
    );

    // Load album artwork from the server
    if (music.albumArt != null &&
            !music.albumArt.isEmpty()) {

        Glide.with(context)
                .load(music.albumArt)
                .placeholder(
                        android.R.drawable.ic_menu_gallery
                )
                .error(
                        android.R.drawable.ic_menu_gallery
                )
                .centerCrop()
                .into(holder.thumbnail);

    } else {

        holder.thumbnail.setImageResource(
                android.R.drawable.ic_menu_gallery
        );
    }

    holder.itemView.setOnClickListener(v -> {

        if (listener != null) {
            int adapterPosition =
                    holder.getBindingAdapterPosition();

            if (adapterPosition !=
                    RecyclerView.NO_POSITION) {

                listener.onMusicClick(adapterPosition);
            }
        }
    });
}

@Override
public int getItemCount() {
    return musicList != null
            ? musicList.size()
            : 0;
}

public static class MusicViewHolder
        extends RecyclerView.ViewHolder {

    TextView title;
    TextView artist;
    ImageView thumbnail;

    public MusicViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(
                R.id.music_title
        );

        artist = itemView.findViewById(
                R.id.music_artist
        );

        thumbnail = itemView.findViewById(
                R.id.music_thumbnail
        );
    }
}

}
