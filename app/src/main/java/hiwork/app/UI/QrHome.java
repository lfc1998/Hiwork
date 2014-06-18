package hiwork.app.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import hiwork.app.R;
import hiwork.app.com.SampleListFragment;

/**
 * Created by Kingsun on 14-5-10.
 */
public class QrHome extends FragmentActivity {
    private Fragment mContent;
    private SlidingMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置标题
        setTitle("QrBike");

        //初始化滑动菜单
        initSlidingMenu();
    }

    /**
     * 初始化滑动菜单
     */
    private void initSlidingMenu() {
        // 设置主界面视图
        setContentView(R.layout.content_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new SampleListFragment()).commit();

        // 设置滑动菜单的属性值
        menu = new SlidingMenu(this);
        // 设置滑动菜单的视图界面
        menu.setMenu(R.layout.menu_frame);
        
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.layout.shadow);
        menu.setBehindWidth(10);//设置SlidingMenu菜单的宽度  貌似没用
        menu.setBehindOffsetRes(R.dimen.slidingmenuoffset);
        menu.setFadeDegree(0.4f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new QrMenu()).commit();

    }

    @Override
    public void onBackPressed() {
        //点击返回键关闭滑动菜单
        if (menu.isMenuShowing()) {
            menu.showContent();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 切换视图
     * @param fragment
     */
    public void switchContent(Fragment fragment) {
        mContent = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment).commit();
        menu.showContent();
    }
}
