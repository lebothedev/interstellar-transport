package za.co.discovery.transport.interstellar.service.impl;

import lombok.extern.java.Log;
import za.co.discovery.transport.interstellar.domain.dao.TransportDaoService;
import za.co.discovery.transport.interstellar.domain.entity.Planet;
import za.co.discovery.transport.interstellar.domain.entity.Route;
import za.co.discovery.transport.interstellar.service.TransportDBService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.math.BigDecimal;
import java.util.List;

@Log
@Stateless
public class TransportDBServiceImpl implements TransportDBService {

	@EJB
	private TransportDaoService transportDaoService;

	public List<Planet> findAllPlanets() {
		return transportDaoService.findAllPlanets();
	}

	public Planet findPlanetByNode(String node) {
		return transportDaoService.findPlanetUsingNode(node);
	}

	public Planet findPlanetById(Long planetId) {
		return transportDaoService.findPlanetById(planetId);
	}

	public List<Route> findRoute(String origin, String destination) {
		return transportDaoService.findRoute(origin, destination);
	}

	public List<Route> findAllRoutes() {
		return transportDaoService.findAllRoutes();
	}

	public boolean createPlanet(String node, String planetName) {
		Planet newPlanet = new Planet(node, planetName);
		return transportDaoService.createPlanet(newPlanet);
	}

	public boolean createRoute(String originNode, String destinationNode, BigDecimal distance) {
		Planet originPlanet = transportDaoService.findPlanetUsingNode(originNode);
		Planet destinationPlanet = transportDaoService.findPlanetUsingNode(destinationNode);
		if (originPlanet != null && destinationPlanet != null) {
			Route newRoute = new Route(originPlanet, destinationPlanet, distance);
			return transportDaoService.createRoute(newRoute);
		} else {
			return false;
		}
	}

	public boolean updatePlanet(String node, String newName) {
		Planet planetToUpdate = findPlanetByNode(node);
		if (planetToUpdate != null) {
			planetToUpdate.setName(newName);
			return transportDaoService.updatePlanet(planetToUpdate);
		}
		return false;
	}

	public boolean updateRoute(Long routeId, BigDecimal newDistance) {
		Route routeToUpdate = transportDaoService.findRouteById(routeId);
		if (routeToUpdate != null) {
			routeToUpdate.setDistance(newDistance);
			return transportDaoService.updateRoute(routeToUpdate);
		}
		return false;
	}

	public boolean deleteRoute(Long routeId) {
		return transportDaoService.deleteRoute(routeId);
	}

	public boolean deletePlanet(String planetNode) {
		return transportDaoService.deletePlanet(planetNode);
	}
}
