package com.example.choco_music.Interface;

import com.example.choco_music.model.AlbumData;
import com.example.choco_music.model.ChartData;
import com.example.choco_music.model.CoverData;
import com.example.choco_music.model.TodaySongData;
import com.example.choco_music.model.VerticalData;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitExService {

    String URL = "http://ec2-15-164-222-230.ap-northeast-2.compute.amazonaws.com:8080/";

    @GET("song/own/")
    Call<ArrayList<VerticalData>> getData_Original();

    @GET("song/covered/")
    Call<ArrayList<CoverData>> getData_Cover();

    @GET("album/own/")
    Call<ArrayList<AlbumData>> AlbumData_Original(@Query("id") int id);

    @GET("album/covered/")
    Call<ArrayList<AlbumData>> AlbumData_Cover(@Query("id") int id);

    @GET("song/recommended/today/")
    Call<ArrayList<TodaySongData>> getData_Song_Today();

    @FormUrlEncoded
    @POST("evaluation/score/own/")
    Call<VerticalData> postData_Original(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("evaluation/score/covered/")
    Call<CoverData> postData_Cover(@FieldMap HashMap<String, Object> param);


}