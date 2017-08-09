package renshou.database.barcode;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;

/**
 * @author xuhui
 * @desc 条形码管理
 */
public class BarCodeService {

	/**
	 * @desc 展示成品半成品数据
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public static Page<Record> getProduct(Integer pageNumber,Integer pageSize){
		String sql = "SELECT semimanufactures_number as product_num,trade_name"
				+ ",specifications,measurement_unit,remark from semimanufactures UNION"
				+ " SELECT finished_number as product_num,trade_name,specifications,measurement_unit,remark";
		String sql1 =" from finished_product";
		return Db.paginate(pageNumber, pageSize,sql,sql1);
	}
	
	/**
	 * @desc 显示要打印的条目
	 * @author xuhui
	 */
	public static List<Record> getConfromPrint(String products){
		String sql = "Select * FROM (SELECT semimanufactures_number as product_num"
				+ ",trade_name,specifications,measurement_unit,remark from"
				+ " semimanufactures UNION SELECT finished_number as product_num,trade_name"
				+ ",specifications,measurement_unit,remark from finished_product) a where product_num in("
				+ products+")";
		return	Db.find(sql);	
	}
	
	/**
	 * @desc 获取所有产品信息
	 * @author xuhui
	 */
	public static List<Record> getAllProduct(){
		String sql = "Select * FROM (SELECT semimanufactures_number as product_num"
				+ ",trade_name,specifications,measurement_unit,remark from semimanufactures"
				+ " UNION SELECT finished_number as product_num,trade_name,specifications"
				+ ",measurement_unit,remark from finished_product) a";
		return Db.find(sql);
	}
	
	/**
	 * @desc 根据产品编码，选取单个产品
	 * @author xuhui
	 */
	public static Record getSingleProduct(String product){
		String sql = "Select * FROM (SELECT semimanufactures_number as product_num,trade_name"
				+ ",specifications,measurement_unit,remark from semimanufactures UNION SELECT finished_number as product_num"
				+ ",trade_name,specifications,measurement_unit,remark from finished_product) a WHERE product_num='"+product+"'";
		return Db.findFirst(sql);
		
	}
}
