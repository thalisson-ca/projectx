package rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


import model.request.ModelPutRequest;
import model.request.ModelRequest;
import model.response.ResponseGetAll;
import model.response.ResponseGetID;
import model.response.ResponseInsert;
import tree.Tree;

@Path("/node0") 
public class NodeREST{
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseInsert post(ModelRequest request) {
		Tree tree = new Tree();
		ResponseInsert response = tree.addNode(request);
		
		return response;
		
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseInsert put(ModelPutRequest request) {
		Tree tree = new Tree();
		ResponseInsert response = tree.editNode(request);
		
		return response;
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseGetAll getAll() {
		Tree tree = new Tree();
		ResponseGetAll response = tree.getTree();
		
		return response;
	}
	
	@GET
	@Path("/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseGetID getByParent(@PathParam("param") int id) {
		Tree tree = new Tree();
		ResponseGetID response = tree.getNodesByParentID(id);
		return response;
		
	}
	
	
}

