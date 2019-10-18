package com.qegame.dialogcontextmenu;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.shape.EdgeTreatment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.RoundedCornerTreatment;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.ShapePath;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class DialogContextMenu extends DialogFragment {

    /** Left of Context */
    private final int TYPE_LEFT = 0;
    /** Top of Context */
    private final int TYPE_TOP = 1;
    /** Right of Context */
    private final int TYPE_RIGHT = 2;
    /** Bottom of Context */
    private final int TYPE_BOTTOM = 3;

    /** Listener */
    private OnSelectItemListener onSelectItemListener;
    /** Position dialog */
    private Point point;
    /** Round */
    private Integer cornerRadius;
    /** Color background */
    private int colorBackground;
    /** Color Ripple */
    private int colorRipple;
    /** Text Size */
    private int textSize;
    /** Text Color */
    private int textColor;
    /** Text Padding */
    private Integer padding;
    /** Text Padding Left */
    private int paddingLeft;
    /** Text Padding Top */
    private int paddingTop;
    /** Text Padding Right */
    private int paddingRight;
    /** Text Padding Bottom */
    private int paddingBottom;

    private int widthDisplay;
    private int heightDisplay;

    private int type;

    private Collection<DialogContextItem> items;

    public static DialogContextMenu instance(@Size(min = 1) @NonNull DialogContextItem...items) {
        if (items.length == 0) {
            throw new RuntimeException("The number of elements must not be equal to zero!");
        }
        DialogContextMenu dialog = new DialogContextMenu();
        dialog.items.addAll(Arrays.asList(items));
        return dialog;
    }

    public DialogContextMenu() {
        this.point = new Point();
        this.onSelectItemListener = new OnSelectItemListener() {
            @Override
            public void onSelectItem(int position) {

            }
        };

        this.padding = null;
        this.paddingLeft = 50;
        this.paddingTop = 20;
        this.paddingRight = 50;
        this.paddingBottom = 20;

        this.items = new ArrayList<>();

        textSize = 14;
        textColor = Color.BLACK;

        colorRipple = Color.GRAY;
        colorBackground = Color.WHITE;
    }

    //region Getters/Setters

    public Collection<DialogContextItem> getItems() {
        return items;
    }

    public OnSelectItemListener getOnSelectItemListener() {
        return onSelectItemListener;
    }
    public void setOnSelectItemListener(OnSelectItemListener onSelectItemListener) {
        this.onSelectItemListener = onSelectItemListener;
    }

    public Integer getCornerRadius() {
        return cornerRadius;
    }
    public void setCornerRadius(Integer cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public int getColorBackground() {
        return colorBackground;
    }
    public void setColorBackground(int colorBackground) {
        this.colorBackground = colorBackground;
    }

    public int getColorRipple() {
        return colorRipple;
    }
    public void setColorRipple(int colorRipple) {
        this.colorRipple = colorRipple;
    }

    public int getTextSize() {
        return textSize;
    }
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public Integer getPadding() {
        return padding;
    }
    public void setPadding(Integer padding) {
        this.padding = padding;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }
    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingTop() {
        return paddingTop;
    }
    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingRight() {
        return paddingRight;
    }
    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }
    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    //endregion

    public void addItem(@NonNull DialogContextItem item) {
        this.items.add(item);
    }

    public int show(@NonNull FragmentTransaction transaction, @Nullable String tag, int x, int y) {
        if (items.size() == 0) {
            throw new RuntimeException("The number of elements must not be equal to zero!");
        }
        setPosition(x, y);
        return super.show(transaction, tag);
    }

    public void show(@NonNull FragmentManager manager, @Nullable String tag, int x, int y) {
        if (items.size() == 0) {
            throw new RuntimeException("The number of elements must not be equal to zero!");
        }
        setPosition(x, y);
        super.show(manager, tag);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        this.widthDisplay = size.x;
        this.heightDisplay = size.y;
    }
    /** gre */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final LinearLayout linear = new LinearLayout(getContext());
        linear.setOrientation(LinearLayout.VERTICAL);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        for (final DialogContextItem item : items) {
            TextView textView = getTextItem(item.getText());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.onSelect();
                    DialogContextMenu.this.onSelectItemListener.onSelectItem(linear.indexOfChild(v));
                    dismiss();
                }
            });
            linear.addView(textView);
        }

        setupType();

        switch (this.type) {
            case TYPE_LEFT: getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_x_end; break;
            case TYPE_TOP: getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_y_end; break;
            case TYPE_RIGHT: getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_x_start; break;
            case TYPE_BOTTOM: getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_y_start; break;
        }

        onMeasureLinear(linear);

        return linear;
    }

    private void setupType() {
        final double MAX_CENTER_RANGE = 0.2;

        int centerRange = (int) (this.widthDisplay * MAX_CENTER_RANGE);
        //Center
        if (Math.abs(x()) < centerRange) {
            if (y() > 0) {
                //Top
                this.type = TYPE_TOP;
            } else {
                //Bottom
                this.type = TYPE_BOTTOM;
            }
        } else {
            if (x() > 0) {
                //Left
                this.type = TYPE_LEFT;
            } else {
                //Right
                this.type = TYPE_RIGHT;
            }
        }
    }

    /** To specify coordinates of an index corner.
     * Must be relative to the entire screen. */
    public final void setPosition(int x, int y) {
        this.point = new Point(x, y);
    }
    /** To specify coordinates of an index corner.
     * Must be relative to the entire screen. */
    public final void setPosition(View view) {
        int[] xy = new int[] {0, 0};
        view.getLocationOnScreen(xy);
        ViewGroup.MarginLayoutParams lp =  (ViewGroup.MarginLayoutParams) view.getLayoutParams();

        int width = view.getWidth() + lp.leftMargin + lp.rightMargin;
        int height = view.getHeight() + lp.topMargin + lp.bottomMargin;

        this.point = new Point(xy[0] + width / 2, xy[1] + height / 2);
    }

    /** Setup Position  */
    private void setupPosition(ViewGroup container) {

        Window dialogWindow = getDialog().getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        int[] xy = buildPosition(container);

        lp.x = xy[0];
        lp.y = xy[1];

        dialogWindow.setAttributes(lp);
    }
    private void onMeasureLinear(final ViewGroup v) {
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setupPosition(v);
            }
        });
    }
    private int[] buildPosition(ViewGroup container) {

        float WIDTH_ARROW = container.getWidth() * 0.3f;
        float HEIGHT_ARROW = container.getWidth() * 0.15f;

        final float ROUND = (container.getWidth() * 0.2f);

        if (WIDTH_ARROW > container.getChildAt(0).getHeight() * 1f) {
            WIDTH_ARROW = container.getChildAt(0).getHeight() * 1f;
        }
        if (HEIGHT_ARROW > container.getChildAt(0).getHeight() * 0.5f) {
            HEIGHT_ARROW = container.getChildAt(0).getHeight() * 0.5f;
        }
        float round = this.cornerRadius == null ? ROUND : this.cornerRadius;
        if (round > container.getChildAt(0).getHeight() / 2) {
            round = container.getChildAt(0).getHeight() / 2;
        }
        if (round > container.getChildAt(0).getWidth() / 2) {
            round = container.getChildAt(0).getWidth() / 2;
        }

        switch (this.type) {
            case TYPE_LEFT:
                container.setBackground(getRightDrawable(round));
                setBackgroundItems(container, new boolean[] {true, false, false, false}, new boolean[] {false, false, true, true}, round);
                setGravity(container, Gravity.END);
                return new int[] {x() - half_w(container), y() + half_h(container)};
            case TYPE_TOP:
                container.setBackground(getTopDrawable(WIDTH_ARROW, HEIGHT_ARROW, round));
                container.setPadding(0, 0, 0, (int) HEIGHT_ARROW);
                setBackgroundItems(container, new boolean[] {true, true, false, false}, new boolean[] {false, false, true, true}, round);
                setGravity(container, Gravity.CENTER);
                return new int[] {x(), y() - half_h(container) - (int) HEIGHT_ARROW / 2};
            case TYPE_RIGHT:
                container.setBackground(getLeftDrawable(round));
                setBackgroundItems(container, new boolean[] {false, true, false, false}, new boolean[] {false, false, true, true}, round);
                setGravity(container, Gravity.START);
                return new int[] {x() + half_w(container), y() + half_h(container)};
            case TYPE_BOTTOM:
                container.setBackground(getBottomDrawable(WIDTH_ARROW, HEIGHT_ARROW, round));
                container.setPadding(0, (int) HEIGHT_ARROW, 0, 0);
                setBackgroundItems(container, new boolean[] {true, true, false, false}, new boolean[] {false, false, true, true}, round);
                setGravity(container, Gravity.CENTER);
                return new int[] {x(), y() + half_h(container) + (int) HEIGHT_ARROW / 2};

                default: return new int[] {0, 0};
        }

    }

    private void setGravity(ViewGroup container, int gravity) {
        for (int i = 0; i < container.getChildCount(); i++) {
            ((TextView) container.getChildAt(i)).setGravity(gravity);
        }
    }

    /** Build TextView */
    private TextView getTextItem(String text) {
        TextView textView = new TextView(getContext());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lp.weight = 1;
        textView.setLayoutParams(lp);

        if (this.padding != null) {
            textView.setPadding(this.padding, this.padding, this.padding, this.padding);
        } else {
            textView.setPadding(this.paddingLeft, this.paddingTop, this.paddingRight, this.paddingBottom);
        }
        textView.setText(text);
        textView.setSingleLine(true);
        textView.setTextSize(this.textSize);
        textView.setTextColor(this.textColor);

        return textView;
    }
    private void setBackgroundItems(ViewGroup container, boolean[] first, boolean[] last, float round) {
        if (container.getChildCount() == 1) {
            first[2] = true;
            first[3] = true;
            container.getChildAt(0).setBackground(getItemBackground(first, round));
            return;
        }
        for (int i = 0; i < container.getChildCount(); i++) {
            if (i == 0) {
                container.getChildAt(i).setBackground(getItemBackground(first, round));
            } else {
                if (i == container.getChildCount() - 1) {
                    container.getChildAt(i).setBackground(getItemBackground(last, round));
                } else {
                    container.getChildAt(i).setBackground(getItemBackground(new boolean[] {false, false, false, false}, round));
                }
            }
        }
    }
    /** Build Background TextView */
    private Drawable getItemBackground(boolean[] corners, float round) {
        ShapeAppearanceModel model = new ShapeAppearanceModel();
        if (corners[0]) model.setTopLeftCorner(new RoundedCornerTreatment(round));
        if (corners[1]) model.setTopRightCorner(new RoundedCornerTreatment(round));
        if (corners[2]) model.setBottomRightCorner(new RoundedCornerTreatment(round));
        if (corners[3]) model.setBottomLeftCorner(new RoundedCornerTreatment(round));
        MaterialShapeDrawable drawable = new MaterialShapeDrawable(model);
        drawable.setTint(this.colorBackground);

        return setupRipple(drawable);
    }
    /** Dot in left */
    private Drawable getLeftDrawable(float round) {
        return getItemBackground(new boolean[] {false, true, true, true}, round);
    }
    private Drawable getTopDrawable(float widthArrow, float heightArrow, float round) {
        ShapeAppearanceModel shape = new ShapeAppearanceModel();
        shape.setBottomEdge(new TopIndex(widthArrow, heightArrow, round));
        shape.setTopLeftCorner(new RoundedCornerTreatment(round));
        shape.setTopRightCorner(new RoundedCornerTreatment(round));
        MaterialShapeDrawable drawable = new MaterialShapeDrawable(shape);
        drawable.setTint(colorBackground);
        return drawable;
    }
    /** Dot in Right */
    private Drawable getRightDrawable(float round) {
        return getItemBackground(new boolean[] {true, false, true, true}, round);
    }
    private Drawable getBottomDrawable(float widthArrow, float heightArrow, float round) {
        ShapeAppearanceModel shape = new ShapeAppearanceModel();
        shape.setTopEdge(new TopIndex(widthArrow, heightArrow, round));
        shape.setBottomLeftCorner(new RoundedCornerTreatment(round));
        shape.setBottomRightCorner(new RoundedCornerTreatment(round));
        MaterialShapeDrawable drawable = new MaterialShapeDrawable(shape);
        drawable.setTint(colorBackground);
        return drawable;
    }
    private Drawable setupRipple(Drawable drawable) {
        ColorStateList colorStateList = new ColorStateList(new int[][]{ new int[]{}},new int[]{this.colorRipple});
        return new RippleDrawable(colorStateList, drawable, null);
    }

    /** @return px */
    private float dp(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
    private int x() {
        return point.x - this.widthDisplay / 2;
    }
    private int y() {
        return point.y - this.heightDisplay / 2;
    }
    private int half_w(View v) {
        return v.getWidth() / 2;
    }
    private int half_h(View v) {
        return v.getHeight() / 2;
    }

    private static class TopIndex extends EdgeTreatment {

        private float radius, height, widthArrow;

        public TopIndex(float widthArrow, float heightArrow, float radius) {
            this.height = heightArrow;
            this.widthArrow = widthArrow;
            this.radius = radius;
        }

        @Override
        public void getEdgePath(float length, float center, float interpolation, ShapePath shapePath) {
            shapePath.reset(0, 0);
            shapePath.lineTo(0, height + radius);
            shapePath.addArc(0, height, radius * 2, height + radius * 2, 90, 180);
            shapePath.lineTo(center - (widthArrow / 2), shapePath.endY);
            shapePath.quadToPoint((int) (shapePath.endX + (widthArrow / 2) * 0.7), (int) (height * 1), center, 0);
            shapePath.quadToPoint((int) (center + (widthArrow / 2) * 0.3), (int) (height * 1), center + widthArrow / 2, height);
            shapePath.lineTo(length, shapePath.endY);
            shapePath.addArc(length - radius * 2, height, length, height + radius * 2, 270, 90);
        }
    }
}
