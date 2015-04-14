package com.ig.ws;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("JMSService")
public class RestServices {

	// @GET here defines, this method will method will process HTTP GET
	// requests.
	@GET
	// @Path here defines method level path. Identifies the URI path that a
	// resource class method will serve requests for.
	@Path("/push/{i}/{j}")
	// @Produces here defines the media type(s) that the methods
	// of a resource class can produce.
	@Produces(MediaType.TEXT_PLAIN)
	// @PathParam injects the value of URI parameter that defined in @Path
	// expression, into the method.
	public String push(@PathParam("i") int i, @PathParam("j") int j) {
		
		try{
		QueueOperations queue = new QueueOperations();
		queue.pushToQueue(i, j);
		}catch(Exception ex){
			return "Error in pushing to queue.";
		}

	return "Numbers pushed to queue successfully";
	}




	@GET 
	@Path("/list/") 
	@Produces(MediaType.TEXT_PLAIN)
	public String list() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(34);
		list.add(23453);
		list.add(435);
		list.add(656);
		
		try{
			QueueOperations queue = new QueueOperations();
			queue.getList();
			}catch(Exception ex){
				return "Error in pushing to queue.";
			}

	return list.toString();
	}

}
