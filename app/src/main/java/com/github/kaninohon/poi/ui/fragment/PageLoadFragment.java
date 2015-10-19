package com.github.kaninohon.poi.ui.fragment;

import static com.github.kaninohon.poi.utils.Reflect.*;
import java.util.ArrayList;

public abstract class PageLoadFragment extends SwipeRefreshFragment {

    private int mCurrentPage = 0;

    protected int getDataPage(){
        return mCurrentPage;
    }

    //回到第一页并且刷新
    public void refreshToFirstPage() {
        mCurrentPage = 1;
    }

    //第一页刷新成功
    protected <T> void onRefreshFirstPageSuccess(T oldDatas, T newDatas){
        ArrayList<Object> oldObjects = (ArrayList<Object>)oldDatas;
        ArrayList<Object> newObjects = (ArrayList<Object>)newDatas;
        oldObjects.clear();
        oldObjects.addAll(newObjects);
    }

    //刷新下一页
    protected void refreshNextPage(){
        mCurrentPage++;
    }
    //刷新下一页成功
    protected <T> boolean onRefreshNextPageSuccess(T oldDatas, T newDatas){
        boolean isAddNew = false;
        ArrayList<Object> oldObjects = (ArrayList<Object>)oldDatas;
        ArrayList<Object> newObjects = (ArrayList<Object>)newDatas;

        for (Object newObj : newObjects) {
            boolean hasload = false;
            for (Object oldObj : oldObjects) {
                String oldId = on(oldObj).call("getId").get();
                String newId = on(newObj).call("getId").get();

                if(oldId.equals(newId)) {
                    hasload = true;
                    break;
                }
            }
            if (!hasload) {
                oldObjects.add(newObj);
                isAddNew = true;
            }
        }
        if(!isAddNew){
            mCurrentPage--;
            return false;
        }
        return true;
    }

    //刷新失败
    protected void onRefreshFail(){
        mCurrentPage--;
    }

    //刷新当前页
    protected void refreshCurrentPage(){

    }



}
