package cn.blinkdagger.androidLab.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.blinkdagger.androidLab.entity.RemarkItem;
import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.ui.adapter.MaximListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：ls
 * 创建时间：2018/1/24
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class MaximListFragment extends Fragment {

    private static final String ARGUMENT_TYPE = "tagType";
    private int type = 0;

    private RecyclerView maximListRV;
    private MaximListAdapter adapter;


    public MaximListFragment() {
    }

    public static MaximListFragment newInstance(int type) {
        MaximListFragment f = new MaximListFragment();
        Bundle b = new Bundle();
        b.putInt(ARGUMENT_TYPE, type);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        type = getArguments().getInt(ARGUMENT_TYPE);
        View rootView = inflater.inflate(R.layout.fragment_maxim_layout, container,false);

        maximListRV = rootView.findViewById(R.id.maxim_list_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        maximListRV.setLayoutManager(linearLayoutManager);


        List<RemarkItem> itemList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            itemList.add(new RemarkItem("第" + i, "啊实打实的" + i));
        }
        adapter = new MaximListAdapter(getActivity(), itemList);
        maximListRV.setAdapter(adapter);


        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
