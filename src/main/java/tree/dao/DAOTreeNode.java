package tree.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import model.hibernate.TreeNode;
import tree.HibernateUtil;

public class DAOTreeNode {

	public static int insert(TreeNode node) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Object newId = session.save(node);
	    session.getTransaction().commit();
	    return Integer.parseInt(newId.toString());
	}
	
	public static int update(TreeNode node) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		session.saveOrUpdate(node);
	    session.getTransaction().commit();
		return node.getId();
	}
	
	public static ArrayList<TreeNode> select(String query,String[] paramName, Object[] value){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query queryObj = session.createQuery(query);
		for(int i=0;i<paramName.length;i++) {
			queryObj.setParameter(paramName[i], value[i]);
		}
		List list = queryObj.list();
		ArrayList<TreeNode> response = new ArrayList<TreeNode>();
		for(Object obj : list) {
			response.add((TreeNode)obj);
		}
		return response;
		
	}
	
}
