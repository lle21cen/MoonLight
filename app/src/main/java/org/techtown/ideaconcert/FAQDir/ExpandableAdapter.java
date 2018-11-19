package org.techtown.ideaconcert.FAQDir;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.ideaconcert.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> groupItems;
    private HashMap<String, ArrayList<String>> childItems;

    public ExpandableAdapter(Context context, ArrayList<String> groupItems, HashMap<String, ArrayList<String>> childItems) {
        this.context = context;
        this.groupItems = groupItems;
        this.childItems = childItems;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupItems.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groupItems.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final String question = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.faq_fragment_item, parent, false);
        }
        TextView groupQuestionView = convertView.findViewById(R.id.faq_question);
        groupQuestionView.setText(question);

        ImageView openCloseButton = convertView.findViewById(R.id.faq_open_close_btn);
        if (isExpanded) {
            openCloseButton.setImageResource(R.drawable.closing);
        } else {
            openCloseButton.setImageResource(R.drawable.open);
        }

        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.childItems.get(this.groupItems.get(groupPosition)).get(childPosition);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.childItems.get(this.groupItems.get(groupPosition)).size();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.faq_child_item, null);
        }

        TextView childTextView = convertView.findViewById(R.id.faq_child_text);
        childTextView.setText(childText);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
