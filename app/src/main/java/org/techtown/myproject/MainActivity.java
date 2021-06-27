package org.techtown.myproject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Iterator;

// 프래그먼트1에서 데이터를 받아오기 위해 프래그먼트의 인터페이스를 implements 한다.
public class MainActivity extends AppCompatActivity implements Fragment1.CallbackStringData, Fragment1.ExitFragment {

    final static String TAG = "메인액티비티";
    RecyclerView recyclerView; // 목록이 표시될 뷰
    Fragment1 fragment1; // 데이터를 입력할 입력화면
    ItemAdapter adapter = new ItemAdapter(); // 어댑터
    BottomNavigationView bottomNavigationView; // 하단탭
    FloatingActionButton fab; // 추가버튼

    NotificationManager manager;
    private static final String CHANNEL_ID = "channel1";
    private static final String CHANNEL_NAME = "channel1";

    private static final String CHANNEL_ID2 = "channel2";
    private static final String CHANNEL_NAME2 = "channel2";

    boolean bottomBool = true; // 하단탭 생성과 삭제를 위한 플래그 전역변수

    SQLiteDatabase db;
    DatabaseHelper dbHelper;


    // 테이터베이스 생성하기


//    void excuteQuery() {
//        showLog("쿼리입력 호출됨");
//
//        Cursor cursor = db.rawQuery("select _id, name, age, mobile from emp", null);
//        int recordCount = cursor.getCount();
//        showLog(String.valueOf(recordCount));
//
//        for (int i = 0; i < recordCount; i++) {
//            cursor.moveToNext();
//            int id = cursor.getInt(0);
//            String name = cursor.getString(1);
//            int age = cursor.getInt(2);
//            String mobile = cursor.getString(3);
//
//            showLog("레코드 출력(" + i + ")" + id + " , " + name + " , " + age + " , " + mobile);
//        }
//        cursor.close();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 뷰와 레이아웃 연결
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        bottomNavigationView = findViewById(R.id.bottonView);

        // 리사이클러뷰 사용을 위한 레이아웃 매니져 연결.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false); // 리사이클러뷰의 레이아웃 설정
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter); // 어댑터와 리사이클러뷰 연결

        // 데이타베이스 만들기
//        createDatabase();
//        db = openOrCreateDatabase("ItemData", MODE_PRIVATE, null);

        // 데이터베이스 초기화하고 만들기
        // 처음 시작시 데이터베이스 지우기
        getApplicationContext().deleteDatabase("ItemData");

