package com.github.kaninohon.poi.ui.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.View;

import com.github.kaninohon.poi.ui.adapter.RecyclerViewAdapter;

public class ContextMenuRecyclerView extends RecyclerView {


    public ContextMenuRecyclerView(Context context) {
        super(context);
    }

    public ContextMenuRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContextMenuRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private ContextMenu.ContextMenuInfo mContextMenuInfo = null;

    @Override
    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return mContextMenuInfo;
    }




    public void saveContext(int position, RecyclerViewAdapter.ViewHolder viewHolder){
        if (position >= 0) {
            mContextMenuInfo = new RecyclerContextMenuInfo(position, viewHolder);
        }
    }

    public static class RecyclerContextMenuInfo implements ContextMenu.ContextMenuInfo {

        public RecyclerContextMenuInfo(int position, RecyclerViewAdapter.ViewHolder viewHolder) {
            this.position = position;
            this.viewHolder = viewHolder;
        }

        public int position;
        public RecyclerViewAdapter.ViewHolder viewHolder;
    }

}