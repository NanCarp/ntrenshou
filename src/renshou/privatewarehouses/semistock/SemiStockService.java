package renshou.privatewarehouses.semistock;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.omg.PortableInterceptor.INACTIVE;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import renshou.utils.EncodeUtil;

/**
 * @desc 半成品库存
 * @author xuhui
 */
public class SemiStockService {
	
	/**
	 * @desc 展示半成品库存基本数据
	 * @param pageNumber
	 * @param pageSize
	 * @return Page<Record>
	 * @author xuhui
	 */

	public static Page<Record> getStock(Integer pageNumber,Integer pageSize,String semimanufactures_number,String trade_name){
		String presql = "SELECT s.id,s.semimanufactures_id,s.semimanufactures_stock_num"
				+ ",m.semimanufactures_number,m.trade_name,m.specifications,m.measurement_unit";
		String sql =" from semimanufactures_stock s LEFT JOIN semimanufactures m ON s.semimanufactures_id = m.id where 1=1";
		if(semimanufactures_number!=null&&semimanufactures_number!=""){
			sql += " and semimanufactures_number like '%"+semimanufactures_number+"%'";
		}
		if(trade_name!=null&&trade_name!=""){
			sql +=" and trade_name like '%"+trade_name+"%'";
		}
		sql +=" order by id desc";
		return Db.paginate(pageNumber, pageSize, presql,sql);
	}
	
	/**
	 * @desc 根据id查询信息
	 * @author xuhui
	 */
	public static Record getStockById(Integer id){
		String sql = "SELECT s.id,s.semimanufactures_id,s.semimanufactures_stock_num,m.semimanufactures_number,m.trade_name"
				+ ",m.specifications,m.measurement_unit from semimanufactures_stock s LEFT JOIN semimanufactures m ON s.semimanufactures_id = m.id"
				+ " where s.id = "+id;
		return Db.findFirst(sql);
	}
	
	/**
	 * @desc 根据id查询产品在库存中的位置
	 * @author xuhui
	 */
	public static List<Record> getStockDetailById(Integer SemiproductId){
		String sql ="SELECT s.id,s.num,s.semimanufactures_id,s.warehouse_id,w.warehouse_name as h3,a.warehouse_name as h2,r.warehouse_name as h1 FROM"
				+ " semimanufactures_stock_detail s LEFT JOIN warehouse w ON s.warehouse_id = w.id LEFT JOIN warehouse a ON w.pid = a.id LEFT JOIN warehouse"
				+ " r ON r.id = a.pid where s.semimanufactures_id ="+SemiproductId;
		return Db.find(sql);
		
	}
	
	/**
	 * @desc 根据库存id查询半成品id
	 * @author xuhui
	 */
	public static Record getSeimId(Integer id){
		return Db.findById("semimanufactures_stock", id);
	}
	
	/**
	 * @desc 根据id批量删除操作
	 * @author xuhui
	 */
	public static boolean delete(String ids){
		String[] allid = ids.split(",");	
		boolean flag = Db.tx(new IAtom() {
			boolean result = true;
			@Override
			public boolean run() throws SQLException {
				// TODO Auto-generated method stub
				for(String id:allid){
					Record record =SemiStockService.getSeimId(Integer.parseInt(id));
					System.out.println(Integer.parseInt(id));
					System.out.println(record);
					if(record!=null){
						Integer semimanufactures_id =record.getInt("semimanufactures_id");
						Db.update("DELETE FROM semimanufactures_stock_detail WHERE semimanufactures_id ="+semimanufactures_id);
					}
					result = Db.deleteById("semimanufactures_stock", "id", id);	
				}
				return result;
			}
		});
		return flag;
	}
	
	/**
	 * @desc 找到最子级仓库
	 * @author xuhui
	 */
	public static List<Record> getSublevel(){
		String sql ="SELECT w.id,w.warehouse_name,w.pid,a.id as aid,a.warehouse_name as awarehouse_name,r.id as rid"
				+ ",r.warehouse_name as rwarehouse_name FROM warehouse w LEFT JOIN warehouse a ON w.pid = a.id LEFT JOIN"
				+ " warehouse r ON r.id = a.pid where r.id IS NOT NULL";
		return Db.find(sql);
	}
	
