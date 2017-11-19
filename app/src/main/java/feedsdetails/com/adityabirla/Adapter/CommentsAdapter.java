package feedsdetails.com.adityabirla.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import feedsdetails.com.adityabirla.Activity.CommentsActivity;
import feedsdetails.com.adityabirla.Pojo.Comments;
import feedsdetails.com.adityabirla.R;

/**
 * Created by Amaresh on 11/19/17.
 */

public class CommentsAdapter extends BaseAdapter {
    Context _context;
    Holder holder;
    ArrayList<Comments> list;
    public CommentsAdapter(CommentsActivity commentsActivity, ArrayList<Comments> cList) {
        this._context=commentsActivity;
        this.list=cList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class Holder{
        TextView name,time,comments;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Comments _pos=list.get(position);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.activity_comments_adapter, parent, false);
            holder.name=(TextView)convertView.findViewById(R.id.name);
           // holder.time=(TextView)convertView.findViewById(R.id.d);
            holder.comments=(TextView)convertView.findViewById(R.id.cmt_text);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.name.setTag(holder);
        holder.comments.setTag(holder);
        return convertView;
    }
}
