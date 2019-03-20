package za.co.discovery.transport.interstellar.service.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.java.Log;
import za.co.discovery.transport.interstellar.service.api.TransportService;
import za.co.discovery.transport.interstellar.service.helper.ValidationHelper;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

@Log
@Path("/")
@Api("Interstellar Transport Service")
public class TransportServiceRestApi {

	private static final String MESSAGE_HEADER_KEY = "Message";

	@Inject
	private TransportService transportService;

	@POST
	@ApiOperation("Create a new planet")
	@Path("/create/planet")
	public Response createPlanet(@ApiParam(value = "Node representing this planet", required = true) @QueryParam("node") String node,
	                             @ApiParam(value = "Name of the new planet", required = true) @QueryParam("name") String name) {
		if (ValidationHelper.isInvalidRequest(node, name)) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		try {
			return buildResponse(transportService.createPlanet(node, name), Response.Status.CREATED);
		} catch (Exception tse) {
			return buildErrorResponse(tse);
		}
	}

	@POST
	@ApiOperation("Create a new route between planets")
	@Path("/create/route")
	public Response createRoute(@ApiParam(value = "Node of the origin planet", required = true) @QueryParam("originNode") String originNode,
	                            @ApiParam(value = "Node of the destination planet", required = true) @QueryParam("destinationNode") String destinationNode,
	                            @ApiParam(value = "Distance between the planets", required = true) @QueryParam("distance") BigDecimal distance) {
		if (ValidationHelper.isInvalidRequest(originNode, destinationNode, distance)) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		try {
			return buildResponse(transportService.createRoute(originNode, destinationNode, distance), Response.Status.CREATED);
		} catch (Exception tse) {
			return buildErrorResponse(tse);
		}
	}

	@GET
	@ApiOperation("Find a planet by it's node")
	@Path("/find/planet/node/{node}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response findPlanetByNode(@ApiParam(value = "Node representing this planet", required = true) @PathParam("node") String node) {
		return buildResponseFromObject(transportService.findPlanetByNode(node));
	}

	@GET
	@ApiOperation("Find a planet by it's identifier")
	@Path("/find/planet/id/{planetId}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response findPlanetById(@ApiParam(value = "Unique ID representing this planet", required = true) @PathParam("planetId") Long planetId) {
		if ((planetId == null) || planetId <= 0) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		return buildResponseFromObject(transportService.findPlanetById(planetId));
	}

	@GET
	@ApiOperation("Find all planets configured in the transport system")
	@Path("/find/planet/all")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response findAllPlanets() {
		return buildResponseFromList(transportService.findAllPlanets());
	}

	@GET
	@ApiOperation("Find all configured routes in the transport system")
	@Path("/find/route/all")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response findAllRoutes() {
		return buildResponseFromList(transportService.findAllRoutes());
	}

	@GET
	@ApiOperation("Find all routes starting at the origin to the destination provided")
	@Path("/find/route")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response findRoute(@ApiParam(value = "Origin planet", required = true) @QueryParam("origin") String origin,
	                          @ApiParam(value = "Destination planet", required = true) @QueryParam("destination") String destination) {
		if (ValidationHelper.isInvalidRequest(origin, destination)) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		return buildResponseFromList(transportService.findRoute(origin, destination));
	}

	@PUT
	@ApiOperation("Update the name of a planet")
	@Path("/update/planet")
	public Response updatePlanetName(@ApiParam(value = "The node of the planet to update", required = true) @QueryParam("node") String node,
	                                 @ApiParam(value = "The new name of the planet", required = true) @QueryParam("newName") String newName) {
		if (ValidationHelper.isInvalidRequest(node, newName)) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		try {
			return buildResponse(transportService.updatePlanetName(node, newName), Response.Status.OK);
		} catch (Exception tse) {
			return buildErrorResponse(tse);
		}
	}

	@PUT
	@ApiOperation("Update the distance between planets")
	@Path("/update/route/{routeId}")
	public Response updateDistanceBetweenPlanets(@ApiParam(value = "The ID of the route in the table", required = true) @PathParam("routeId") Long routeId,
	                                             @ApiParam(value = "The new distance between the planets", required = true) @QueryParam("newDistance") BigDecimal newDistance) {
		if (ValidationHelper.isInvalidRequest(routeId, newDistance)) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		try {
			return buildResponse(transportService.updateRouteDistance(routeId, newDistance), Response.Status.OK);
		} catch (Exception tse) {
			return buildErrorResponse(tse);
		}
	}

	@DELETE
	@ApiOperation("Deletes a planet from a table")
	@Path("/delete/planet/{node}")
	public Response deletePlanetByNode(@ApiParam(value = "The node of the planet to delete", required = true) @PathParam("node") String node) {
		if (ValidationHelper.isInvalidRequest(node)) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		try {
			return buildResponse(transportService.deletePlanet(node), Response.Status.OK);
		} catch (Exception tse) {
			return buildErrorResponse(tse);
		}
	}

	@DELETE
	@ApiOperation("Deletes a route from the system")
	@Path("/delete/route/{routeId}")
	public Response deleteRouteById(@ApiParam(value = "The route ID of the route to delete", required = true) @PathParam("routeId") Long routeId) {
		if ((routeId == null) || routeId <= 0) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		try {
			return buildResponse(transportService.deleteRoute(routeId), Response.Status.OK);
		} catch (Exception tse) {
			return buildErrorResponse(tse);
		}
	}

	private Response buildErrorResponse(Exception tse) {
		return Response.serverError()
				.header(MESSAGE_HEADER_KEY, tse.getMessage())
				.build();
	}

	private Response buildResponseFromObject(Object entity) {
		if (entity != null) {
			return Response.ok(entity).build();
		} else {
			return Response.noContent().build();
		}
	}

	private Response buildResponseFromList(List entity) {
		if (entity != null && !entity.isEmpty()) {
			return Response.ok(entity).build();
		} else {
			return Response.noContent().build();
		}
	}

	private Response buildResponse(boolean result, Response.Status returnStatus) {
		if (result) {
			return Response.status(returnStatus).build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
}
