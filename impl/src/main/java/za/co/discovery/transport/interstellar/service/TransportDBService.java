package za.co.discovery.transport.interstellar.service;


import za.co.discovery.transport.interstellar.domain.entity.Planet;
import za.co.discovery.transport.interstellar.domain.entity.Route;

import java.math.BigDecimal;
import java.util.List;

public interface TransportDBService {

	List<Planet> findAllPlanets();
	List<Route> findAllRoutes();

	Planet findPlanetByNode(String node);
	Planet findPlanetById(Long planetId);
	List<Route> findRoute(String origin, String destination);

	boolean createPlanet(String node, String planetName);
	boolean createRoute(String originNode, String destinationNode, BigDecimal distance);

	boolean deleteRoute(Long routeId);
	boolean deletePlanet(String planetNode);

	boolean updatePlanet(String node, String newName);
	boolean updateRoute(Long routeId, BigDecimal newDistance);
}
