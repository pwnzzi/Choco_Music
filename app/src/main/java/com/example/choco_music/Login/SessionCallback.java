package com.example.choco_music.Login;

import android.util.Log;

import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import static android.content.ContentValues.TAG;

public class SessionCallback implements ISessionCallback {
    // 로그인에 성공한 상태

    @Override

    public void onSessionOpened() {

        requestMe();

    }



    // 로그인에 실패한 상태

    @Override

    public void onSessionOpenFailed(KakaoException exception) {

        Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());

    }



    // 사용자 정보 요청

    public void requestMe() {

        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                super.onFailure(errorResult);
                Log.e(TAG, "requestMe onFailure message : " + errorResult.getErrorMessage());
            }

            @Override
            public void onFailureForUiThread(ErrorResult errorResult) {
                super.onFailureForUiThread(errorResult);
                Log.e(TAG, "requestMe onFailureForUiThread message : " + errorResult.getErrorMessage());
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e(TAG, "requestMe onSessionClosed message : " + errorResult.getErrorMessage());
            }

            @Override
            public void onSuccess(MeV2Response result) {
                Log.e(TAG, "requestMe onSuccess message : " + result.getKakaoAccount().getEmail() + " " + result.getId() + " " + result.getNickname());
            }

        });
    }

    /** 로그아웃시 **/
    public void onClickLogout() {

        UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e(TAG, "카카오 로그아웃 onSessionClosed");
            }

            @Override
            public void onNotSignedUp() {
                Log.e(TAG, "카카오 로그아웃 onNotSignedUp");
            }

            @Override
            public void onSuccess(Long result) {
                Log.e(TAG, "카카오 로그아웃 onSuccess");
            }
        });

        // 사용자정보 요청 결과에 대한 Callback
/*
        UserManagement.getInstance().requestMe(new MeResponseCallback() {

            // 세션 오픈 실패. 세션이 삭제된 경우,

            @Override

            public void onSessionClosed(ErrorResult errorResult) {

                Log.e("SessionCallback :: ", "onSessionClosed : " + errorResult.getErrorMessage());

            }



            // 회원이 아닌 경우,

            @Override

            public void onNotSignedUp() {

                Log.e("SessionCallback :: ", "onNotSignedUp");

            }



            // 사용자정보 요청에 성공한 경우,

            @Override

            public void onSuccess(UserProfile userProfile) {



                Log.e("SessionCallback :: ", "onSuccess");



                String nickname = userProfile.getNickname();

                String email = userProfile.getEmail();

                String profileImagePath = userProfile.getProfileImagePath();

                String thumnailPath = userProfile.getThumbnailImagePath();

                String UUID = userProfile.getUUID();

                long id = userProfile.getId();



                Log.e("Profile : ", nickname + "");

                Log.e("Profile : ", email + "");

                Log.e("Profile : ", profileImagePath  + "");

                Log.e("Profile : ", thumnailPath + "");

                Log.e("Profile : ", UUID + "");

                Log.e("Profile : ", id + "");

            }



            // 사용자 정보 요청 실패

            @Override

            public void onFailure(ErrorResult errorResult) {

                Log.e("SessionCallback :: ", "onFailure : " + errorResult.getErrorMessage());

            }

        });*/

    }
}
