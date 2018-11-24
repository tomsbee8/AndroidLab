package cn.blinkdagger.androidLab.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.blinkdagger.androidLab.R;


public class DrawerFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private int mParam1;

    public DrawerFragment() {
        // Required empty public constructor
    }

    public static DrawerFragment newInstance(String param1) {
        DrawerFragment fragment = new DrawerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drawer, container, false);
    }


    public void displayImage(int displayImage){
        switch (displayImage){

        }
    }
}
