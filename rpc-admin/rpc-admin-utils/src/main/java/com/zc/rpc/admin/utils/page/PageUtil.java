package com.zc.rpc.admin.utils.page;

import java.util.ArrayList;
import java.util.List;

public class PageUtil<T> {

    /**
     * 要分页的list数据
     * -- GETTER --
     *  返回要分页的list数据
     *
     * @return

     */

    private List<T> myList;

    /**
     * 请求的页号，默认第1页
     * -- GETTER --
     *  返回请求页数
     *
     * @return

     */
    private int pageNum = 1;

    /**
     * 每页条数，默认10条
     */
    private int pageSize = 10;

    /**
     * 分页后的数据
     */
    private List<T> data;

    /**
     * 分页后的总页数
     */
    private int pageCount;

    /**
     * 总数据条数
     */
    private int recordCount;

    public boolean isHasPrePage() {
        return isHasPrePage;
    }

    public void setHasPrePage(boolean hasPrePage) {
        isHasPrePage = hasPrePage;
    }

    public boolean isHasNextPage() {
        return isHasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        isHasNextPage = hasNextPage;
    }

    /**
     * 上一页
     */
    private int prePageIndex;
    /**
     * 下一页
     */
    private int nextPageIndex;

    /**
     * 是否第一页
     */
    private boolean firstPage; // 是否第一页

    /**
     * 是否最后一页
     */
    private boolean lastPage; // 是否最后一页

    /**
     * 是否有上一页
     *
     * @return
     */
    private boolean isHasPrePage;

    /**
     * 是否有下一页
     *
     * @return
     */
    private boolean isHasNextPage;


//    /**
//     * 返回分页结果集合
//     *
//     * @return
//     */
//    public PageUtil<T> getResult() {
//        PageUtil<T> p = new PageUtil<>();
//
//        //总数据条数
//        p.setRecordCount(this.recordCount);
//
//        //总页数
//        p.setPageCount(this.pageCount);
//
//
//        //每页的数据条数
//        p.setPageSize(this.pageSize);
//
//        //是否有上一页
//        p.setHasPrePage(this.isHasPrePage);
//
//        //是否有下一页
//        p.setHasNextPage(this.isHasNextPage);
//
//        //上一页页码
//        p.setPrePageIndex(this.prePageIndex);
//
//        //下一页页码
//        p.setNextPageIndex(this.nextPageIndex);
//
//        //分页数据
//        p.setData((ArrayList<T>) this.data);
//
//        //请求的页数
//        p.setPageNum(this.pageNum);
//
//        return p;
//    }


    /**
     * 设置请求的页数
     *
     * @param pageNum
     */
    public void setPageNum(int pageNum) { // 每当页数改变，都会调用这个函数，筛选代码可以写在这里
        this.pageNum = pageNum;

        // 上一页，下一页确定
        prePageIndex = pageNum - 1;
        nextPageIndex = pageNum + 1;
        // 是否第一页，最后一页
        if (pageNum == 1) {
            firstPage = true;
        } else {
            firstPage = false;
        }
        if (pageNum == pageCount) {
            lastPage = true;
        } else {
            lastPage = false;
        }

        // 筛选工作
        data = new ArrayList<T>();
        for (int i = (pageNum - 1) * pageSize; i < pageNum
                * pageSize
                && i < recordCount; i++) {
            data.add(myList.get(i));
        }
    }

    @Override
    public String toString() {
        return "{" +
                "recordCount=" + recordCount+
                ", pageCount=" + pageCount +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", isPrePage=" + isHasPrePage +
                ", isNextPage=" + isHasNextPage +
                ", prePageIndex=" + prePageIndex +
                ", nextPageIndex=" + nextPageIndex +
                ", data=" + data +
                '}';
    }

    /**
     * 设置要分页的数据
     *
     * @param myList
     */
    public PageUtil<T> setMyList(List<T> myList) {
        this.myList = myList;
        // 计算条数
        recordCount = myList.size();
        // 计算页数
        if (recordCount % pageSize == 0) {
            pageCount = recordCount / pageSize;
        } else {
            pageCount = recordCount / pageSize + 1;
        }



        //计算是否有上一页
        if(pageNum == 1){
            isHasPrePage = false;
        }else{
            isHasPrePage = true;
        }

        //计算是否有下一页
        if(pageNum == pageCount){
            isHasNextPage = false;
        }else {
            isHasNextPage = true;
        }


        // 筛选工作
        data = new ArrayList<T>();
        for (int i = (pageNum - 1) * pageSize; i < pageNum
                * pageSize
                && i < recordCount; i++) {
            data.add(myList.get(i));
        }

        return this;
    }

    /**
     * 返回每页请求的数据条数
     *
     * @return
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页的请求条数
     *
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 返回分页后的数据
     *
     * @return
     */
    public List<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
        this.data = data;
    }


    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    public int getPrePageIndex() {
        return prePageIndex;
    }

    public void setPrePageIndex(int prePageIndex) {
        this.prePageIndex = prePageIndex;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }


}
