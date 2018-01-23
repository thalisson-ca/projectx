package model.request;

public class ModelRequest {
	public String code;
	public String description;
	public int parentId;
	public String detail;
	
	public ModelRequest() {
		
	}
	
	public void setCode(String cod) {
		code=cod;
	}
	
	public void setDescription(String desc) {
		description=desc;
	}
	
	public void setDetail(String detail) {
		this.detail=detail;
	}
	
	public void setParentId(int parent) {
		parentId=parent;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getDetail() {
		return detail;
	}
	
	public int getParentId() {
		return parentId;
	}
	
	@Override
	public String toString() {
		 return new StringBuffer("Code: ").append(this.code)
				    .append(" description: ").append(this.description)
				    .append(" detail: ").append(this.detail)
				    .append(" parent: ").append(this.parentId).toString();

	}
}
