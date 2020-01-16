package com.example.choco_music.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.choco_music.Holder.Playlist_ViewHolder;
import com.example.choco_music.R;
import com.example.choco_music.model.Music_Playlist_Data;
import java.util.List;

public class Playlist_Apdapter extends RecyclerView.Adapter<Playlist_ViewHolder> {

    private List<Music_Playlist_Data> music_playlist;

    public void setData(List<Music_Playlist_Data> list){
        music_playlist = list;
    }


    @NonNull
    @Override
    public Playlist_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_fragment_items,parent,false);
        return new Playlist_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Playlist_ViewHolder holder, int position) {

        Music_Playlist_Data data = music_playlist.get(position);

        holder.vocal.setText(data.getVocal());
        holder.title.setText(data.getTitle());

    }

    @Override
    public int getItemCount() {
        return music_playlist.size();
    }
}
