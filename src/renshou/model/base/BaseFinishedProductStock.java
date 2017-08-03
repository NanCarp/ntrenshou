package renshou.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseFinishedProductStock<M extends BaseFinishedProductStock<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Integer id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public M setFinishedProductId(java.lang.Integer finishedProductId) {
		set("finished_product_id", finishedProductId);
		return (M)this;
	}

	public java.lang.Integer getFinishedProductId() {
		return get("finished_product_id");
	}

	public M setFinishedProductStockNum(java.math.BigDecimal finishedProductStockNum) {
		set("finished_product_stock_num", finishedProductStockNum);
		return (M)this;
	}

	public java.math.BigDecimal getFinishedProductStockNum() {
		return get("finished_product_stock_num");
	}

}
