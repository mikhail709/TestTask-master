package com.test.testtask.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.test.testtask.R;
import com.test.testtask.ui.adapters.GridAdapter;
import com.test.testtask.data.entity.Post;

import java.util.ArrayList;

public class GridFragment extends Fragment {

    private static final String ARG_PARAM_DATA = "data";

    private OnGridFragmentInteractionListener mListener;

    private ArrayList<Post> data;

    private GridView gridView;
    private GridAdapter gridAdapter;

    public GridFragment(){
    }

    public static GridFragment newInstance(ArrayList<Post> gridItemsData) {
        GridFragment fragment = new GridFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM_DATA, gridItemsData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.data = getArguments().getParcelableArrayList(ARG_PARAM_DATA);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid, container, false);

        this.gridView = (GridView) view.findViewById(R.id.grid_view);
        this.gridAdapter = new GridAdapter(getActivity(), data);
        this.gridView.setAdapter(gridAdapter);
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    Post post = (Post) view.getTag(R.string.post_tag_key);
                    mListener.onGridItemPressed(post.getId(), post.getUserId());
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGridFragmentInteractionListener) {
            mListener = (OnGridFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnGridFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnGridFragmentInteractionListener {
        void onGridItemPressed(int postId, int userID);
    }
}
