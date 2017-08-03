package renshou.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSemimanufacturesStockDetail<M extends BaseSemimanufacturesStockDetail<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Integer id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public M setWarehouseId(java.lang.Integer warehouseId) {
		set("warehouse_id", warehouseId);
		return (M)this;
	}

	public java.lang.Integer getWarehouseId() {
		return get("warehouse_id");
	}

	public M setSemimanufacturesId(java.lang.Integer semimanufacturesId) {
		set("semimanufactures_id", semimanufacturesId);
		return (M)this;
	}

	public java.lang.Integer getSemimanufacturesId() {
		return get("semimanufactures_id");
	}

	public M setNum(java.math.BigDecimal num) {
		set("num", num);
		return (M)this;
	}

	public java.math.BigDecimal getNum() {
		return get("num");
	}

}
