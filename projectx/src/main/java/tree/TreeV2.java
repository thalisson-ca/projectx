package tree;

import java.util.ArrayList;
import java.util.HashMap;

import org.hibernate.Session;
import model.data.ModelArrayNode;
import model.data.ModelSimpleNode;
import model.hibernate.TreeNodeV2;
import model.request.ModelPutRequest;
import model.request.ModelRequest;
import model.response.ResponseDelete;
import model.response.ResponseGetAll;
import model.response.ResponseGetID;
import model.response.ResponseInsert;
import tree.dao.DAOTreeNodeV2;


public class TreeV2 {
	public ResponseInsert addNode(ModelRequest request) {
		ResponseInsert response = new ResponseInsert();
		TreeNodeV2 node = new TreeNodeV2();
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			session.beginTransaction();	
			String query = "from TreeNodeV2 where id=:idParent";
			String[] paramName = {"idParent"};
			Object[] value = {request.parentId};
			ArrayList<TreeNodeV2> root = DAOTreeNodeV2.select(query, paramName, value,session);
			
			if(!root.isEmpty()) {
				int rootR = root.get(0).getRgt();
				
				node.setCode(request.code);
				node.setDescription(request.description);
				node.setDetail(request.detail);
				node.setParent(request.parentId);
				node.setLft(rootR);
				node.setRgt(rootR+1);
				DAOTreeNodeV2.updateLeftRight(session, rootR,2);
				
			}else {
				query = "from TreeNodeV2";
				paramName =new String[]{};
				value =  new Object[]{};
				root = DAOTreeNodeV2.select(query, paramName, value,session);
				
				
				if(request.parentId==0 && root.isEmpty()) {
				
					node.setCode(request.code);
					node.setDescription(request.description);
					node.setDetail(request.detail);
					node.setParent(request.parentId);
					node.setLft(1);
					node.setRgt(2);
				}else {
					throw new Exception("Invalid parent node.");
					
				}
			}
			Object newId = session.save(node);
			response.id =  Integer.parseInt(newId.toString());
			session.getTransaction().commit();
			
		}catch(Exception e) {
			session.getTransaction().rollback();
			response.setError(e.toString());
		}
		session.close();
		return response;
	}
	
	
	public ResponseInsert editNode(ModelPutRequest request) {
		ResponseInsert response = new ResponseInsert();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			
			session.beginTransaction();	
			TreeNodeV2 node = DAOTreeNodeV2.queryByID(request.id, session);
			if(node!=null) {
				if(node.getParent()==request.parentId) {
					TreeNodeV2 treeNode = node;
					treeNode.setCode(request.code);
					treeNode.setDetail(request.detail);
					treeNode.setDescription(request.description);
					response.id = DAOTreeNodeV2.simpleUpdate(treeNode,session);
					
				}else {
					ArrayList<TreeNodeV2> childrenNodes = new ArrayList<TreeNodeV2>();
					TreeNodeV2 newParent = DAOTreeNodeV2.queryByID(request.parentId, session);
					
					if(newParent==null) {
						throw new Exception("Invalid parent node.");
					}
					
					int lft = node.getLft();
					int rgt = node.getRgt();
					String query = "from TreeNodeV2 where lft>:nodeLft and rgt<:nodeRgt";
					String[] paramName = {"nodeLft","nodeRgt"};
					Object[] value = {lft,rgt};
					childrenNodes.addAll(DAOTreeNodeV2.select(query, paramName, value, session));
					int delta = rgt-lft+1;
					
					DAOTreeNodeV2.updateLeftRight(session, rgt+1,-delta);
					int loc;
					if(lft>newParent.getRgt()) {
						loc = newParent.getRgt();
					}else {
						loc = newParent.getRgt()-delta;
					}
					
					DAOTreeNodeV2.updateLeftRight(session, loc,delta);
					
					int innerDelta = loc - node.getLft();
					
					node.setCode(request.code);
					node.setDetail(request.detail);
					node.setDescription(request.description);
					node.setParent(request.parentId);
					node.setLft(lft+innerDelta);
					node.setRgt(rgt+innerDelta);
					session.saveOrUpdate(node);
					
					for(TreeNodeV2 child:childrenNodes) {
						child.setLft(child.getLft()+innerDelta);
						child.setRgt(child.getRgt()+innerDelta);
						session.saveOrUpdate(child);
					}
					
				}
				response.id = node.getId();
				session.getTransaction().commit();
			}else {
				
				throw new Exception("Node "+request.id+" doesn't exist.");
			}
						
		}catch(Exception e) {
			session.getTransaction().rollback();
			response.setError(e.toString());
		}
		session.close();
		return response;
	}
	
	public ResponseGetAll getTree() {
		ResponseGetAll response = new ResponseGetAll();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String query = "from TreeNodeV2 order by lft";
			String[] paramName = {};
			Object[] value = {};
			ArrayList<TreeNodeV2> nodes = DAOTreeNodeV2.select(query, paramName, value,session);
			if(nodes.isEmpty()) {
				response.setError("There is no node.");
			}else {
				HashMap<Integer, ModelArrayNode> map = new HashMap<Integer, ModelArrayNode>();
				for(TreeNodeV2 node :nodes) {
					ModelArrayNode model = new ModelArrayNode();
					model.code = node.getCode();
					model.description = node.getDescription();
					model.detail = node.getDetail();
					model.id = node.getId();
					map.put(model.id, model);
					
					int idParent =node.getParent(); 
					if(idParent!=0) {
						ModelArrayNode parent = map.get(idParent);
						parent.children.add(model);
					}else {
						response.root = model;
					}
					
				}
				
			} 
			
		}catch(Exception e) {
			response.setError(e.toString());
		}
		session.close();
		return response;
	}
	
	public ResponseGetID getNodesByParentID(int id) {
		ResponseGetID response = new ResponseGetID();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String query = "from TreeNodeV2 where id=:id";
			String[] paramName = {"id"};
			Object[] value = {id};
			ArrayList<TreeNodeV2> rootArray = DAOTreeNodeV2.select(query, paramName, value,session);
			if(!rootArray.isEmpty()) {
				TreeNodeV2 root = rootArray.get(0);
				query = "from TreeNodeV2 where lft>:lftP and rgt<:rgtP order by lft";
				paramName = new String[]{"lftP","rgtP"};
				value = new Object[]{root.getLft(),root.getRgt()};
				rootArray.addAll(DAOTreeNodeV2.select(query, paramName, value,session));
				
				for(TreeNodeV2 node : rootArray) {
					ModelSimpleNode model = new ModelSimpleNode();
					model.code = node.getCode();
					model.description = node.getDescription();
					model.detail = node.getDetail();
					model.id = node.getId();
					model.parentId = node.getParent();
					model.hasChildren = (node.getRgt()-node.getLft())>1;
					response.nodes.add(model);
				}
				
			}else {
				response.setError("The node "+id+" doesn't exist.");
			}
			
			
		}catch(Exception e) {
			response.setError(e.toString());
		}
		session.close();
		return response;
	
	}
	
	public ResponseDelete deleteWithId(int id) {
		ResponseDelete response = new ResponseDelete();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();	
			int qtd = 0;
			TreeNodeV2 node = DAOTreeNodeV2.queryByID(id, session);
			if(node!=null) {
				
				int lft = node.getLft();
				int rgt = node.getRgt();
				String query = "delete TreeNodeV2 where lft>=:nodeLft and rgt<=:nodeRgt";
				String[] paramName = {"nodeLft","nodeRgt"};
				Object[] value = {lft,rgt};
				qtd =DAOTreeNodeV2.deleteOrUpdate(query, paramName, value, session);
				int delta = rgt-lft+1;
				DAOTreeNodeV2.updateLeftRight(session, rgt+1,-delta);
				
			}
			response.result = qtd+" nodes deleted";
			session.getTransaction().commit();
		}catch(Exception e) {
			session.getTransaction().rollback();
			response.setError(e.toString());
		}
		session.close();
		return response;
	}
}
