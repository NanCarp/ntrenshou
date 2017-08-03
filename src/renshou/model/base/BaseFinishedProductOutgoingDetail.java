package renshou.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseFinishedProductOutgoingDetail<M extends BaseFinishedProductOutgoingDetail<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Integer id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public M setFinishedProductOutgoingId(java.lang.Integer finishedProductOutgoingId) {
		set("finished_product_outgoing_id", finishedProductOutgoingId);
		return (M)this;
	}

	public java.lang.Integer getFinishedProductOutgoingId() {
		return get("finished_product_outgoing_id");
	}

	public M setFinishedProductId(java.lang.Integer finishedProductId) {
		set("finished_product_id", finishedProductId);
		return (M)this;
	}

	public java.lang.Integer getFinishedProductId() {
		return get("finished_product_id");
	}

	public M setNum(java.math.BigDecimal num) {
		set("num", num);
		return (M)this;
	}

	public java.math.BigDecimal getNum() {
		return get("num");
	}

	public M setWarehouseId(java.lang.Integer warehouseId) {
		set("warehouse_id", warehouseId);
		return (M)this;
	}

	public java.lang.Integer getWarehouseId() {
		return get("warehouse_id");
	}

}