package com.wuyin.supermarket.holder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wuyin.supermarket.R;
import com.wuyin.supermarket.holder.base.BaseHolder;
import com.wuyin.supermarket.model.AppInfo;
import com.wuyin.supermarket.uri.Constants;
import com.wuyin.supermarket.utils.UIUtils;
import android.view.ViewGroup.LayoutParams;

import java.util.List;

/**
 * Created by yinlong on 2016/5/11.
 */
public class DetailSafeHolder  extends BaseHolder<AppInfo> implements View.OnClickListener {

    private RelativeLayout safe_layout;
    private LinearLayout safe_content;
    private ImageView safe_arrow;
    ImageView[] ivs;
    ImageView[] iv_des;
    TextView[] tv_des;
    LinearLayout[] des_layout;
    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.detail_safe);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        ivs = new ImageView[4]; // 初始化标题栏的图片
        ivs[0] = (ImageView) view.findViewById(R.id.iv_1);
        ivs[1] = (ImageView) view.findViewById(R.id.iv_2);
        ivs[2] = (ImageView) view.findViewById(R.id.iv_3);
        ivs[3] = (ImageView) view.findViewById(R.id.iv_4);
        iv_des = new ImageView[4]; // 初始化每个条目描述的图片
        iv_des[0] = (ImageView) view.findViewById(R.id.des_iv_1);
        iv_des[1] = (ImageView) view.findViewById(R.id.des_iv_2);
        iv_des[2] = (ImageView) view.findViewById(R.id.des_iv_3);
        iv_des[3] = (ImageView) view.findViewById(R.id.des_iv_4);
        tv_des = new TextView[4]; // 初始化每个条目描述的文本
        tv_des[0] = (TextView) view.findViewById(R.id.des_tv_1);
        tv_des[1] = (TextView) view.findViewById(R.id.des_tv_2);
        tv_des[2] = (TextView) view.findViewById(R.id.des_tv_3);
        tv_des[3] = (TextView) view.findViewById(R.id.des_tv_4);

        des_layout = new LinearLayout[4]; // 初始化条目线性布局
        des_layout[0] = (LinearLayout) view.findViewById(R.id.des_layout_1);
        des_layout[1] = (LinearLayout) view.findViewById(R.id.des_layout_2);
        des_layout[2] = (LinearLayout) view.findViewById(R.id.des_layout_3);
        des_layout[3] = (LinearLayout) view.findViewById(R.id.des_layout_4);

        safe_content = (LinearLayout) view.findViewById(R.id.safe_content);
        safe_arrow = (ImageView) view.findViewById(R.id.safe_arrow);
        safe_layout = (RelativeLayout) view.findViewById(R.id.safe_layout);

        LayoutParams layoutParams = safe_content.getLayoutParams();
        layoutParams.height=0;
        safe_content.setLayoutParams(layoutParams);
        safe_arrow.setImageResource(R.mipmap.arrow_down);
    }

    @Override
    public void refreshData(AppInfo data) {
        safe_layout.setOnClickListener(this);

        List<String> safeUrl = data.getSafeUrl();
        List<String> safeDesUrl = data.getSafeDesUrl();
        List<String> safeDes = data.getSafeDes();
        List<Integer> safeDesColor = data.getSafeDesColor(); // 0 1 2 3
        for (int i = 0; i < 4; i++) {
            if (i < safeUrl.size() && i < safeDesUrl.size()
                    && i < safeDes.size() && i < safeDesColor.size()) {
                ivs[i].setVisibility(View.VISIBLE);
                des_layout[i].setVisibility(View.VISIBLE);
                Glide.with(UIUtils.getContext()).load(Constants.IMAGE_URL+safeUrl.get(i)).into(ivs[i]);
                Glide.with(UIUtils.getContext()).load(Constants.IMAGE_URL+safeDesUrl.get(i)).into(iv_des[i]);

                tv_des[i].setText(safeDes.get(i));
                // 根据服务器数据显示不同的颜色
                int color;
                int colorType = safeDesColor.get(i);
                if (colorType >= 1 && colorType <= 3) {
                    color = Color.rgb(255, 153, 0); // 00 00 00
                } else if (colorType == 4) {
                    color = Color.rgb(0, 177, 62);
                } else {
                    color = Color.rgb(122, 122, 122);
                }
                tv_des[i].setTextColor(color);

            } else {
                ivs[i].setVisibility(View.GONE);
                des_layout[i].setVisibility(View.GONE);
            }

        }

    }

    boolean flag=false;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.safe_layout) {
            int  startHeight;
            int targetHeight;
            if (!flag) {    //  展开的动画
                startHeight=0;
                targetHeight=getMeasureHeight();

                flag = true;
                //safe_content.setVisibility(View.VISIBLE);
                safe_content.getMeasuredHeight();  //  0
            } else {
                flag=false;
                //safe_content.setVisibility(View.GONE);
                startHeight=getMeasureHeight();
                targetHeight=0;
            }
            // 值动画
            ValueAnimator animator=ValueAnimator.ofInt(startHeight,targetHeight);
            final RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) safe_content.getLayoutParams();
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {  // 监听值的变化

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    int value=(Integer) animator.getAnimatedValue();// 运行当前时间点的一个值
                    layoutParams.height=value;
                    safe_content.setLayoutParams(layoutParams);// 刷新界面
                }
            });

            animator.addListener(new Animator.AnimatorListener() {  // 监听动画执行
                //当动画开始执行的时候调用
                @Override
                public void onAnimationStart(Animator arg0) {
                    // TODO Auto-generated method stub

                }
                @Override
                public void onAnimationRepeat(Animator arg0) {

                }
                @Override
                public void onAnimationEnd(Animator arg0) {
                    if(flag){
                        safe_arrow.setImageResource(R.mipmap.arrow_up);
                    }else{
                        safe_arrow.setImageResource(R.mipmap.arrow_down);
                    }
                }
                @Override
                public void onAnimationCancel(Animator arg0) {

                }
            });

            animator.setDuration(500);
            animator.start();


        }
    }

    //onMeasure()  制定测量的规则
    // measure() 实际测量
    /**
     * 获取控件实际的高度
     */
    public int getMeasureHeight(){
        int width = safe_content.getMeasuredWidth();  //  由于宽度不会发生变化  宽度的值取出来
        safe_content.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;//  让高度包裹内容

        int widthMeasureSpec= View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.EXACTLY, width);  //  mode+size
        //    参数1  测量控件mode    参数2  大小
        int heightMeasureSpec= View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.AT_MOST, 1000);// 我的高度 最大是1000
        // 测量规则 宽度是一个精确的值width, 高度最大是1000,以实际为准
        safe_content.measure(widthMeasureSpec, heightMeasureSpec); // 通过该方法重新测量控件

        return safe_content.getMeasuredHeight();



    }
}
