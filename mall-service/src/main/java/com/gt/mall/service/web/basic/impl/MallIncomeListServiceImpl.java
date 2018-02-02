package com.gt.mall.service.web.basic.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.basic.MallIncomeListDAO;
import com.gt.mall.entity.basic.MallIncomeList;
import com.gt.mall.service.web.basic.MallIncomeListService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.DateTimeKit;
import com.gt.mall.utils.PageUtil;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 收入记录表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2018-01-30
 */
@Service
public class MallIncomeListServiceImpl extends BaseServiceImpl< MallIncomeListDAO,MallIncomeList > implements MallIncomeListService {

    @Autowired
    private MallIncomeListDAO mallIncomeListDAO;

    @Override
    public PageUtil findByTradePage( Map< String,Object > params ) {
	params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) ) );
	int pageSize = 10;
	int rowCount = mallIncomeListDAO.tradeCount( params );
	PageUtil page = new PageUtil( CommonUtil.toInteger( params.get( "curPage" ) ), pageSize, rowCount, "mallOrder/toIndex.do" );
	params.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	params.put( "maxResult", pageSize );
	if ( rowCount > 0 ) {
	    List< Map< String,Object > > list = mallIncomeListDAO.findByTradePage( params );
	    page.setSubList( list );
	}

	return page;
    }

    /**
     * 商城导出订单
     * type 1 商城订单导出
     */
    @Override
    public HSSFWorkbook exportTradeExcel( Map< String,Object > params, String[] titles, int type, List< Map< String,Object > > shoplist ) {
	HSSFWorkbook workbook = new HSSFWorkbook();//创建一个webbook，对应一个Excel文件
	HSSFSheet sheet = workbook.createSheet( "交易记录" );//在webbook中添加一个sheet,对应Excel文件中的sheet
	HSSFRow rowTitle = sheet.createRow( 0 );//在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
	HSSFCellStyle styleTitle = workbook.createCellStyle();//创建单元格，并设置值表头 设置表头居中
	styleTitle.setAlignment( HSSFCellStyle.ALIGN_CENTER );// 设置单元格左右居中
	HSSFFont fontTitle = workbook.createFont();//创建字体
	fontTitle.setBoldweight( HSSFFont.BOLDWEIGHT_BOLD );//加粗
	fontTitle.setFontName( "宋体" );//设置自提
	fontTitle.setFontHeight( (short) 200 );// 设置字体大小
	styleTitle.setFont( fontTitle );//给文字加样式

	HSSFCell cellTitle;
	//循环标题
	for ( int i = 0; i < titles.length; i++ ) {
	    cellTitle = rowTitle.createCell( i );//创建标题
	    cellTitle.setCellValue( titles[i] );
	    cellTitle.setCellStyle( styleTitle );//给标题加样式
	}
	sheet.setDefaultColumnWidth( 20 );
	sheet.setColumnWidth( 2, 60 * 256 );
	HSSFCellStyle centerStyle = workbook.createCellStyle();
	centerStyle.setAlignment( HSSFCellStyle.ALIGN_CENTER );// 设置单元格左右居中

	HSSFCellStyle leftStyle = workbook.createCellStyle();
	leftStyle.setAlignment( HSSFCellStyle.ALIGN_LEFT );// 设置单元格左右居中
	//循环数据
	if ( type == 1 ) {
	    List< Map< String,Object > > data = mallIncomeListDAO.findByTradePage( params );
	    int j = 1;
	    for ( Map< String,Object > order : data ) {
		j = getTradeOrderList( sheet, centerStyle, leftStyle, order, j, shoplist );
	    }
	}

	return workbook;
    }

    private int getTradeOrderList( HSSFSheet sheet, HSSFCellStyle valueStyle, HSSFCellStyle leftStyle, Map< String,Object > order, int i, List< Map< String,Object > > shopList ) {
	String state = "";//订单状态
	Integer incomeType = null;

	if ( CommonUtil.isNotEmpty( order.get( "incomeType" ) ) ) {
	    incomeType = CommonUtil.toInteger( order.get( "incomeType" ) );
	}
	if ( incomeType == 2 ) {
	    state = "已退款";
	} else {
	    state = "已支付";
	}
	String name = "";
	String price = "";
	String buyerName = "";
	if ( CommonUtil.isNotEmpty( order.get( "proName" ) ) ) {
	    name = order.get( "proName" ).toString();
	}
	if ( CommonUtil.isNotEmpty( order.get( "incomeMoney" ) ) ) {
	    price = order.get( "incomeMoney" ).toString();
	}
	if ( CommonUtil.isNotEmpty( order.get( "incomeUnit" ) ) ) {
	    Integer unit = CommonUtil.toInteger( order.get( "incomeUnit" ) );
	    if ( unit == 2 ) {
		price += "粉币";
	    } else if ( unit == 3 ) {
		price += "积分";
	    }
	}
	if ( CommonUtil.isNotEmpty( order.get( "buyerName" ) ) ) {
	    buyerName = order.get( "buyerName" ).toString();
	}
	Date date = DateTimeKit.parseDate( order.get( "createTime" ).toString(), "yyyy/MM/dd HH:mm" );
	String createTime = DateTimeKit.format( date, DateTimeKit.DEFAULT_DATETIME_FORMAT );
	HSSFRow row = sheet.createRow( i );
	createCell( row, 0, createTime, valueStyle );
	createCell( row, 1, order.get( "proNo" ).toString(), valueStyle );
	createCell( row, 2, name, valueStyle );
	createCell( row, 3, buyerName, valueStyle );
	createCell( row, 4, price, valueStyle );
	createCell( row, 5, state, valueStyle );
	i++;
	return i;
    }

    private void createCell( HSSFRow row, int column, String value, HSSFCellStyle valueStyle ) {
	HSSFCell cell = row.createCell( column );
	cell.setCellValue( value );
	cell.setCellStyle( valueStyle );
    }

    @Override
    public String getCountByTimes( Map< String,Object > params ) {

	return mallIncomeListDAO.getCountByTimes( params );
    }

    @Override
    public List< Map< String,Object > > getCountListByTimes( Map< String,Object > params ) {

	return mallIncomeListDAO.getCountListByTimes( params );
    }
}
