package model.request;

public class ModelPutRequest extends ModelRequest {
	public int id;
	
	public ModelPutRequest() {
		
		
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		 return new StringBuffer(super.toString())
				    .append(" id: ").append(this.id).toString();

	}
}
