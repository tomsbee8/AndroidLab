package cn.blinkdagger.androidLab.UI.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import cn.blinkdagger.androidLab.Base.BaseActivity;
import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.UI.fragment.DrawerFragment;
import cn.blinkdagger.androidLab.Utils.SystemBarUtil;

import butterknife.BindView;

public class DrawerActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.list_view_menu)
    ListView listViewMenu;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar_center_tv)
    TextView toolbarCenterTV;

    DrawerFragment fragment;
    public static final int DISPLAY_SPRING_IMAGE =1;
    public static final int DISPLAY_SUMMER_IMAGE =2;
    public static final int DISPLAY_AUTUMN_IMAGE =3;
    public static final int DISPLAY_WINTER_IMAGE =4;

    @Override
    protected int getLayout() {
        return R.layout.activity_drawer;
    }

    @Override
    protected void initViewAndData() {
        initToolBar();
        listViewMenu.setOnItemClickListener(this);
        fragment =new DrawerFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fl_main, fragment).commit();

        SystemBarUtil.setTranslucentStatusBar(this,true);
//        SystemBarUtil.setStatusBarColor(this,Color.parseColor("#000000"),true);

    }

    private void initToolBar (){
        // 设置返回按钮点击事件
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerVisible(listViewMenu)){
                    drawerLayout.openDrawer(listViewMenu);
                }else{
                    drawerLayout.closeDrawer(listViewMenu);
                }
            }
        });
        setSupportActionBar(mToolbar);
        // 设置隐藏标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // 设置显示返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if(fragment!=null){
            fragment.displayImage(DISPLAY_SPRING_IMAGE);
        }
        switch (position) {
            case 0:
                fragment.displayImage(DISPLAY_SPRING_IMAGE);
                break;
            case 1:
                fragment.displayImage(DISPLAY_SUMMER_IMAGE);
                break;
            case 2:
                fragment.displayImage(DISPLAY_AUTUMN_IMAGE);
                break;
            case 3:
                fragment.displayImage(DISPLAY_WINTER_IMAGE);
                break;
            default:
                break;
        }
    }
}
