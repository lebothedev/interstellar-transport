package za.co.discovery.transport.interstellar.service.impl;

import lombok.extern.java.Log;
import za.co.discovery.transport.interstellar.domain.entity.Planet;
import za.co.discovery.transport.interstellar.domain.entity.Route;
import za.co.discovery.transport.interstellar.service.api.TransportService;
import za.co.discovery.transport.interstellar.service.TransportDBService;
import za.co.discovery.transport.interstellar.service.api.dto.PlanetResponse;
import za.co.discovery.transport.interstellar.service.api.dto.RouteResponse;
import za.co.discovery.transport.interstellar.service.api.xsd.PlanetHop;
import za.co.discovery.transport.interstellar.service.api.xsd.RouteResponseDocument;
import za.co.discovery.transport.interstellar.service.galaxy.Galaxy;
import za.co.discovery.transport.interstellar.service.helper.OptimalRouteCalculator;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Log
@Stateless
public class TransportServiceImpl implements TransportService {

	@EJB
	private TransportDBService transportDBService;

	public RouteResponseDocument determineShortestPath(String origin, String destination) {
		RouteResponseDocument result = new RouteResponseDocument();

		List<RouteResponse> routeList = findAllRoutes();
		List<PlanetResponse> planetList = findAllPlanets();

		Galaxy galaxy = new Galaxy(planetList, routeList);

		OptimalRouteCalculator calculator = new OptimalRouteCalculator(galaxy);
		calculator.begin(findPlanetByNode(origin));
		LinkedList<PlanetResponse> optimalPath = calculator.getPath(findPlanetByNode(destination));

		if (optimalPath != null) {
			for (PlanetResponse p : optimalPath) {
				PlanetHop hop = new PlanetHop();
				hop.setNode(p.getNode());
				hop.setName(p.getName());
				result.getHop().add(hop);
			}
		}

		return result;
	}

	public PlanetResponse findPlanetByNode(String node) {
		Planet planet = transportDBService.findPlanetByNode(node);
		if (planet != null) {
			return new PlanetResponse(planet.getNode(), planet.getName());
		}
		return null;
	}

	public PlanetResponse findPlanetById(Long planetId) {
		Planet planet = transportDBService.findPlanetById(planetId);
		if (planet != null) {
			return new PlanetResponse(planet.getNode(), planet.getName());
		}
		return null;
	}

	public List<RouteResponse> findRoute(String originNode, String destinationNode) {
		return getRouteListFromRouteObject(transportDBService.findRoute(originNode, destinationNode));
	}

	public List<PlanetResponse> findAllPlanets() {
		return getPlanetListFromPlanetObject(transportDBService.findAllPlanets());
	}

	public List<RouteResponse> findAllRoutes() {
		return getRouteListFromRouteObject(transportDBService.findAllRoutes());
	}

	public boolean createPlanet(String node, String name) {
		return transportDBService.createPlanet(node, name);
	}

	public boolean createRoute(String originNode, String destinationNode, BigDecimal distance) {
		return transportDBService.createRoute(originNode, destinationNode, distance);
	}

	public boolean updatePlanetName(String node, String newName) {
		return transportDBService.updatePlanet(node, newName);
	}

	public boolean updateRouteDistance(Long routeId, BigDecimal newDistance) {
		return transportDBService.updateRoute(routeId, newDistance);
	}

	public boolean deletePlanet(String node) {
		return transportDBService.deletePlanet(node);
	}

	public boolean deleteRoute(Long routeId) {
		return transportDBService.deleteRoute(routeId);
	}

	/**
	 * Converts from the entity object to another object to loosen coupling between system objects
	 *
	 * @param routes - List of routes retrieved from the database
	 * @return
	 */
	private List<RouteResponse> getRouteListFromRouteObject(List<Route> routes) {
		List<RouteResponse> response = new ArrayList<RouteResponse>();
		if (routes != null && !routes.isEmpty()) {
			for (Route route : routes) {
				response.add(buildRouteResponse(route));
			}
		}
		return response;
	}

	/**
	 * Converts from the entity object to another object to loosen coupling between system objects
	 *
	 * @param planets
	 * @return
	 */
	private List<PlanetResponse> getPlanetListFromPlanetObject(List<Planet> planets) {
		List<PlanetResponse> response = new ArrayList<PlanetResponse>();
		if (planets != null && !planets.isEmpty()) {
			for (Planet planet : planets) {
				response.add(buildPlanetResponse(planet.getId(), planet.getNode(), planet.getName()));
			}
		}
		return response;
	}

	private RouteResponse buildRouteResponse(Route route) {
		RouteResponse response = new RouteResponse();
		response.setOrigin(buildPlanetResponse(route.getOrigin().getId(), route.getOrigin().getNode(), route.getOrigin().getName()));
		response.setDestination(buildPlanetResponse(route.getDestination().getId(), route.getDestination().getNode(), route.getDestination().getName()));
		response.setDistance(route.getDistance());
		return response;
	}

	private PlanetResponse buildPlanetResponse(Long id, String node, String name) {
		PlanetResponse planetResponse = new PlanetResponse(node, name);
		planetResponse.setPlanetId(id);
		return planetResponse;
	}
}
