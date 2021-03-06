package com.huhuo.integration.db.mysql;
/**
 * 用于分组操作
 * @author kevin
 */
public class Group {

	/**
	 * 排序字段
	 */
	protected String field;
	/**
	 * 升降序
	 */
	private Dir dir;
	
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Dir getDir() {
		return dir;
	}
	
	public void setDir(Dir dir) {
		this.dir = dir;
	}

	public Group(String field, Dir dir) {
		super();
		this.field = field;
		this.dir = dir;
	}
	
}
