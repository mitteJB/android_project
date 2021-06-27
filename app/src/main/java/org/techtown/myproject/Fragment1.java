package org.techtown.myproject;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment1 extends Fragment {
    public CallbackStringData callbackStringData;
    public ExitFragment exitFragment;
    final static String TAG = "프래그먼트1";
    static String str = "초기값";
    TextView textView2;
    static DatabaseHelper dbHelper;
    static SQLiteDatabase db;

    public interface ExitFragment{
        public void exitFragmentMethod();
    }

    // 액티비티에 인터페이스를 이용해서 문자열 데이터를 넘겨보자 start
    public interface CallbackStringData {
        // 추상메소드
        public abstract void callbackStringDataMethod(String string, String string2);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "온어태치");
        if (context instanceof CallbackStringData) {
            callbackStringData = (CallbackStringData) context;
        }
        if (context instanceof ExitFragment) {
            exitFragment = (ExitFragment) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbackStringData = null;
        exitFragment = null;
    }
    // 액티비티에 인터페이스를 이용해서 문자열 데이터를 넘겨보자 end


    public Fragment1() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "온크리에이트;;");

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "온스타트");
//        Log.d(TAG, str);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        String str = data.getStringExtra("date");
        Log.d(TAG, "액티비티리졀트");
//        Log.d(TAG, str);
        str = data.getStringExtra("date");
        textView2.setText(str);
    }

    @Override
    public void onResume() {
        super.onResume();
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "뷰크리에이트");

        TextView textView = view.findViewById(R.id.textView);
        textView2 = view.findViewById(R.id.textView2);
        EditText editText = view.findViewById(R.id.editTextTextPersonName);
        Button button = view.findViewById(R.id.button);
        Button button2 = view.findViewById(R.id.button2);
        Button button3 = view.findViewById(R.id.button3);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 메소드에 str을 넣어서, 액티비티에 보내자.
                String str = editText.getText().toString();
                String date_text = textView2.getText().toString();
                if (callbackStringData != null) {
                    callbackStringData.callbackStringDataMethod(str, date_text);
                }

                // 데이터베이스관련
                db = SQLiteDatabase.openOrCreateDatabase("/data/data/org.techtown.myproject/databases/ItemData", null);
                String tableName = "ItemTable";
                db.execSQL("create table if not exists " + tableName + "("
                + " _id integer NOT NULL PRIMARY KEY autoincrement, "
                + " text_main text, "
                + " text_edit text, "
                + " checkbox int, "
                + " cardBackGroundColor int, "
                + " time text)");

                String text_main = editText.getText().toString();
                String text_edit = "";
                int checkbox = 0;
                int cardBackGroundColor = -1;
                String time = textView2.getText().toString();
                db.execSQL("insert into " + tableName
                        + "(text_main, text_edit, checkbox, cardBackGroundColor, time) "
                        + " values "
                        + "( '"+text_main+"', '"+text_edit+"', '"+checkbox+"', '"+cardBackGroundColor+"', '"+time+"' )");

                String dataInfo = "텍스트 : " + text_main + ", 에딧텍스트 : " + text_edit + ", 체크박스 : " + checkbox + ", 배경값 : " + cardBackGroundColor + ", 시간 : " +time;
                Log.d(TAG, dataInfo);
//                String text_main = editText.getText().toString();
//                String text_edit = "";
//                int checkbox = 0;
//                int cardBackGroundColor = -1;
//                String time = textView2.getText().toString();

                // 데이터베이스 테이블 생성
//                String tableName = "ItemTable";
//                db.execSQL("create table if not exists " + tableName + "("
//                + " _id integer NOT NULL PRIMARY KEY autoincrement, "
//                + " text_main text, "
//                + " text_edit text, "
//                + " checkbox int, "
//                + " cardBackGroundColor int, "
//                + " time text)");

                // 데이터베이스에 값 입력
//                db.execSQL("insert into " + tableName
//                        + "(text_main, text_edit, checkbox, cardBackGroundColor, time) "
//                        + " values "
//                        + "( '"+text_main+"', '"+text_edit+"', '"+checkbox+"', '"+cardBackGroundColor+"', '"+time+"' )");

                // 열린 fragment1을 닫는다.
                closeFragment();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 알람 액티비티를 띄워야 한다.
                Intent intent = new Intent(getContext(), AlarmActivity.class);
                startActivityForResult(intent, 0);

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitFragment.exitFragmentMethod();
                closeFragment();
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 프래그먼트에 버튼 사용하기
        Log.d(TAG, "온크리에이트뷰");
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_1, container, false);
        return rootView;
    }

    private void showToast2(String string) {
        Toast.makeText(getContext(), string, Toast.LENGTH_SHORT).show();
    }

    private void setText(String string) {

    }

    private void closeFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(Fragment1.this).commit();
        fragmentManager.popBackStack();
    }



}