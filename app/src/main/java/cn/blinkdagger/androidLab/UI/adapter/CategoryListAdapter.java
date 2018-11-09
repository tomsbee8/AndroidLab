package cn.blinkdagger.androidLab.UI.adapter;

import android.content.Context;

import cn.blinkdagger.androidLab.Base.BaseBindingAdapter;
import cn.blinkdagger.androidLab.entity.CategoryItem;
import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.databinding.ItemCategoryListBinding;

/**
 * 类描述：
 * 创建人：ls
 * 创建时间：2017/5/14
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class CategoryListAdapter extends BaseBindingAdapter<CategoryItem,ItemCategoryListBinding>  {

    public CategoryListAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_category_list;
    }

    @Override
    protected void onBindItem(ItemCategoryListBinding binding, CategoryItem item) {

    }

}
