package za.co.discovery.transport.interstellar.service.api;

import za.co.discovery.transport.interstellar.service.api.dto.PlanetResponse;
import za.co.discovery.transport.interstellar.service.api.dto.RouteResponse;
import za.co.discovery.transport.interstellar.service.api.exception.TransportSystemException;
import za.co.discovery.transport.interstellar.service.api.xsd.RouteResponseDocument;

import java.math.BigDecimal;
import java.util.List;

/**
 * Used to interact with backend functionality
 */
public interface TransportService {

	/**
	 * Find a specific planet using the unique node value.
	 *
	 * @param node - Node representing this planet
	 * @return - Wrapper object containg data about the planet
	 */
	PlanetResponse findPlanetByNode(String node);

	/**
	 * Find planet using the primary key.
	 *
	 * @param planetId - Unique planet ID
	 * @return
	 */
	PlanetResponse findPlanetById(Long planetId);

	/**
	 * Find a route between two given points.
	 *
	 * @param originNode      - Origin Node/Planet
	 * @param destinationNode - Destination Node/Planet
	 * @return  List of hops to get to the destination
	 */
	List<RouteResponse> findRoute(String originNode, String destinationNode);

	/**
	 * Returns all the planets configured in the system.
	 *
	 * @return
	 */
	List<PlanetResponse> findAllPlanets();

	/**
	 * Finds all the configured routes in the system.
	 *
	 * @return
	 */
	List<RouteResponse> findAllRoutes();

	/**
	 * Creates a new planet with the provided Node and name.
	 *
	 * @param node - Unique node value for the planet
	 * @param name - Name of this planet
	 * @return
	 */
	boolean createPlanet(String node, String name);

	/**
	 * Creates a new route between two adjacent points. The planets needs to be configured beforehand.
	 *
	 * @param originNode      - Starting point
	 * @param destinationNode - Destination node/planet
	 * @param distance        - How long the distance between the two nodes is.
	 * @return
	 */
	boolean createRoute(String originNode, String destinationNode, BigDecimal distance);

	/**
	 * Updates the name of the planet at the specified node.
	 *
	 * @param node    - Node representing this planet
	 * @param newName - The new name of this planet
	 * @return
	 */
	boolean updatePlanetName(String node, String newName);

	/**
	 * Updates the distance between two adjacent planets
	 *
	 * @param routeId     - ID of the route
	 * @param newDistance - New distance
	 * @return
	 */
	boolean updateRouteDistance(Long routeId, BigDecimal newDistance);

	/**
	 * Removes the planet at the specified node.
	 * @param node - The node configured for this planet
	 * @return
	 * @throws TransportSystemException - A planet on an existing route cannot be deleted.
	 */
	boolean deletePlanet(String node) throws TransportSystemException;

	/**
	 * Deletes a route from the system.
	 *
	 * @param routeId - The unique route ID
	 * @return
	 * @throws TransportSystemException - When an error occurs during the removal
	 */
	boolean deleteRoute(Long routeId) throws TransportSystemException;

	/**
	 * Determines the shortest path between two given points.
	 *
	 * @param origin      - Origin node/planet
	 * @param destination - Destination node/planet
	 * @return
	 */
	RouteResponseDocument determineShortestPath(String origin, String destination);
}
