package net.etfbl.rest;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Linija;

@Path("/linije")
public class APIService {

	Service service;
	
	public APIService() {
		service = new Service();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Linija> getAll(){
		return service.getLinije();
	}
	
	@GET
	@Path("/{naziv}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByName(@PathParam("naziv") String naziv) {
		Linija linija = service.getByName(naziv);
		if(linija != null)
			return Response.status(200).entity(linija).build();
		else 
			return Response.status(404).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(Linija linija) {
		if(service.add(linija))
			return Response.status(200).entity(linija).build();
		else 
			return Response.status(500).entity("Greska pri dodavanje").build();
	}
	
	@PUT
	@Path("/{naziv}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response edit(@PathParam("naziv") String naziv, String stanicaPolazak) {
		if(service.update(naziv, stanicaPolazak))
			return Response.status(200).entity(naziv).build();
		else
			return Response.status(404).build();
	}
	
	@DELETE
	@Path("/{naziv}")
	public Response remove(@PathParam("naziv") String naziv) {
		if(service.remove(naziv))
			return Response.status(200).build();
		else 
			return Response.status(404).build();
	}
}






