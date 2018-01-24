package model.response;

import java.util.ArrayList;

import model.data.ModelSimpleNode;

public class ResponseGetID extends Response{
	public ArrayList<ModelSimpleNode> nodes;
	
	
	public ResponseGetID(){
		nodes = new ArrayList<ModelSimpleNode>();
		
	}
	
}
