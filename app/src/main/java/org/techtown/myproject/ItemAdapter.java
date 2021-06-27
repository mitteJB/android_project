package org.techtown.myproject;

import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.security.Key;
import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    static ArrayList<Item> items = new ArrayList<Item>();
    final static String TAG = "ItemAdapter";


    // 엔터 입력시 액티비티에서 처리하기 위한 커스텀 인터페이스!
    public interface OnEnterClickListener {
        void onEnterClick(View v, int position);

    }
    private OnEnterClickListener enterClickListener = null;


    public void setOnEnterClickListener(OnEnterClickListener enterClickListener) {
        this.enterClickListener = enterClickListener;
    }
    // 일반 클릭시 액티비티에서 처리하기 위한 커스텀 인터페이스!
    // 메인에서 오버라이드해서 구현한다.
    public interface OnItemClickListener {
        void onItemClick(View v, int position);

    }
    private OnItemClickListener clickListener = null;

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }
    //

    // 롱클릭시 액티비티에서 처리하기 위한 커스텀 인터페이스!
    // 메인에서 오버라이드해서 구현할거야.
    public interface OnItemLongClickListener {
        void onItemLongClick(View v, int position);

    }
    private OnItemLongClickListener longClickListener = null;

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }
    //



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        CardView cardView;
        CheckBox checkBox;
        TextView textView, textView3;
        EditText editText;

        int mPosition;


        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnCreateContextMenuListener(this);

            checkBox = itemView.findViewById(R.id.checkBox);
            textView = itemView.findViewById(R.id.textView);
            textView3 = itemView.findViewById(R.id.textView3);
            cardView = itemView.findViewById(R.id.cardView);
            editText = itemView.findViewById(R.id.editTextTextPersonName3);

            textView.setVisibility(View.VISIBLE);
            editText.setVisibility(View.INVISIBLE);


            // 아이템 클릭시 실행할 기능을 넣자.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "눌렸다.");
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) { // 아이템 유무 검사
                        // 포지션을 얻었으니 데이터 접근 가능
//                        Item item = items.get(position);

                        if (clickListener != null) {
                            clickListener.onItemClick(v, position);
                            checkBoxHighlight(ViewHolder.this, position);
                            Log.d(TAG, textView.getText().toString());
                            Log.d(TAG, editText.getText().toString());

                        }
                    }
                }
            });

            editText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (keyCode == KeyEvent.KEYCODE_ENTER) {
                            String str = String.valueOf(keyCode);
                            Log.d(TAG, str);
                            editItem(items.get(position));
                            chechBoxDefault(ViewHolder.this, position);
                            textView.setVisibility(View.VISIBLE);
                            editText.setVisibility(View.INVISIBLE);

                        }
                    }
//                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
//                        Log.d(TAG, "실행");
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            editItem(items.get(position));
//                        }
//                    }
                    return false;
                }
            });

            // 체크박스 클릭 리스너
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        checkBoxHighlight(ViewHolder.this, getAdapterPosition());

                    } else if (!checkBox.isChecked()) {
                        checkBoxHighlight(ViewHolder.this, getAdapterPosition());
                    }
                }
            });
        }

        public void setItem(Item item) {
            textView.setText(item.getText_main());
            textView3.setText(item.getTime());
            editText.setText(item.getText_edit());
            cardView.setCardBackgroundColor(item.getCardBackGroundColor());
            checkBox.setChecked(item.getCheck());
        }


        public void editItem(Item item) {
            String str = editText.getText().toString();
            if (!str.equals("")) {
                textView.setText(str);
                item.setText_main(str);
            }
        }

        // 컨텍스트 메뉴
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Modify = menu.add(Menu.NONE, R.id.modify_menu, 1, "변경");
            MenuItem Delete = menu.add(Menu.NONE, R.id.delete_menu, 2, "삭제");
            Modify.setOnMenuItemClickListener(onMenuItemClickListener);
            Delete.setOnMenuItemClickListener(onMenuItemClickListener);

        }

        // 컨텍스트 메뉴별 기능 추가하기
        private final MenuItem.OnMenuItemClickListener onMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.modify_menu:
                        // 변경 눌렀을때 데이터를 바꾸어 준다.
                        int position = getAdapterPosition();
                        checkBoxHighlight(ViewHolder.this, position);
                        textView.setVisibility(View.INVISIBLE);
                        editText.setVisibility(View.VISIBLE);
                        editText.requestFocus();
                        return true;

                    case R.id.delete_menu:
                        // 삭제 눌렀을때 아이템 삭제.
                        items.remove(getAdapterPosition());
                        notifyDataSetChanged();
                        return true;
                }
                return false;
            }
        };
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {
        Item item = new Item(items.get(position).text_main, items.get(position).text_edit, items.get(position).getCheck(), items.get(position).getCardBackGroundColor(), items.get(position).getTime());
        Log.d(TAG, "onBindViewHolder 호출 : " + item.getInfo());
        holder.setItem(item);



    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    // 뷰홀더 객체ㅔㅔㅔㅔㅔㅔㅔㅔㅔㅔㅔㅔㅔㅔㅔㅔㅔ


    public void addItem(Item item) {
        items.add(item);
    }

    // 체크박스를 변경하고, 카드뷰의 배경을 클릭마다 바꾸어주는 메서드
    private void checkBoxHighlight(ViewHolder v, int position) {
        if (items.get(position).getCheck()) {
            v.checkBox.setChecked(false);
            items.get(position).setCheck(false);
            v.cardView.setCardBackgroundColor(-1);
            items.get(position).setCardBackGroundColor(-1);


        } else if (!items.get(position).getCheck()) {
            v.checkBox.setChecked(true);
            items.get(position).setCheck(true);
            v.cardView.setCardBackgroundColor(100);
            items.get(position).setCardBackGroundColor(100);
        }
//        this.notifyDataSetChanged();
        String str1 = String.valueOf(items.get(position).getCheck());
        Log.d(TAG, str1);
    }

    // 체크박스 상태 기본으로 돌리기
    private void chechBoxDefault(ViewHolder v, int position) {
        v.checkBox.setChecked(false);
        items.get(position).setCheck(false);
        v.cardView.setCardBackgroundColor(-1);
        items.get(position).setCardBackGroundColor(-1);
    }

    private void checkValue(ViewHolder v, int position) {
        String str1 = String.valueOf(items.get(position).getCheck());
        Log.d(TAG, str1);
    }


}
