package cn.blinkdagger.androidLab.ui.activity;

import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import cn.blinkdagger.androidLab.base.BaseActivity;
import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.ui.fragment.DrawerFragment;


public class DrawerActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private Toolbar mToolbar;
    private ListView listViewMenu;
    private DrawerLayout drawerLayout;

    DrawerFragment fragment;
    public static final int DISPLAY_SPRING_IMAGE =1;
    public static final int DISPLAY_SUMMER_IMAGE =2;
    public static final int DISPLAY_AUTUMN_IMAGE =3;
    public static final int DISPLAY_WINTER_IMAGE =4;

    @Override
    public  int getContentLayout() {
        return R.layout.activity_drawer;
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected void initView() {
        mToolbar = findViewById(R.id.toolbar);
        listViewMenu = findViewById(R.id.list_view_menu);
        drawerLayout = findViewById(R.id.drawer_layout);
    }

    @Override
    protected void initData() {
        listViewMenu.setOnItemClickListener(this);
        fragment =new DrawerFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fl_main, fragment).commit();
    }

    @Override
    protected void onNavigationIconClick() {
        if(drawerLayout.isDrawerVisible(listViewMenu)){
            drawerLayout.openDrawer(listViewMenu);
        }else{
            drawerLayout.closeDrawer(listViewMenu);
        }
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
