package com.example.choco_music.activities;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.choco_music.R;
import com.example.choco_music.adapters.ListViewAdapter;
import com.example.choco_music.model.AudioModel;
import java.util.ArrayList;
import java.util.List;

public class SongSettingActivity extends AppCompatActivity {
    private Button upload_mp3_btn;
    private ListView listView;
    private TextView SongName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_song);

        setup_view();
    }
    private void setup_view(){
        SongName = findViewById(R.id.song_file_name);
        listView = findViewById(R.id.SongList);
        upload_mp3_btn = findViewById(R.id.upload_mp3);
        upload_mp3_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllAudioFromDevice(getApplicationContext());
                listView.setVisibility(View.VISIBLE);
            }
        });
    }
    public List<String> getAllAudioFromDevice(final Context context) {
        final List<String> tempAudioList = new ArrayList<>();

      //  final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,tempAudioList );
         ListViewAdapter adapter;
         adapter = new ListViewAdapter();
        listView.setAdapter(adapter);

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA,MediaStore.Audio.AudioColumns.TITLE ,MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,};
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);

        if (c != null) {
            while (c.moveToNext()) {
                String path = c.getString(0);   // Retrieve path.
                String name = c.getString(1);   // Retrieve name.
                String album = c.getString(2);  // Retrieve album name.
                String artist = c.getString(3); // Retrieve artist name.

              adapter.addItem(name,path,album,artist);
            }
            c.close();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  Uri myUri = Uri.parse(tempAudioList.get(position).getaPath());
                AudioModel audioModel = (AudioModel)parent.getItemAtPosition(position);

                listView.setVisibility(View.GONE);
                String file_name = audioModel.getaName();

                SongName.setText(file_name);
            }
        });
        return tempAudioList;
    }
}
