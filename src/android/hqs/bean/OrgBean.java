package android.hqs.bean;

import java.io.Serializable;

public class OrgBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int _id;
	private int parentId;
	private String name;
	
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
