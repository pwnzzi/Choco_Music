package com.example.choco_music.Interface;

import com.example.choco_music.model.VerticalData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitExService {

    String URL = "http://ec2-13-125-156-43.ap-northeast-2.compute.amazonaws.com:8080/song/";



    @GET("own/")
    Call<ArrayList<VerticalData>> getData2(@Query("id") int id);
}
