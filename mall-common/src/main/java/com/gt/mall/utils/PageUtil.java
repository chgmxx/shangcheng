package com.gt.mall.utils;

import java.util.List;

/**
 * 分页工具类
 *
 * @author qusk
 * @date 2015年11月10日
 * @description
 */
@SuppressWarnings( "rawtypes" )
public class PageUtil {

    /** 当前页 **/
    private int curPage = 0;

    /** 总页数 **/
    private int pageCount = 0;

    /** 总条数 **/
    private int rowCount;

    /** 每页显示记录数 **/
    private int pageSize;

    /** 每页要显示的集合 **/
    private List subList;

    // 默认每页显示记录数
    public static final int defaultPageSize = 10;

    //默认当前页
    public static final int DEFAULT_CURRENT_PAGE = 1;

    public PageUtil() {
    }

    /**
     * 初始化分页
     *
     * @param curPage  当前页
     * @param pageSize 每页条数
     * @param rowCount 总条数
     * @param url      后台地址
     */
    public PageUtil( int curPage, int pageSize, int rowCount, String url ) {
        if ( pageSize == 0 ) {
            pageSize = defaultPageSize;
        }
        this.rowCount = rowCount;
        this.pageSize = pageSize;
        this.countMaxPage();
        if ( curPage <= 0 ) {
            this.curPage = DEFAULT_CURRENT_PAGE;
        } else {
            if ( curPage > pageCount ) {
                this.curPage = pageCount;
            } else {
                this.curPage = curPage;
            }
        }

        if ( this.curPage <= 0 ) {
            this.curPage = DEFAULT_CURRENT_PAGE;
        }
    }

    /**
     * 计算总页数
     */
    private void countMaxPage() {
        if ( rowCount % pageSize == 0 ) {
            pageCount = rowCount / pageSize;
        } else {
            pageCount = rowCount / pageSize + 1;
        }

    }

    /**
     * 获取每页显示集合
     *
     * @return
     */
    public List getSubList() {
        return this.subList;
    }

    /**
     * <设置每页显示集合对象>
     * <功能详细描述>
     *
     * @param subList [参数说明]
     */
    public void setSubList( List subList ) {
        this.subList = subList;
    }

    /**
     * 读取每页显示记录数
     *
     * @return int
     */
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize( int pageSize ) {
        this.pageSize = pageSize;

        this.countMaxPage();
        if ( curPage > pageCount ) {
            this.curPage = pageCount;
        }
    }

    /**
     * 读取当前显示页数
     *
     * @return int
     */
    public int getCurPage() {
        return curPage;
    }

    public void setCurPage( int curPage ) {
        this.curPage = curPage;
    }

    /**
     * 读取总页数
     *
     * @return int
     */
    public int getPageCount() {
        return pageCount;
    }

    /**
     * 读取总记录数
     *
     * @return int
     */
    public int getRowCount() {
        return rowCount;
    }

    public void setPageCount( int pageCount ) {
        this.pageCount = pageCount;
    }

    public void setRowCount( int rowCount ) {
        this.rowCount = rowCount;
    }

}
