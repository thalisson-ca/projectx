package rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


import model.request.ModelPutRequest;
import model.request.ModelRequest;
import model.response.ResponseDelete;
import model.response.ResponseGetAll;
import model.response.ResponseGetID;
import model.response.ResponseInsert;
import tree.TreeV2;

@Path("/node") 
public class NodeRESTV2{
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseInsert post(ModelRequest request) {
		TreeV2 tree = new TreeV2();
		ResponseInsert response = tree.addNode(request);
		
		return response;
		
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseInsert put(ModelPutRequest request) {
		TreeV2 tree = new TreeV2();
		ResponseInsert response = tree.editNode(request);
		
		return response;
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseGetAll getAll() {
		TreeV2 tree = new TreeV2();
		ResponseGetAll response = tree.getTree();
		
		return response;
	}
	
	@GET
	@Path("/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseGetID getByParent(@PathParam("param") int id) {
		TreeV2 tree = new TreeV2();
		ResponseGetID response = tree.getNodesByParentID(id);
		return response;
		
	}
	
	@DELETE
	@Path("/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDelete deleteID(@PathParam("param") int id) {
		TreeV2 tree = new TreeV2();
		ResponseDelete response = tree.deleteWithId(id);
		
		return response;
	}
	
	
}

