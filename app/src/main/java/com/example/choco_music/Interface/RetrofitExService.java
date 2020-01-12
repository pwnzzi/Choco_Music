package com.example.choco_music.Interface;

import com.example.choco_music.model.ChartData;
import com.example.choco_music.model.VerticalData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitExService {

    String URL = "http://ec2-15-164-222-230.ap-northeast-2.compute.amazonaws.com:8080/";


 //   @GET("song/own/")
 //   Call<ArrayList<VerticalData>> getData2(@Query("id") int id);

    @GET("song/own/")
    Call<ArrayList<VerticalData>> getData2();

    @GET("song/own/")
    Call<ArrayList<ChartData>> getData1();

    @GET("elbum/own/")
    Call<ArrayList<VerticalData>> elbumData(@Query("id") int id);


}
