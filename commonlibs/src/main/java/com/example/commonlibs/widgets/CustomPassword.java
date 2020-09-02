package com.example.commonlibs.widgets;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.commonlibs.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义身份证密码键盘
 *
 * @author ZJ
 * created at 2019/9/19 17:33
 */
public class CustomPassword extends RelativeLayout implements View.OnClickListener {
    Context context;
    private String strPassword;     //输入的密码
    private TextView[] tvList;      //用数组保存6个TextView，为什么用数组？
    //因为就6个输入框不会变了，用数组内存申请固定空间，比List省空间（自己认为）
    private GridView gridView;    //用GrideView布局键盘，其实并不是真正的键盘，只是模拟键盘的功能
    private ArrayList<Map<String, String>> valueList;    //有人可能有疑问，为何这里不用数组了？
    //因为要用Adapter中适配，用数组不能往adapter中填充
    private ImageView imgCancel;
    private int currentIndex = -1;    //用于记录当前输入密码格位置

    public CustomPassword(Context context) {
        this(context, null);
    }

    public CustomPassword(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = View.inflate(context, R.layout.layout_password, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        valueList = new ArrayList<Map<String, String>>();
        tvList = new TextView[4];
        imgCancel = (ImageView) view.findViewById(R.id.img_cancel);
        imgCancel.setOnClickListener(this);
        tvList[0] = (TextView) view.findViewById(R.id.tv_pass1);
        tvList[1] = (TextView) view.findViewById(R.id.tv_pass2);
        tvList[2] = (TextView) view.findViewById(R.id.tv_pass3);
        tvList[3] = (TextView) view.findViewById(R.id.tv_pass4);
        gridView = (GridView) view.findViewById(R.id.gv_keybord);
        startAnimator(tvList[0]);
        setView();
        addView(view);      //必须要，不然不显示控件
    }

    ObjectAnimator animator;

    private void startAnimator(TextView textView) {
        animator = ObjectAnimator.ofFloat(textView, "alpha", 1f, 0f, 1f);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.img_cancel) {
            Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
        }
    }


    private void setView() {
        /* 初始化按钮上应该显示的数字 */
        for (int i = 1; i < 13; i++) {
            Map<String, String> map = new HashMap<String, String>();
            if (i < 10) {
                switch (i) {
                    case 1:
                        map.put("name", String.valueOf(1));
                        break;
                    case 2:
                        map.put("name", String.valueOf(2));
                        break;
                    case 3:
                        map.put("name", String.valueOf(3));
                        break;
                    case 4:
                        map.put("name", String.valueOf(4));
                        break;
                    case 5:
                        map.put("name", String.valueOf(5));
                        break;
                    case 6:
                        map.put("name", String.valueOf(6));
                        break;
                    case 7:
                        map.put("name", String.valueOf(7));
                        break;
                    case 8:
                        map.put("name", String.valueOf(8));
                        break;
                    case 9:
                        map.put("name", String.valueOf(9));
                        break;
                }
            } else if (i == 10) {
                map.put("name", "");
            } else if (i == 12) {
                map.put("name", "--");
            } else if (i == 11) {
                map.put("name", String.valueOf(0));
            }
            valueList.add(map);
        }

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 9) {
                return;
            }
            if (position < 11) {    //点击0~9按钮
                if (currentIndex >= -1 && currentIndex < 3) {//判断输入位置————要小心数组越界
                    int dex = ++currentIndex;
                    animator.cancel();
                    tvList[currentIndex].setAlpha(1.0f);
                    if (dex < 3) {
                        startAnimator(tvList[dex + 1]);
                    }
                    tvList[dex].setText(valueList.get(position).get("name"));

                    tvList[dex].setTextColor(Color.TRANSPARENT);
                    Drawable drawable = getResources().getDrawable(R.drawable.pwd_oval_show);

                    //一定要加这行！！！！！！！！！！！
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tvList[dex].setCompoundDrawablePadding(-10);
                    tvList[dex].setCompoundDrawables(null, null, drawable, null);
                    if (currentIndex == 3) {
                        onPasswordInputFinish.inputFinish(tvList[0].getText().toString() + tvList[1].getText().toString()
                                + tvList[2].getText().toString() + tvList[3].getText().toString());
                    }
                }
            } else {
                if (position == 11) {      //退格

                    if (currentIndex - 1 >= -1) {      //判断是否删除完毕————要小心数组越界
                        animator.cancel();
                        int add = currentIndex + 1;
                        if (add < 4) {
                            tvList[add].setAlpha(1.0f);
                        }
                        int del = currentIndex--;
                        if (del > 0 || del == 0) {
                            startAnimator(tvList[del]);
                        }
//                            tvList[del].setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        Drawable drawable = getResources().getDrawable(R.drawable.pwd_oval_show);

                        //一定要加这行！！！！！！！！！！！
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

                        tvList[del].setCompoundDrawables(null, null, null, null);
                        tvList[del].setTextColor(getResources().getColor(R.color.colorAccent));
                        tvList[del].setText("一");
                    }

                }
            }
        });
    }

    public void clearAll() {
        for (TextView textView : tvList) {
            currentIndex--;
            textView.setText("一");
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
            textView.setTextColor(getResources().getColor(R.color.colorAccent));
            drawable.setBounds(0, 0, 0, drawable.getMinimumHeight());
            textView.setCompoundDrawables(null, null, null, null);
        }
        startAnimator(tvList[0]);
    }

    public OnPasswordCancelInput onPasswordCancelInput;

    public void setOnPasswordCancelInput(OnPasswordCancelInput onPasswordCancelInput) {
        this.onPasswordCancelInput = onPasswordCancelInput;
    }

    public OnPasswordInputFinish onPasswordInputFinish;

    public void setOnPasswordInputFinish(OnPasswordInputFinish inputFinish) {
        this.onPasswordInputFinish = inputFinish;

    }


    /* 获取输入的密码 */
    public String getStrPassword() {
        return strPassword;
    }

    /* 暴露取消支付的按钮，可以灵活改变响应 */
    public ImageView getCancelImageView() {
        return imgCancel;
    }


    //GrideView的适配器
    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return valueList.size();
        }

        @Override
        public Object getItem(int position) {
            return valueList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.layout_item_gride_key, null);
                viewHolder = new ViewHolder();
                viewHolder.btnKey = (TextView) convertView.findViewById(R.id.btn_keys);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.btnKey.setText(valueList.get(position).get("name"));
            if (position == 11) {
                viewHolder.btnKey.setBackground(null);
                viewHolder.btnKey.setTextColor(getResources().getColor(R.color.colorAccent));
                Drawable nav_up = getResources().getDrawable(R.drawable.icon_h_de);
                nav_up.setBounds(0, 20, nav_up.getMinimumWidth(), nav_up.getMinimumHeight() + 10);
                viewHolder.btnKey.setCompoundDrawables(null, nav_up, null, null);
            }
            if (position == 9) {
                viewHolder.btnKey.setBackground(null);
            }

            return convertView;
        }
    };


    /**
     * 存放控件
     */
    public final class ViewHolder {
        public TextView btnKey;
    }
}
