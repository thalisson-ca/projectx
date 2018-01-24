package model.response;

public abstract class Response {
	public boolean hasError;
	public String error;
	public boolean hasWarning;
	public String warning;
	
	public void setError(String error) {
		hasError = true;
		this.error = error;
	}
}
