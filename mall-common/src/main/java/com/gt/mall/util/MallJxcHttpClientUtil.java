package com.gt.mall.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nan 2015-11
 */
public class MallJxcHttpClientUtil {

    public static String TOKEN_STR = "";

    @Autowired
    private static JxcConfigUtil myConfig;

    public static String login() {
	System.out.println( "第一次登陆" );
	String url = myConfig.getJxcUrl() + "/erp/b/login";
	Map< String,Object > params = new HashMap< String,Object >();
	params.put( "account", myConfig.getJxcAccount() );
	params.put( "pwd", myConfig.getJxcPwd() );
	System.out.println( url );
	try {
	    JSONObject jsonObject = JSONObject.parseObject( MallHttpClientUtil.httpPostRequest( url, params ) );
	    System.out.println( jsonObject );
	    if ( jsonObject.getString( "code" ).equals( "1001" ) ) {
		JSONObject tokens = jsonObject.getJSONObject( "data" );
		TOKEN_STR = tokens.getString( "token" );
		return TOKEN_STR;
	    }
	} catch ( UnsupportedEncodingException e ) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 批量新增或修改商品和商品库存
     *
     * @param params  商品参数
     * @param isFirst 是否第一次，避免死循环
     *
     * @return
     */
    public static JSONArray batchSave( Map< String,Object > params, boolean isFirst ) {
	System.out.println( "批量新增或修改商品和商品库存:" + params );
	String url = myConfig.getJxcUrl() + "/erp/batchSave";
	if ( CommonUtil.isEmpty( TOKEN_STR ) ) {
	    TOKEN_STR = login();
	}
	try {
	    JSONObject jsonObject = JSONObject.parseObject( MallHttpClientUtil.httpPostRequest( url, params, TOKEN_STR ) );
	    String code = jsonObject.getString( "code" );
	    System.out.println( jsonObject );
	    if ( code.equals( "1001" ) ) {
		System.out.println( jsonObject.getJSONArray( "data" ) );

		return jsonObject.getJSONArray( "data" );
	    } else if ( code.equals( "1005" ) || code.equals( "1006" ) ) {
		if ( isFirst ) {
		    login();
		    return batchSave( params, false );
		}

	    }
	} catch ( UnsupportedEncodingException e ) {
	    e.printStackTrace();
	}

	return null;
    }

    /**
     * 批量新增或修改商品规格
     *
     * @param params  规格信息
     * @param isFirst 是否第一次，避免死循环
     *
     * @return
     */
    public static JSONArray batchAttrSave( Map< String,Object > params, boolean isFirst ) {
	System.out.println( "批量新增或修改商品规格：" + params );
	String url = myConfig.getJxcUrl() + "/erp/attr/batchSave";
	if ( CommonUtil.isEmpty( TOKEN_STR ) ) {
	    TOKEN_STR = login();
	}
	System.out.println( "规格传参：" + JSONObject.toJSONString( params ) );
	try {
	    JSONObject jsonObject = JSONObject.parseObject( MallHttpClientUtil.httpPostRequest( url, params, TOKEN_STR ) );
	    String code = jsonObject.getString( "code" );
	    System.out.println( jsonObject );
	    if ( code.equals( "1001" ) ) {
		System.out.println( "规格返回值:" + jsonObject.getJSONArray( "data" ) );

		return jsonObject.getJSONArray( "data" );
	    } else if ( code.equals( "1005" ) || code.equals( "1006" ) ) {
		if ( isFirst ) {
		    login();
		    return batchAttrSave( params, false );
		}
	    }
	} catch ( UnsupportedEncodingException e ) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 保存或修改仓库
     *
     * @param params  保存或修改仓库的参数
     * @param isFirst 是否第一次，避免死循环
     *
     * @return true 保存/修改成功
     */
    public static boolean saveUpdateWarehouse( Map< String,Object > params, boolean isFirst ) {
	System.out.println( "保存或修改仓库：" + params );
	String url = myConfig.getJxcUrl() + "/erp/updateWarehouse";
	if ( CommonUtil.isEmpty( TOKEN_STR ) ) {
	    TOKEN_STR = login();
	}
	try {
	    JSONObject jsonObject = JSONObject.parseObject( MallHttpClientUtil.httpPostRequest( url, params, TOKEN_STR ) );
	    String code = jsonObject.getString( "code" );
	    System.out.println( jsonObject );
	    if ( code.equals( "1000" ) ) {
		return true;
	    } else if ( code.equals( "1005" ) || code.equals( "1006" ) ) {
		if ( isFirst ) {
		    login();
		    return saveUpdateWarehouse( params, false );
		}
	    }
	} catch ( UnsupportedEncodingException e ) {
	    e.printStackTrace();
	}
	return false;
    }

    /**
     * 发货退货时调用修改库存
     *
     * @param params  库存参数
     * @param isFirst 是否第一次，避免死循环
     *
     * @return true 保存/修改成功
     */
    public static boolean inventoryOperation( Map< String,Object > params, boolean isFirst ) {
	System.out.println( "发货退货时调用修改库存：" + params );
	String url = myConfig.getJxcUrl() + "/erp/order/inventory/operation";
	if ( CommonUtil.isEmpty( TOKEN_STR ) ) {
	    TOKEN_STR = login();
	}
	try {
	    JSONObject jsonObject = JSONObject.parseObject( MallHttpClientUtil.httpPostRequest( url, params, TOKEN_STR ) );
	    String code = jsonObject.getString( "code" );
	    System.out.println( jsonObject );
	    if ( code.equals( "1001" ) ) {
		return true;
	    } else if ( code.equals( "1005" ) || code.equals( "1006" ) ) {
		if ( isFirst ) {
		    login();
		    return inventoryOperation( params, false );
		}
	    }
	} catch ( UnsupportedEncodingException e ) {
	    e.printStackTrace();
	}
	return false;
    }

    /**
     * 查询同步/未同步的商品
     *
     * @param params  门店id和 是否查询同步商品
     * @param isFirst 是否第一次，避免死循环
     *
     * @return true 保存/修改成功
     */
    public static JSONArray syncProductCheck( Map< String,Object > params, boolean isFirst ) {
	String url = myConfig.getJxcUrl() + "/erp/order/sync/product/check";
	if ( CommonUtil.isEmpty( TOKEN_STR ) ) {
	    TOKEN_STR = login();
	}
	System.out.println( "查询同步/未同步的商品：" + params );
	try {
	    JSONObject jsonObject = JSONObject.parseObject( MallHttpClientUtil.httpPostRequest( url, params, TOKEN_STR ) );
	    String code = jsonObject.getString( "code" );
	    System.out.println( jsonObject );
	    if ( code.equals( "1001" ) ) {
		System.out.println( "查询同步/未同步的商品返回值：" + jsonObject.getJSONArray( "data" ) );
		return jsonObject.getJSONArray( "data" );
	    } else if ( code.equals( "1005" ) || code.equals( "1006" ) ) {
		if ( isFirst ) {
		    login();
		    return syncProductCheck( params, false );
		}
	    }
	} catch ( UnsupportedEncodingException e ) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 查询单个商品的规格详情和库存
     *
     * @param params  库存参数
     * @param isFirst 是否第一次，避免死循环
     *
     * @return true 保存/修改成功
     */
    public static JSONObject getInventoryById( Map< String,Object > params, boolean isFirst ) {
	String url = myConfig.getJxcUrl() + "/erp/order/sync/pro/inventory";
	if ( CommonUtil.isEmpty( TOKEN_STR ) ) {
	    TOKEN_STR = login();
	}
	System.out.println( "查询单个商品的规格详情和库存：" + params );
	try {
	    JSONObject jsonObject = JSONObject.parseObject( MallHttpClientUtil.httpPostRequest( url, params, TOKEN_STR ) );
	    String code = jsonObject.getString( "code" );
	    System.out.println( "查询单个商品的规格详情和库存返回：" + jsonObject );
	    if ( code.equals( "1001" ) ) {
		return jsonObject.getJSONObject( "data" );
	    } else if ( code.equals( "1005" ) || code.equals( "1006" ) ) {
		if ( isFirst ) {
		    login();
		    return getInventoryById( params, false );
		}
	    }
	} catch ( UnsupportedEncodingException e ) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 查询商品规格
     *
     * @param params  库存参数
     * @param isFirst 是否第一次，避免死循环
     *
     * @return true 保存/修改成功
     */
    public static JSONArray getProductAttrs( Map< String,Object > params, boolean isFirst ) {
	System.out.println( "查询商品规格：" + params );
	String url = myConfig.getJxcUrl() + "/erp/query/getProductAttrs/shop";
	if ( CommonUtil.isEmpty( TOKEN_STR ) ) {
	    TOKEN_STR = login();
	}
	try {
	    JSONObject jsonObject = JSONObject.parseObject( MallHttpClientUtil.httpPostRequest( url, params, TOKEN_STR ) );
	    String code = jsonObject.getString( "code" );
	    System.out.println( jsonObject );
	    if ( code.equals( "1001" ) ) {
		return jsonObject.getJSONArray( "data" );
	    } else if ( code.equals( "1005" ) || code.equals( "1006" ) ) {
		if ( isFirst ) {
		    login();
		    return getProductAttrs( params, false );
		}
	    }
	} catch ( UnsupportedEncodingException e ) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 根据商品id查询库存数量
     *
     * @param params  库存参数
     * @param isFirst 是否第一次，避免死循环
     *
     * @return true 保存/修改成功
     */
    public static JSONArray inventoryByProduct( Map< String,Object > params, boolean isFirst ) {
	String url = myConfig.getJxcUrl() + "erp/order/sync/inventoryByProduct";
	if ( CommonUtil.isEmpty( TOKEN_STR ) ) {
	    TOKEN_STR = login();
	}
	System.out.println( "根据商品id查询库存数量：" + params );
	try {
	    JSONObject jsonObject = JSONObject.parseObject( MallHttpClientUtil.httpPostRequest( url, params, TOKEN_STR ) );
	    String code = jsonObject.getString( "code" );
	    System.out.println( "根据商品id查询库存数量返回:" + jsonObject );
	    if ( code.equals( "1001" ) ) {
		return jsonObject.getJSONArray( "data" );
	    } else if ( code.equals( "1005" ) || code.equals( "1006" ) ) {
		if ( isFirst ) {
		    login();
		    return inventoryByProduct( params, false );
		}
	    }
	} catch ( UnsupportedEncodingException e ) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 根据商品详情id查询库存
     *
     * @param params  库存参数
     * @param isFirst 是否第一次，避免死循环
     *
     * @return true 保存/修改成功
     */
    public static Object getInvNumByInvenId( Map< String,Object > params, boolean isFirst ) {
	String url = myConfig.getJxcUrl() + "/erp/order/sync/attrs/inventory";
	if ( CommonUtil.isEmpty( TOKEN_STR ) ) {
	    TOKEN_STR = login();
	}
	System.out.println( "根据商品详情id查询库存：" + params );
	try {
	    JSONObject jsonObject = JSONObject.parseObject( MallHttpClientUtil.httpPostRequest( url, params, TOKEN_STR ) );
	    String code = jsonObject.getString( "code" );
	    System.out.println( "根据商品详情id查询库存返回：" + jsonObject );
	    if ( code.equals( "1001" ) ) {
		return jsonObject.get( "data" );
	    } else if ( code.equals( "1005" ) || code.equals( "1006" ) ) {
		if ( isFirst ) {
		    login();
		    return getInvNumByInvenId( params, false );
		}
	    }
	} catch ( UnsupportedEncodingException e ) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 同步接口回调
     *
     * @param params  库存参数
     * @param isFirst 是否第一次，避免死循环
     *
     * @return true 保存/修改成功
     */
    public static boolean syncCallback( Map< String,Object > params, boolean isFirst ) {
	String url = myConfig.getJxcUrl() + "/erp/order/sync/product/callback";
	if ( CommonUtil.isEmpty( TOKEN_STR ) ) {
	    TOKEN_STR = login();
	}
	System.out.println( "同步接口回调：" + params );
	try {
	    JSONObject jsonObject = JSONObject.parseObject( MallHttpClientUtil.httpPostRequest( url, params, TOKEN_STR ) );
	    String code = jsonObject.getString( "code" );
	    System.out.println( "同步接口回调返回：" + jsonObject );
	    if ( code.equals( "1001" ) ) {
		return true;
	    } else if ( code.equals( "1005" ) || code.equals( "1006" ) ) {
		if ( isFirst ) {
		    login();
		    return syncCallback( params, false );
		}
	    }
	} catch ( UnsupportedEncodingException e ) {
	    e.printStackTrace();
	}
	return false;
    }


    public static void main( String[] args ) {

	//{orders=[{"rootUid":42,"uName":"gt123456","uType":1,"remark":"商城下单","shopId":17,"uId":42,"type":1,
	// "products":[{"amount":1,"id":100,"price":7}]}]}
	//减库存
    	/*Map<String, Object> proMap = new HashMap<String, Object>();
    	proMap.put("id", 1);
    	proMap.put("amount", 1);
    	proMap.put("price", "7");
    	List<Map<String, Object>> proList = new ArrayList<Map<String,Object>>();
    	proList.add(proMap);




    	//根据商品id查询库存信息
    	/*Map<String, Object> params = new HashMap<String, Object>();
    	params.put("shopIds", "17");
    	params.put("proIds", "36");
    	System.out.println(params);
    	JSONArray arr = inventoryByProduct(params, true);
    	System.out.println(arr);
    	if(arr != null && arr.size() > 0){
    		for (Object object : arr) {
				JSONArray proArr = JSONArray.parseArray(object.toString());
				System.out.println(proArr.get(0)+"---"+proArr.get(1));
			}
    	}*/

	//查询商家规格
    	/*Map<String, Object> params = new HashMap<String, Object>();
    	params.put("rootUid", 42);
    	JSONArray arr = getProductAttrs(params, true);
    	if(arr != null && arr.size() > 0){
    		for (Object object : arr) {
				JSONObject specObj = JSONObject.parseObject(object.toString());
				int erpNameId = specObj.getInteger("id");
				String erpName = specObj.getString("name");
				System.out.println("父类规格："+erpNameId+"---"+erpName);

				if(CommonUtil.isNotEmpty(specObj.get("childs"))){
					JSONArray childArr = specObj.getJSONArray("childs");
					if(childArr != null && childArr.size() > 0){
						for (Object object2 : childArr) {
							JSONObject childObj = JSONObject.parseObject(object2.toString());
							int erpValueId = childObj.getInteger("id");
							String erpValue = childObj.getString("name");

							System.out.println("子类规格："+erpValueId+"---"+erpValue);
						}
					}
				}

			}
    	}*/

	//查询详情库存
    	/*Map<String, Object> params = new HashMap<String, Object>();
    	params.put("shopId", 18);
    	params.put("productId", 29);
    	JSONObject obj = getInventoryById(params, true);
    	System.out.println(obj);
    	if(CommonUtil.isNotEmpty(obj.get("attrs"))){
    		JSONArray arr = JSONArray.parseArray(obj.getString("attrs"));
    		if(arr != null && obj.size() > 0){
    			for (Object object : arr) {
					JSONObject attrObj = JSONObject.parseObject(object.toString());
					System.out.println(attrObj.get("amount"));
				}
    		}
    	}*/

	//查询单个详情的库存
    	/*Map<String, Object> params = new HashMap<String, Object>();
    	params.put("shopId", 17);
    	params.put("attrsId", 99);
    	Object obj = getInvNumByInvenId(params, true);
    	System.out.println(obj);*/

	//查询未同步的商品
	/*Map< String,Object > params = new HashMap< String,Object >();
	params.put( "rootUid", 42 );
	params.put( "sync", 0 );
	System.out.println( params );
	JSONArray proArr = syncProductCheck( params, true );
	if ( proArr != null && proArr.size() > 0 ) {
	    for ( Object object : proArr ) {//循环商品
		JSONObject proObj = JSONObject.parseObject( object.toString() );
		System.out.println( "商品名称:" + proObj.getString( "name" ) );
		System.out.println( "是否参与网售：" + proObj.getBoolean( "netSales" ) );
		System.out.println( "是否打折：" + proObj.getBoolean( "discount" ) );
		System.out.println( "是否允许退货：" + proObj.getBoolean( "returns" ) );
		//商品库存详情
		System.out.println( "商品库存：" + proObj.getJSONArray( "attrs" ) );

		JSONArray invenArr = proObj.getJSONArray( "attrs" );
		if ( invenArr != null && invenArr.size() > 0 ) {
		    for ( Object object2 : invenArr ) {//循环库存
			JSONObject invenObj = JSONObject.parseObject( object2.toString() );
			System.out.print( "规格详情id：" + invenObj.getInteger( "id" ) );
			System.out.print( "--父类规格id：" + invenObj.getString( "attrIds" ) );
			System.out.print( "--父类规格：" + invenObj.getString( "attrNames" ) );
			System.out.println( "--规格价：" + invenObj.getDouble( "retailPrice" ) );
			System.out.println( "---门店id：" + invenObj.getInteger( "shopId" ) );

			if ( CommonUtil.isNotEmpty( invenObj.get( "norms" ) ) ) {
			    JSONArray specArr = invenObj.getJSONArray( "norms" );
			    if ( specArr != null && specArr.size() > 0 ) {
				for ( Object object3 : specArr ) {//循环规格
				    JSONObject specObj = JSONObject.parseObject( object3.toString() );

				    System.out.print( "父类规格id:" + specObj.getInteger( "id" ) );
				    System.out.print( "--父类规格：" + specObj.getString( "name" ) );

				    JSONObject childSpecObj = specObj.getJSONObject( "child" );
				    System.out.print( "----子类规格id:" + childSpecObj.getInteger( "id" ) );
				    System.out.println( "--子类规格：" + childSpecObj.getString( "name" ) );
				}
			    }
			}

		    }
		}
		System.out.println( "-----------------" );
	    }
	}*/


    	/*if(arr != null && arr.size() > 0){
    		for (Object object : arr) {
				JSONObject obj = JSONObject.parseObject(object.toString());

				System.out.println("规格值id："+obj.getString("attrIds"));
				System.out.println("规格值："+obj.getString("attrNames"));
				System.out.println("规格详情id："+obj.getInteger("id"));
				System.out.println("商品单价："+obj.getDoubleValue("lsj"));

				if(CommonUtil.isNotEmpty(obj.get("product"))){
					JSONObject productObj = JSONObject.parseObject(obj.get("product").toString());

					System.out.println("商品名称："+productObj.getString("name"));
					System.out.println("是否参与网售："+productObj.getBoolean("netSales"));
					System.out.println("是否打折："+productObj.getBoolean("discount"));
					System.out.println("是否允许退货："+productObj.getBoolean("returns"));

				}

				System.out.println("-------------");

			}
    	}
    	System.out.println(arr);*/

	//    	//批量新增规格
	//    	Map<String, Object> attrParams = new HashMap<String, Object>();
	//    	JSONObject specParams = new JSONObject();
	//    	specParams.put("uId", 42);
	//    	specParams.put("uType", 1);
	//    	specParams.put("uName", "gt123456");
	//    	specParams.put("rootUid", 42);
	//    	Map<String, Object> specParams2 = new HashMap<String, Object>();
	//    	specParams2.put("id", "");
	//    	specParams2.put("name", "尺码");
	//    	specParams2.put("parentId", "");
	//    	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	//    	list.add(specParams2);
	//    	specParams.put("norms", list);
	//    	attrParams.put("attrs", specParams);
	//
	////    	System.out.println(attrParams);
	//    	JSONArray arr = batchAttrSave(attrParams,true);
	//    	JSONObject jObj = JSONObject.parseObject(arr.get(0).toString());
	//    	int nameId = jObj.getInteger("id") ;
	//    	System.out.println("父类规格id："+nameId);
	//
	//    	attrParams = new JSONObject();
	//    	Map<String, Object> specParams3 = new HashMap<String, Object>();
	//    	specParams3.put("id", "");
	//    	specParams3.put("name", "iPhone 7 4.7 英寸显示屏");
	//    	specParams3.put("parentId", nameId);
	//
	//    	list = new ArrayList<Map<String,Object>>();
	//    	list.add(specParams3);
	//    	specParams.put("norms", list);
	//
	//    	attrParams.put("attrs", specParams);
	//
	//
	////    	System.err.println(JSONObject.toJSON(attrParams));
	//    	arr = batchAttrSave(attrParams,true);
	//    	jObj = JSONObject.parseObject(arr.get(0).toString());
	//    	int valueId = jObj.getInteger("id") ;
	//    	System.out.println("子类规格id："+valueId);
	//
	//    	Map<String, Object> storeParams = new HashMap<String, Object>();
	//    	storeParams.put("createUid", "42");
	//    	storeParams.put("uidType", 1);
	//    	List<Map<String, Object>> lists = new ArrayList<Map<String,Object>>();
	//    	Map<String, Object> map = new HashMap<String, Object>();
	//    	map.put("id", 17);
	//    	map.put("name", "广东谷通科技有限公司");
	//    	map.put("address", "广东省惠州市惠城区惠州大道赛格假日广场10楼1007-1008室");
	//    	map.put("phone", "0752-2329043");
	//    	map.put("principal", "小多");
	//    	lists.add(map);
	//    	storeParams.put("shopList", JSONArray.toJSON(lists));
	//    	saveUpdateWarehouse(storeParams, true);
	//
	//
	////    	int valueId = 136;
	//
	//    	Map<String, Object> params = new HashMap<String, Object>();
	//    	params.put("uId", 42);
	//    	params.put("uType", 1);
	//    	params.put("uName", "gt123456");
	//    	params.put("rootUid", 42);
	////    	params.put("shopId", 17);
	//    	List<Map<String, Object>> productList = new ArrayList<Map<String,Object>>();
	//    	Map<String, Object> productParams = new HashMap<String, Object>();
	//    	productParams.put("id", "");
	//    	productParams.put("name", "iphone7");
	//
	//    	List<Map<String, Object>> invList = new ArrayList<Map<String,Object>>();
	//    	Map<String, Object> invParams = new HashMap<String, Object>();
	////    	invParams.put("id", "");
	//    	invParams.put("ids", valueId);//规格id组
	//    	invParams.put("names", "iPhone 7 4.7 英寸显示屏");
	//    	invParams.put("amount", 45);
	//    	invParams.put("price", 1);
	//    	invParams.put("shopId", 17);
	//    	invList.add(invParams);
	//    	productParams.put("norms", invList);
	//
	//    	productList.add(productParams);
	//    	params.put("pros", productList);
	//
	//    	Map<String, Object> proParams = new HashMap<String, Object>();
	//    	proParams.put("pros", JSONObject.toJSON(params));
	//
	//    	System.out.println(proParams);
	//    	batchSave(proParams,true);
    }

}
