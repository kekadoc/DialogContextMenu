package com.kekadoc.tools.android.view.dialog.expample;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.material.snackbar.Snackbar;
import com.kekadoc.tools.android.view.dialog.DialogPopupList;
import com.kekadoc.tools.android.view.dialog.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private String[] text_0 = new String[] {"0"};
    private String[] text_1 = new String[] {"item_0", "item_0_item_1", "item_0_item_1_item_2"};
    private String[] text_2 = new String[] {"item_0_item_1_item_2_item_3"};

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        FrameLayout coordinatorLayout = findViewById(R.id.container);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogPopupList dialog = DialogPopupList.instance(new DialogPopupList.Item() {
                    @Override
                    public CharSequence getText() {
                        return "Item";
                    }

                    @Override
                    public void onSelect() {

                    }
                });
                dialog.setPosition(v);
                dialog.show(getSupportFragmentManager(), "tag");
            }
        });

        DialogPopupList dialog = DialogPopupList.instance(new DialogPopupList.Item() {
            @Override
            public CharSequence getText() {
                return "Item";
            }

            @Override
            public void onSelect() {

            }
        });
        dialog.setPosition(540,888);

        dialog.show(getSupportFragmentManager(), "");

        coordinatorLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                DialogPopupList.Item item_0 = new DialogPopupList.Item() {
                    @Override
                    public CharSequence getText() {
                        return "Item_0";
                    }

                    @Override
                    public void onSelect() {

                    }
                };
                DialogPopupList.Item item_1 = new DialogPopupList.Item() {
                    @Override
                    public CharSequence getText() {
                        return "Item_1";
                    }

                    @Override
                    public void onSelect() {

                    }
                };
                DialogPopupList.Item item_2 = new DialogPopupList.Item() {
                    @Override
                    public CharSequence getText() {
                        return "Item_2";
                    }

                    @Override
                    public void onSelect() {

                    }
                };
                DialogPopupList dialog = DialogPopupList.instance(item_0, item_1, item_2);
                dialog.setPosition((int) event.getX(), (int) event.getY());
                dialog.setColorBackground(getResources().getColor(R.color.colorPrimary));
                dialog.setTextColor(Color.BLACK);
                dialog.setColorRipple(getResources().getColor(R.color.colorAccent));
                dialog.setPadding(20);
                dialog.setCornerRadius(80);
                dialog.setOnSelectItemListener(new DialogPopupList.OnSelectItemListener() {
                    @Override
                    public void onSelectItem(int position) {
                        Snackbar.make(v, "select " + position, Snackbar.LENGTH_SHORT).show();
                    }
                });
                dialog.show(getSupportFragmentManager(), "tag");
                return false;
            }
        });

    }

}
