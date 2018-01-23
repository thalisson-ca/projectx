package model.data;

import java.util.ArrayList;

public class ModelArrayNode extends ModelNode{
	public ArrayList<ModelArrayNode> children;
	
	public ModelArrayNode() {
		children = new ArrayList<ModelArrayNode>();
	}
	
}
