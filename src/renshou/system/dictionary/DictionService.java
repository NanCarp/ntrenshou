package renshou.system.dictionary;

import java.sql.SQLException;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
/**
 * @ClassName: DictionService
 * @Description: 系统管理_基础数据管理
 * @author: xuhui
 * @date: 2017年5月15日下午2:00:00
 * @version: 1.0 版本初成
 */
public class DictionService {
	// 基础数据列表
		public static List<Record> getDictionaryList() {
			String sql = "SELECT * FROM t_dictionary ";
			return Db.find(sql);
		}

		// 删除基础数据
		public static boolean deleteDictionary(Integer id) {
			return Db.deleteById("t_dictionary", id);
		}
		
		public static Page<Record> getDictionaryPages(int pageNumber,int pageSize,String keyword,String key){
			String sql = "from t_dictionary where 1=1";
			if(keyword!=null&&keyword!=""){
				sql += " and keyword Like '%" +keyword+"%'";
			}
			if(key!=null&&key!=""){
				sql +=" and `key` Like ='%"+key+"%'";
			}
			return Db.paginate(pageNumber, pageSize, "select *", sql);
			
		}


        /** 
        * @Title: isDuplicate 
        * @Description: 检测重复
        * @param key
        * @param value
        * @return boolean
        * @author liyu
        */
        public static boolean isDuplicate(String key, String value, String keyword) {
            return Db.find("SELECT * FROM t_dictionary WHERE `key` = ? AND (value= ? OR keyword = ?)", 
                    key, value, keyword).size() > 0;
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
						result = Db.deleteById("t_dictionary", "id", id);		
						}
					return result;
				}
			});
			return flag;
		}

}
