package com.iyaadam.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    private Context context;
    private List<Music> musicList;
    private OnMusicClickListener listener;

    public interface OnMusicClickListener {
        void onMusicClick(int position);
    }

    public MusicAdapter(Context context, List<Music> musicList, OnMusicClickListener listener) {
        this.context = context;
        this.musicList = musicList;
        this.listener = listener;
    }

    @Override
    public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_item, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MusicViewHolder holder, int position) {
        Music music = musicList.get(position);
        holder.title.setText(music.title);
        holder.artist.setText(music.artist);
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMusicClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public static class MusicViewHolder extends RecyclerView.ViewHolder {
        TextView title, artist;
        ImageView thumbnail;

        public MusicViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.music_title);
            artist = itemView.findViewById(R.id.music_artist);
            thumbnail = itemView.findViewById(R.id.music_thumbnail);
        }
    }
}