//        db = openOrCreateDatabase("ItemData", MODE_PRIVATE, null);
//        db.execSQL("create table if not exists ItemTable ("
//                + " _id integer NOT NULL PRIMARY KEY autoincrement, "
//                + " text_main text, "
//                + " text_edit text, "
//                + " checkbox int, "
//                + " cardBackGroundColor int, "
//                + " time text)");
//        loadData();

        // 하단탭이 있으면 안보이게 하기
        if (bottomBool) {
            bottomNavigationView.setVisibility(View.INVISIBLE);
            bottomBool = false;
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment1();
//                showNoti1();
//                playRingtone();
//                createDatabase();
//                excuteQuery();
            }
        });


        // 하단탭 아이템 선택시 실행될 기능을 넣자.
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.tab1:
                                // 1번탭 선택시 실행될 기능 선택 항목 삭제
                                delCheckedItem();
                                bottomView();
                                return true;

                            case R.id.tab2:
                                // 2번탭 선택시 실행될 기능 - 전체선택
                                if (adapter.getItemCount() > 0) {
                                    if (bottomNavigationView.getMenu().getItem(1).getTitle().equals("전체선택")) {
                                        Log.d(TAG, "전체선택을 눌렀을때 기능이 시작");
                                        bottomNavigationView.getMenu().getItem(1).setTitle("삭제"); // 하단탭 아이콘과 텍스트 바꾸기
                                        bottomNavigationView.getMenu().getItem(1).setIcon(android.R.drawable.checkbox_on_background);
                                        for (int i = 0; i < adapter.getItemCount(); i++) { // 아이템 전체 선택하기
                                            adapter.items.get(i).setCheck(true);
                                            adapter.items.get(i).setCardBackGroundColor(100);
                                        }
                                        showToast("모든 아이템을 선택하였습니다.");
                                        recyclerView.setAdapter(adapter);
                                    } else if (bottomNavigationView.getMenu().getItem(1).getTitle().equals("삭제")) {
                                        Log.d(TAG, "삭제를 눌렀을때 기능이 시작");
                                        delCheckedItem(); // 아이템 전체 삭제하기
                                        bottomNavigationView.getMenu().getItem(1).setTitle("전체선택"); // 하단탭 아이콘과 텍스트 바꾸기
                                        bottomNavigationView.getMenu().getItem(1).setIcon(android.R.drawable.checkbox_off_background);
                                        showToast("삭제가 완료되었습니다.");
                                        bottomView();
                                    }
                                } else {
                                    showToast("아이템이 없습니다.");
                                }

                                return true;
                            case R.id.tab3:
                                // 3번탭 선택시 실행될 기능
                                // 닫기
                                itemDefaultStyle();
                                recyclerView.removeAllViewsInLayout();
                                adapter.notifyDataSetChanged();
                                recyclerView.setAdapter(adapter);
                                bottomView();
                                return true;
                        }
                        return false;
                    }
                }
        );

        // 아이템 클릭시 실행할 기능을 넣자.
        adapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // 아이템을 클릭하면, 실행할 액티비티의 기능
                if (bottomNavigationView.getVisibility() == View.INVISIBLE) {
                    bottomView();
                }
            }
        });

        // 어댑터의 커스텀리스너(롱클릭을위한)를 오버라이드하여, 액티비티에서 처리할수 있게 한다.
        adapter.setOnItemLongClickListener(new ItemAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, int position) {
                // 아이템을 롱클릭하면, 하단탭이 생성되게 할것이다.
                if (bottomNavigationView.getVisibility() == View.INVISIBLE) {
                    bottomView();
                }
            }
        });

        adapter.setOnEnterClickListener(new ItemAdapter.OnEnterClickListener() {
            @Override
            public void onEnterClick(View v, int position) {
                // 엔터를 누르면
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public void callbackStringDataMethod(String string, String string2) {
//        loadData();
        recyclerView.setVisibility(View.VISIBLE); // fragment1을 불러올때 리사이클러뷰를 감추었으니, 이제 보이게 한다.
        adapter.addItem(new Item(string, string2)); // 어댑터에 프래그먼트에서 가져온 데이터를 Item 객체에 실어서 보낸다.
        recyclerView.setAdapter(adapter); // 리사이클러뷰를 세팅한다.
        recyclerView.setVisibility(View.VISIBLE); // 리사이클러뷰를 보이게 한다.
        loadData();
    }

    @Override
    public void exitFragmentMethod() {
        recyclerView.setVisibility(View.VISIBLE); // 리사이클러뷰를 보이게 한다.
    }


    // 리사이클러뷰의 체크한 아이템을 삭제하는 메서드

    private void delCheckedItem() {
        if (adapter.getItemCount() > 0) {
//             Iterator를 이용해 ArrayList<item> items의 item들을 지우기
            Iterator<Item> iter = adapter.items.iterator();
            while (iter.hasNext()) {
                if (iter.next().getCheck()) {
                    iter.remove();
                }
            }
        }
        // 리사이클러뷰를 재설정해서 아이템들 새로고침하기
        recyclerView.setAdapter(adapter);
    }

    private void itemDefaultStyle() {
        if (adapter.getItemCount() > 0) {
            // 체크와 배경색이 디폴드값으로 돌아오게 한다.
            for (int i = 0; i < adapter.getItemCount(); i++) {
                adapter.items.get(i).setCheck(false);
                adapter.items.get(i).setCardBackGroundColor(-1);
            }
        }
    }

    // 하단탭 생성, 삭제
    private void bottomView() {
        if (!bottomBool) {
            bottomNavigationView.setVisibility(View.VISIBLE);
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_bottom_menu_start);
            bottomNavigationView.startAnimation(anim);
            bottomBool = true;
        } else if (bottomBool) {
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_bottom_menu_end);
            bottomNavigationView.startAnimation(anim);
            bottomNavigationView.setVisibility(View.INVISIBLE);
            bottomBool = false;
        } else {
            Log.d(TAG, "bottomView에서 bottomBool이 이상해요");
        }
    }

    private void setFragment1() {
        // 리사이클러뷰를 가리고 프래그먼트를 띄운다.
        recyclerView.setVisibility(View.INVISIBLE);
        // 입력화면 프래그먼트 띄우기
        fragment1 = new Fragment1();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();
    }

    public void showToast(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }

    private void showNoti1() {
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); // NotificationManager 객체 참조하기

        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (manager.getNotificationChannel(CHANNEL_ID) == null) {
                manager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT));
            }
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(this);
        }
        builder.setContentTitle("타이틀");
        builder.setContentText("텍스트");
        builder.setSmallIcon(android.R.drawable.ic_menu_view);
        Notification noti = builder.build();

        manager.notify(1, noti);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        diaryNotification((Calendar) data.getExtras().get("calendar"));
    }

    void diaryNotification(Calendar calendar) {
        Log.d("알람액티비티", "다이어리노티피케이션");
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public void showLog(String string) {
        Log.d("메인액티비티", string);
    }

    public void saveData(String tableName, int position, String memo, String memo_edit, int checkbox, int back, String time) {
        db.execSQL("insert into " + tableName
                + "(position, text_main, text_edit, checkbox, cardBackGroundColor, time) "
                + " values "
                + "( '"+position+"', '"+memo+"', '"+memo_edit+"', '"+checkbox+"','"+back+"' , '"+time+"' )");
    }

    public void loadData() {
        db = openOrCreateDatabase("ItemData", MODE_PRIVATE, null);
        String tableName = "ItemTable";
        Cursor cursor = db.rawQuery("select  text_main, text_edit, checkbox, cardBackGroundColor, time from " + tableName + "", null);
        int recordCount = cursor.getCount();
        for (int i = 0; i < recordCount; i++) {
            cursor.moveToNext();
//                    int id = cursor.getInt(0);
            String text_main = cursor.getString(0);
            String text_edit = cursor.getString(1);
            int checkbox = cursor.getInt(2);
            int cardBackGroundColor = cursor.getInt(3);
            String time = cursor.getString(4);

            showLog("레코드 : " + text_main + " , " + text_edit + " , " + checkbox + " , " + cardBackGroundColor + " , " + time);
        }
        cursor.close();
    }


    @Override
    protected void onPostResume() {
        // 타임액티비티 종료하면 이 메서드가 실행된다.
        super.onPostResume();
        showLog("재시작");
    }

    @Override
    protected void onStart() {
        super.onStart();
        db = openOrCreateDatabase("ItemData", MODE_PRIVATE, null);
        db.execSQL("create table if not exists ItemTable ("
                + " _id integer NOT NULL PRIMARY KEY autoincrement, "
                + " text_main text, "
                + " text_edit text, "
                + " checkbox int, "
                + " cardBackGroundColor int, "
                + " time text)");
        loadData();
    }
}