package com.test.testtask.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.test.testtask.R;
import com.test.testtask.data.entity.Post;
import com.test.testtask.utils.TestTaskApplication;

import java.util.List;

public class GridAdapter extends BaseAdapter{

    public class ViewHolder{
        public TextView tvID;
        public TextView tvTitle;
    }

    private List<Post> posts;
    private LayoutInflater layoutInflater;

    public GridAdapter(Context context, List<Post> data){
        this.posts = data;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    public Post getPostItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return posts.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null){
            view = layoutInflater.inflate(R.layout.item_grid_view, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvID = (TextView) view.findViewById(R.id.tvPostId);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.tvPostTitle);
            view.setTag(R.string.viewholder_tag_key, viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag(R.string.viewholder_tag_key);
        }

        Post post = getPostItem(position);
        viewHolder.tvID.setText(String.valueOf(post.getId()));
        viewHolder.tvTitle.setText(post.getTitle());

        view.setMinimumHeight(parent.getMeasuredHeight() / 2);

        view.setTag(R.string.post_tag_key, post);

        return view;
    }
}
