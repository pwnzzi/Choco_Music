package com.example.choco_music.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.choco_music.Login.SessionCallback;
import com.example.choco_music.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.auth.AuthType;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class Login_Fragment extends androidx.fragment.app.Fragment implements GoogleApiClient.OnConnectionFailedListener {

    public static OAuthLogin mOAuthLoginInstance;
    public Map<String,String> mUserInfoMap;
    public String NAVER_OAUTH_CLIENT_ID = "S65qKKdyPt2cYLbnin35" ,NAVER_OAUTH_CLIENT_SECRET = "T4Ocm7o6xq" , NAVER_OAUTH_CLIENT_NAME = "Choco Music"  ;
    public Context context;
    private Button kakao_login_btn, naver_login_btn;
    private SignInButton google_login_btn;
    private FirebaseAuth auth; // 파이어 베이스 인증 객체
    private GoogleApiClient googleApiClient; // 구글 API 클라이언트 객체
    private static final int RED_SIGN_GOOGLE = 100; //구글 로그인 결과 코드

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login_fragment, container, false);
        context = getContext();
        //카카오 로그인
        kakao_login_btn = view.findViewById(R.id.kakao_login_btn);
        kakao_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.kakao.auth.Session session=  com.kakao.auth.Session.getCurrentSession();

                session.addCallback(new SessionCallback());

                session.open(AuthType.KAKAO_LOGIN_ALL, Login_Fragment.this.getActivity());
            }
        });
        //구글 로그인
        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 초기화

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(),  Login_Fragment.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();


        google_login_btn = view.findViewById(R.id.btn_login_google);
        google_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RED_SIGN_GOOGLE);
            }
        });
        //네이버 로그인
        initNaver();

        naver_login_btn = view.findViewById(R.id.btn_login_naver);
        naver_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOAuthLoginInstance.startOauthLoginActivity(getActivity(),mOAuthLoginHandler);
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //구글 로그인 인증을 요청 했을 때 결과 값을 되돌려 받는곳
        if(requestCode == RED_SIGN_GOOGLE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount(); // account 라는 데이터는 구글 로그인 정보를 담고 있습니다.(닉네임, 프로필사진, 이메일주소..등)
                resultLogin(account); // 로그인 값 출력 수행하는 메소드
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void resultLogin(final GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        auth.signInWithCredential(credential).addOnCompleteListener(Login_Fragment.this.getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){ // 로그인 성공했으면..
                    Toast.makeText(getActivity(),"로그인 성공",Toast.LENGTH_SHORT).show();
                    Log.e("닉네임",account.getDisplayName());
                    Log.e("이미지 uri",String.valueOf(account.getPhotoUrl()));
                    Log.e("이메일",account.getEmail());
                }else{
                    Toast.makeText(getActivity(),"로그인 실패",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private  void initNaver(){
        //Naver Init
        mOAuthLoginInstance = OAuthLogin.getInstance();
        mOAuthLoginInstance.init(getContext(), NAVER_OAUTH_CLIENT_ID, NAVER_OAUTH_CLIENT_SECRET,NAVER_OAUTH_CLIENT_NAME);
        /*
         * 2015년 8월 이전에 등록하고 앱 정보 갱신을 안한 경우 기존에 설정해준 callback intent url 을 넣어줘야 로그인하는데 문제가 안생긴다.
         * 2015년 8월 이후에 등록했거나 그 뒤에 앱 정보 갱신을 하면서 package name 을 넣어준 경우 callback intent url 을 생략해도 된다.
         */
        //mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME, OAUTH_callback_intent_url);
    }
    /**
     * startOAuthLoginActivity() 호출시 인자로 넘기거나, OAuthLoginButton 에 등록해주면 인증이 종료되는 걸 알 수 있다.
     */
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginInstance.getAccessToken(context);
                String refreshToken = mOAuthLoginInstance.getRefreshToken(context);
                long expiresAt = mOAuthLoginInstance.getExpiresAt(context);
                String tokenType = mOAuthLoginInstance.getTokenType(context);
                Log.d("TAG",accessToken);
                Log.d("TAG",refreshToken);
                Log.d("TAG",String.valueOf(expiresAt));
                Log.d("TAG",tokenType);
                Log.d("TAG", mOAuthLoginInstance.getState(context).toString());
                new RequestApiTask().execute();
            } else {
                String errorCode = mOAuthLoginInstance.getLastErrorCode(context).getCode();
                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(context);
                Toast.makeText(context, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        };
    };



    private class RequestApiTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            String url = "https://openapi.naver.com/v1/nid/getUserProfile.xml";
            String at = mOAuthLoginInstance.getAccessToken(context);
            mUserInfoMap = requestNaverUserInfo(mOAuthLoginInstance.requestApi(context, at, url));
            return null;
        }

        protected void onPostExecute(Void content) {
            if (mUserInfoMap.get("email") == null) {
                Toast.makeText(context, "로그인 실패하였습니다.  잠시후 다시 시도해 주세요!!", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("TAG", String.valueOf(mUserInfoMap));
            }

        }
    }
    private Map<String,String> requestNaverUserInfo(String data) { // xml 파싱
        String f_array[] = new String[9];

        try {
            XmlPullParserFactory parserCreator = XmlPullParserFactory
                    .newInstance();
            XmlPullParser parser = parserCreator.newPullParser();
            InputStream input = new ByteArrayInputStream(
                    data.getBytes("UTF-8"));
            parser.setInput(input, "UTF-8");

            int parserEvent = parser.getEventType();
            String tag;
            boolean inText = false;
            boolean lastMatTag = false;

            int colIdx = 0;

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        tag = parser.getName();
                        if (tag.compareTo("xml") == 0) {
                            inText = false;
                        } else if (tag.compareTo("data") == 0) {
                            inText = false;
                        } else if (tag.compareTo("result") == 0) {
                            inText = false;
                        } else if (tag.compareTo("resultcode") == 0) {
                            inText = false;
                        } else if (tag.compareTo("message") == 0) {
                            inText = false;
                        } else if (tag.compareTo("response") == 0) {
                            inText = false;
                        } else {
                            inText = true;

                        }
                        break;
                    case XmlPullParser.TEXT:
                        tag = parser.getName();
                        if (inText) {
                            if (parser.getText() == null) {
                                f_array[colIdx] = "";
                            } else {
                                f_array[colIdx] = parser.getText().trim();
                            }
                            colIdx++;
                        }
                        inText = false;
                        break;
                    case XmlPullParser.END_TAG:
                        tag = parser.getName();
                        inText = false;
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
            Log.e("dd", "Error in network call", e);
        }
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("email"           ,f_array[0]);
        resultMap.put("nickname"        ,f_array[1]);
        resultMap.put("enc_id"          ,f_array[2]);
        resultMap.put("profile_image"   ,f_array[3]);
        resultMap.put("age"             ,f_array[4]);
        resultMap.put("gender"          ,f_array[5]);
        resultMap.put("id"              ,f_array[6]);
        resultMap.put("name"            ,f_array[7]);
        resultMap.put("birthday"        ,f_array[8]);
        Log.e("이메일",f_array[0]);
        Log.e("닉네임",f_array[1]);
        Log.e("나이",f_array[4]);
        Log.e("성별",f_array[5]);
        Log.e("이름",f_array[7]);
        return resultMap;
    }

}
