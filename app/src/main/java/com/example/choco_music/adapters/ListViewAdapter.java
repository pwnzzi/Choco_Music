package com.example.choco_music.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.choco_music.R;
import com.example.choco_music.model.AudioModel;
import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private ArrayList<AudioModel> listViewItemList = new ArrayList<AudioModel>() ;
    public ListViewAdapter(){}
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.chart_recycler_items,parent,false);

        }
        AudioModel audioModel = listViewItemList.get(position);
        TextView title = convertView.findViewById(R.id.chart_title);
        TextView vocal = convertView.findViewById(R.id.chart_vocal);
        ImageView img = convertView.findViewById(R.id.music_icon);

       img.setVisibility(View.GONE);
        vocal.setText(audioModel.getaArtist());
        title.setText(audioModel.getaName());
        title.setTextColor(Color.BLACK);
        vocal.setTextColor(Color.BLACK);

        return convertView;
    }
    public void addItem(String name, String path, String album, String vocal){
         AudioModel audioModel = new AudioModel();

         audioModel.setaName(name);
         audioModel.setaPath(path);
         audioModel.setaAlbum(album);
         audioModel.setaArtist(vocal);
         Log.e("album",album);

        listViewItemList.add(audioModel);
    }
}
