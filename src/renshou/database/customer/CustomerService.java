package renshou.database.customer;

import java.sql.SQLException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * @desc 公司管理
 * @author xuhui
 */
public class CustomerService {

	/**
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public static Page<Record> getCustomer(Integer pageNumber,Integer pageSize ,String company_name){
		String sql = "from company where 1=1";
		sql += " and company_name like '%"+company_name+"%'";
		return Db.paginate(pageNumber, pageSize,"select *",sql);
	}
	
	/**
	 * @desc 根据公司id，找到相应的公司
	 * @author xuhui
	 */
	public static Record getCustom(Integer id){
		return Db.findById("company", id);
	}
	
	/**
	 * @desc 根据公司id,找到相应的联系人
	 * @author xuhui
	 */
	public static List<Record> getContant(Integer id){
		String sql = "select * from company_contact where company_id = "+id;
		return Db.find(sql);
	}
	
	/**
	 * @author xuhui
	 * @desc 保存客户信息
	 */
	public static boolean saveCustoms(Integer id,String company_name,String company_address,String remark,String list){
		boolean flag = false;
		flag = Db.tx(new IAtom() {
			boolean r = false;
			@Override
			public boolean run() throws SQLException {
				List<JSONObject> jlist = (List<JSONObject>) JSONObject.parse(list);
				Record record = new Record();
				record.set("company_name", company_name);
				record.set("company_address", company_address);
				record.set("remark", remark);
				if(id!=null){
					record.set("id", id);
					 r =Db.update("company", record);
					 if(r){
						 Db.update("delete from company_contact where company_id = ?", id);
						 System.out.println(jlist.size());
						 for(JSONObject j:jlist){
							 if(j.getString("name")!=null){
								 Record record2 = new Record();
									record2.set("company_id", id);
									record2.set("name", j.getString("name"));
									record2.set("call_num", j.getString("call_num"));
									record2.set("QQ", j.getString("QQ"));
									record2.set("wechat",j.getString("wechat"));
									record2.set("e_mail", j.getString("e_mail"));
									record2.set("remark", j.getString("cremark"));
									Db.save("company_contact", record2);
								 
							 }									
						  } 	 
					 }
				}else{
					r = Db.save("company", record);
					if(r){
						String sql = "select id from company where company_name = '"+company_name+"'";
						Integer id = Db.findFirst(sql).get("id");
						for(JSONObject j:jlist){
							if(j.getString("name")!=null){
								Record record2 = new Record();
								record2.set("company_id", id);
								record2.set("name", j.getString("name"));
								record2.set("call_num", j.getString("call_num"));
								record2.set("QQ", j.getString("QQ"));
								record2.set("wechat",j.getString("wechat"));
								record2.set("e_mail", j.getString("e_mail"));
								record2.set("remark", j.getString("cremark"));
								Db.save("company_contact", record2);
							}
						}
					}
				}
				// TODO Auto-generated method stub
				return r;
			}
		});
		return flag;
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
					result =Db.deleteById("company", "id", id);
					Db.update("delete from company_contact where company_id = ?", id);	
					}
				return result;
			}
		});
		return flag;
	}
}
