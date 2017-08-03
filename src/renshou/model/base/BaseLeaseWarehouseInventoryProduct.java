package renshou.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseLeaseWarehouseInventoryProduct<M extends BaseLeaseWarehouseInventoryProduct<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Integer id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public M setInventoryId(java.lang.Integer inventoryId) {
		set("inventory_id", inventoryId);
		return (M)this;
	}

	public java.lang.Integer getInventoryId() {
		return get("inventory_id");
	}

	public M setProductId(java.lang.Integer productId) {
		set("product_id", productId);
		return (M)this;
	}

	public java.lang.Integer getProductId() {
		return get("product_id");
	}

	public M setQuantity(java.math.BigDecimal quantity) {
		set("quantity", quantity);
		return (M)this;
	}

	public java.math.BigDecimal getQuantity() {
		return get("quantity");
	}

}
