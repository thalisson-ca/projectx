package tree.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import model.hibernate.TreeNodeV2;
import tree.HibernateUtil;

public class DAOTreeNodeV2 {

	
	public static void updateLeftRight(Session session, int locValue, int delta) {
		System.out.println("locValue: "+locValue);
		System.out.println("delta: "+delta);
		String query = "update TreeNodeV2 set rgt=rgt+:delta where rgt>=:locValue";
		Query queryObj=session.createQuery(query);
		queryObj.setParameter("delta",delta);
		queryObj.setParameter("locValue",locValue);
		queryObj.executeUpdate();
					
		query = "update TreeNodeV2 set lft=lft+:delta where lft>=:locValue";
		queryObj=session.createQuery(query);
		queryObj.setParameter("delta",delta);
		queryObj.setParameter("locValue",locValue);
		queryObj.executeUpdate();
	}
	
	public static TreeNodeV2 queryByID(int id, Session session) {
		String query = "from TreeNodeV2 where id=:id";
		String[] paramName = {"id"};
		Object[] value = {id};
		ArrayList<TreeNodeV2> nodes = DAOTreeNodeV2.select(query, paramName, value, session);
		if(nodes.isEmpty()) {
			return null;
		}else {
			return nodes.get(0);
		}
		
	}
	
	public static int simpleUpdate(TreeNodeV2 node,Session session ) {
		session.saveOrUpdate(node);
	    return node.getId();
	}
	
	public static int updateWithQuery(String query,String[] paramName, Object[] value) {
		
		
		
		return 0;
	}
	
	public static ArrayList<TreeNodeV2> select(String query,String[] paramName, Object[] value,Session session){
		
		Query queryObj = session.createQuery(query);
		for(int i=0;i<paramName.length;i++) {
			queryObj.setParameter(paramName[i], value[i]);
		}
		List list = queryObj.list();
		ArrayList<TreeNodeV2> response = new ArrayList<TreeNodeV2>();
		for(Object obj : list) {
			response.add((TreeNodeV2)obj);
		}
		return response;
		
	}
	
public static int deleteOrUpdate(String query,String[] paramName, Object[] value,Session session){
		
		Query queryObj = session.createQuery(query);
		for(int i=0;i<paramName.length;i++) {
			queryObj.setParameter(paramName[i], value[i]);
		}
		
		return queryObj.executeUpdate();
		
	}
	
	
}
