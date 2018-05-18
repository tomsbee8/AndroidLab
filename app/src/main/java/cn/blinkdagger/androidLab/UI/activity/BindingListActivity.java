package cn.blinkdagger.androidLab.UI.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.blinkdagger.androidLab.Base.BaseActivity;
import cn.blinkdagger.androidLab.Entity.CategoryItem;
import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.UI.adapter.CategoryListAdapter;

/**
 * 类描述：MVVM测试
 * 创建人：ls
 * 创建时间：2017/5/14
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class BindingListActivity extends BaseActivity {


    @BindView(R.id.binding_list_rv)
    RecyclerView bindingListRv;

    private CategoryListAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_binding_list;
    }

    @Override
    protected void initViewAndData() {
        ButterKnife.bind(this);

        List<CategoryItem> hostList = new ArrayList<CategoryItem>() {{
            add(new CategoryItem("10", "测试环境", ""));
            add(new CategoryItem("12", "正式环境", ""));
            add(new CategoryItem("13", "自定义环境", ""));
        }};
        adapter = new CategoryListAdapter(this);
        bindingListRv.setAdapter(adapter);
        adapter.getItems().addAll(hostList);


    }
}
