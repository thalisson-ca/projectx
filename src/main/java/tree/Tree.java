package tree;

import java.util.ArrayDeque;
import java.util.ArrayList;

import model.data.ModelArrayNode;
import model.data.ModelSimpleNode;
import model.hibernate.TreeNode;
import model.request.ModelPutRequest;
import model.request.ModelRequest;
import model.response.ResponseGetAll;
import model.response.ResponseGetID;
import model.response.ResponseInsert;
import tree.dao.DAOTreeNode;

public class Tree {
	
	public ResponseInsert addNode(ModelRequest request) {
		ResponseInsert response = new ResponseInsert();
		try {
			TreeNode treeNode = new TreeNode();
			treeNode.setCode(request.code);
			treeNode.setDetail(request.detail);
			treeNode.setParent(request.parentId);
			treeNode.setDescription(request.description);
			response.id =DAOTreeNode.insert(treeNode); 
			
		}catch(Exception e) {
			response.setError(e.toString());
		}
		
		return response;
	}
	
	public ResponseInsert editNode(ModelPutRequest request) {
		ResponseInsert response = new ResponseInsert();
		try {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(request.id);
			treeNode.setCode(request.code);
			treeNode.setDetail(request.detail);
			treeNode.setParent(request.parentId);
			treeNode.setDescription(request.description);
			response.id = DAOTreeNode.update(treeNode); 

			
		}catch(Exception e) {
			response.setError(e.toString());
		}
		
		return response;
	}
	
	public ResponseGetAll getTree() {
		ResponseGetAll response = new ResponseGetAll();
		try {
			
			
			String query = "from TreeNode where parent = :parentid";
			String[] paramName = new String[] {"parentid"};
			Object[] value = new Object[]{0};
			ArrayList<TreeNode> list = DAOTreeNode.select(query, paramName, value);
			if(!list.isEmpty()) {
				
				
				TreeNode node = list.get(0);
				ModelArrayNode model = new ModelArrayNode();
				model.id = node.getId();
				model.code = node.getCode();
				model.description = node.getDescription();
				model.detail = node.getDetail();
				model.parentId = node.getParent();
					
				query = "from TreeNode where parent = :parentid";
				paramName = new String[] {"parentid"};
				value = new Object[]{model.id};
				ArrayList<TreeNode> children = DAOTreeNode.select(query, paramName, value);
				for(TreeNode child:children) {
					model.children.add(getSubTree(child));
				}
				response.root = model;
			}
			
		}catch(Exception e) {
			response.setError(e.toString());
		}
		
		return response;
	}
	
	private ModelArrayNode getSubTree(TreeNode subRoot) {
		ModelArrayNode model = new ModelArrayNode();
		model.id = subRoot.getId();
		model.code = subRoot.getCode();
		model.description = subRoot.getDescription();
		model.detail = subRoot.getDetail();
		model.parentId = subRoot.getParent();
				
		String query = "from TreeNode where parent = :parentid";
		String[] paramName = new String[] {"parentid"};
		Object[] value = new Object[]{model.id};
		ArrayList<TreeNode> children = DAOTreeNode.select(query, paramName, value);
		for(TreeNode child:children) {
			model.children.add(getSubTree(child));
		}
		return model;
	}
	
	public ResponseGetID getNodesByParentID(int id) {
		ResponseGetID response = new ResponseGetID();
		try {
			String[] paramName = {"id"};
			Object[] value = {id};
			String query = "from TreeNode where id = :id";
			ArrayList<TreeNode> list = DAOTreeNode.select(query, paramName, value);
			if(!list.isEmpty()) {
				
				ArrayDeque<TreeNode> treeQueue = new ArrayDeque<TreeNode>();
				treeQueue.add(list.get(0));
				
				do {
					TreeNode node = treeQueue.pop();
					System.out.println(node.getId());
					ModelSimpleNode model = new ModelSimpleNode();
					model.id = node.getId();
					model.code = node.getCode();
					model.description = node.getDescription();
					model.detail = node.getDetail();
					model.parentId = node.getParent();
					
					query = "from TreeNode where parent = :parentid";
					paramName = new String[] {"parentid"};
					value = new Object[]{model.id};
					ArrayList<TreeNode> children = DAOTreeNode.select(query, paramName, value);
					model.hasChildren = !children.isEmpty();
					for(TreeNode child:children) {
						treeQueue.add(child);
					}
					response.nodes.add(model);
					
				}while(!treeQueue.isEmpty());
				
			}
			
		}catch(Exception e) {
			response.setError(e.toString());
		}
				
		return response;
	
	}
	
}

