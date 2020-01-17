package com.example.choco_music.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.choco_music.Holder.Search_ViewHolder;
import com.example.choco_music.R;
import com.example.choco_music.model.SearchData;
import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<Search_ViewHolder> {

    private ArrayList<SearchData> SearchDatas ;
    public void setData(ArrayList<SearchData> list){
        SearchDatas = list;
    }

    @NonNull
    @Override
    public Search_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //사용할 아이템 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_recycler_items,parent,false);
        Search_ViewHolder holder = new Search_ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull Search_ViewHolder holder, int position) {

        SearchData data = SearchDatas.get(position);
        holder.vocal.setText(data.getVocal());
        holder.title.setText(data.getTitle());
    }

    @Override
    public int getItemCount() {
        return SearchDatas.size();
    }

}