	/**
	 * @desc 筛选出尚未添加到库存里的数据
	 * @author xuhui
	 */
	public static List<Record> getNoRepeat(){
		String sql ="SELECT id,semimanufactures_number,trade_name,specifications,measurement_unit from semimanufactures where id"
				+ " not IN (SELECT semimanufactures_id from semimanufactures_stock)";
		return Db.find(sql);
	}
	
	/**
	 * @desc 根据半成品id查询半成品信息
	 * @author xuhui
	 */
	public static Record getMeimes(Integer id){
		return Db.findById("semimanufactures", id);
	}
	
	/**
	 * @desc 保存数据
	 * @author xuhui
	 */
	public static boolean saveStrock(Integer id,Integer semimanufactures_id,String list,String semimanufactures_id_num){
		boolean flag = false;
		flag = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				// TODO Auto-generated method stub
				Record record = new Record();
				record.set("semimanufactures_id", semimanufactures_id);
				record.set("semimanufactures_stock_num", semimanufactures_id_num);
				if(id!=null){					
					record.set("id", id);					
					Db.update("semimanufactures_stock", record);
				}else{
					Db.save("semimanufactures_stock", record);
				}
				List<JSONObject> jsonObjS = (List<JSONObject>) JSONObject.parse(list);
				String sql = "delete from semimanufactures_stock_detail where semimanufactures_id ="+semimanufactures_id;
				Db.update(sql);
				for(JSONObject s:jsonObjS){
					Record r = new Record();
					r.set("semimanufactures_id", semimanufactures_id);
					r.set("warehouse_id",s.getInteger("warehouse_id"));
					r.set("num", s.getDouble("warehouse_id_num"));
					Db.save("semimanufactures_stock_detail", r);
				}
				return true;
			}
		});
		return true;
	}
	
	/**
	 * @desc 统计库存明细表GROUP BY半成品id的所有数据
	 * @author xuhui
	 */
	public static List<Record> getSemiStocklistBySemiid(){
		String sql = "select * from(SELECT semimanufactures_id,SUM(num) as semimanufactures_stock_num FROM"
				+ " semimanufactures_stock_detail GROUP BY semimanufactures_id) s";
		return Db.find(sql);
	}
	
	/**
	 * @param response
	 * @param sdate
	 * @param rectification
	 * @param company_name
	 * @return boolean result
	 * @desc 导出表格
	 */
	public static boolean getExcel(HttpServletResponse response){
		HSSFWorkbook wbook = new HSSFWorkbook();//创建一个workbook对应一个excel
		HSSFSheet sheet = wbook.createSheet();//创建一个sheet
		wbook.setSheetName(0, "半成品仓库导入表格", (short)1);
		//sheet.addMergedRegion(new Region(0, (short)0 , 0, (short)11));
		
		//设置列宽
		sheet.setColumnWidth((short)0, (short)2000);//序号
		sheet.setColumnWidth((short)1, (short)5000);//产品编码
		sheet.setColumnWidth((short)2, (short)5000);//对应仓库
		sheet.setColumnWidth((short)3, (short)5000);//数量


        HSSFCellStyle cellStyle = wbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFFont font = wbook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 20);//设置字体大小    
        cellStyle.setFont(font);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框 
        
        HSSFCellStyle cellBorder = wbook.createCellStyle();
        cellBorder.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellBorder.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
        cellBorder.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
        cellBorder.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
        cellBorder.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框 
               
        HSSFRow row;
		row = sheet.createRow(0);
		HSSFCell cell0 = row.createCell((short)0);
		cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell0.setCellStyle(cellBorder);
		cell0.setCellValue("序号");

		row = sheet.createRow(0);
		HSSFCell cell1 = row.createCell((short)1);
		cell1.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell1.setCellStyle(cellBorder);
		cell1.setCellValue("产品编码");
		
		row = sheet.createRow(0);
		HSSFCell cell2 = row.createCell((short)2);
		cell2.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell2.setCellStyle(cellBorder);
		cell2.setCellValue("对应仓库");
		
		row = sheet.createRow(0);
		HSSFCell cell3 = row.createCell((short)3);
		cell3.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell3.setCellStyle(cellBorder);
		cell3.setCellValue("数量");

		response.addHeader("Content-Disposition", "attachment;filename=" + EncodeUtil.toUtf8String("半成品仓库导入")+ ".xls");
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		try {
			ServletOutputStream out = response.getOutputStream();
			wbook.write(out);
			out.flush();
			out.close();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
