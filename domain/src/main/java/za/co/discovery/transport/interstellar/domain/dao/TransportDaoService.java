package za.co.discovery.transport.interstellar.domain.dao;

import lombok.extern.java.Log;
import za.co.discovery.transport.interstellar.domain.DataFileReader;
import za.co.discovery.transport.interstellar.domain.entity.Planet;
import za.co.discovery.transport.interstellar.domain.entity.Route;
import za.co.discovery.transport.interstellar.service.api.exception.TransportSystemException;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;

@Log
@Singleton
@Startup
public class TransportDaoService {

	@PersistenceContext(unitName = "interstellar-transport-db")
	private EntityManager entityManager;

	private static final String ROUTES_SOURCE_FILE_NAME = "import/routes.csv";
	private static final String PLANETS_SOURCE_FILE_NAME = "import/planets.csv";

	@PostConstruct
	public void initialise() {
		loadPlanetsFromFile();
		loadRoutesFromFile();
	}

	public List<Planet> findAllPlanets() {
		return entityManager.createNamedQuery(Planet.FIND_ALL_PLANETS).getResultList();
	}

	public List<Route> findAllRoutes() {
		return entityManager.createNamedQuery(Route.FIND_ALL_ROUTES).getResultList();
	}

	public Planet findPlanetById(Long planetId) {
		return entityManager.find(Planet.class, planetId);
	}

	public Route findRouteById(Long routeId) {
		return entityManager.find(Route.class, routeId);
	}

	public List<Route> findRoute(String origin, String destination) {
		return entityManager.createNamedQuery(Route.FIND_BY_ORIGIN_AND_DESTINATION)
				.setParameter("origin", origin)
				.setParameter("destination", destination)
				.getResultList();
	}

	public List<Route> findRouteIncludingPlanet(String planetNode) {
		return entityManager.createNamedQuery(Route.FIND_ROUTE_INCLUDING_PLANET)
				.setParameter("node", planetNode)
				.getResultList();
	}

	public Planet findPlanetUsingNode(String node) {
		try {
			return (Planet) entityManager.createNamedQuery(Planet.FIND_PLANET_BY_NODE)
					.setParameter("node", node)
					.getSingleResult();
		} catch (NoResultException nre) {
			log.log(Level.WARNING, "Could not find planet with node {0}", node);
			return null;
		}
	}

	public boolean createPlanet(Planet planet) {
		try {
			entityManager.persist(planet);
			return true;
		} catch (Exception r) {
			String errorMessage = String.format("Failed to save planet. Reason: %s", r.getMessage());
			logError(errorMessage, r);
			throw new TransportSystemException(errorMessage, r);
		}
	}

	public boolean createRoute(Route route) {
		try {
			entityManager.persist(route);
			return true;
		} catch (PersistenceException r) {
			String errorMessage = String.format("Failed to save route. Reason: %s", r.getMessage());
			logError(errorMessage, r);
			throw new TransportSystemException(errorMessage, r);
		}
	}

	public boolean updatePlanet(Planet planet) {
		try {
			entityManager.merge(planet);
			return true;
		} catch (Exception p) {
			String errorMessage = String.format("Failed to update planet. Reason: %s", p.getMessage());
			logError(errorMessage, p);
			throw new TransportSystemException(errorMessage, p);
		}
	}

	public boolean updateRoute(Route route) {
		try {
			entityManager.merge(route);
			return true;
		} catch (Exception p) {
			String errorMessage = String.format("Failed to update route. Reason: %s", p.getMessage());
			logError(errorMessage, p);
			throw new TransportSystemException(errorMessage, p);
		}
	}

	public boolean deletePlanet(String planetNode) {
		try {
			List<Route> routes = findRouteIncludingPlanet(planetNode);
			if (routes != null && !routes.isEmpty()) {
				throw new TransportSystemException("Cannot delete planet. It is used on routes");
			} else {
				Planet planet = findPlanetUsingNode(planetNode);
				if (planet != null) {
					entityManager.remove(planet);
				}
				return true;
			}
		} catch (Exception ex) {
			String errorMessage = String.format("Failed to delete planet. Reason: %s", ex.getMessage());
			logError(errorMessage, ex);
			throw new TransportSystemException(errorMessage, ex);
		}
	}

	public boolean deleteRoute(Long routeId) {
		try {
			Route route = findRouteById(routeId);
			if (route != null) {
				entityManager.remove(route);
			}
			return true;
		} catch (Exception r) {
			String errorMessage = String.format("Failed to delete route. Reason: %s", r.getMessage());
			logError(errorMessage, r);
			throw new TransportSystemException(errorMessage, r);
		}
	}

	public void saveRoute(Route routeToAdd) {
		entityManager.persist(routeToAdd);
	}

	public void savePlanet(Planet planetToAdd) {
		entityManager.persist(planetToAdd);
	}

	private void logError(String errorMessage, Throwable cause) {
		log.log(Level.SEVERE, errorMessage, cause);
	}

	private void loadPlanetsFromFile() {
		List<String[]> data = DataFileReader.readDataFromFile(PLANETS_SOURCE_FILE_NAME);
		for (String[] entry : data) {
			Planet planet = new Planet(entry[0], entry[1]);
			savePlanet(planet);
		}
	}

	private void loadRoutesFromFile() {
		List<String[]> data = DataFileReader.readDataFromFile(ROUTES_SOURCE_FILE_NAME);
		for (String[] entry : data) {
			Planet originPlanet = findPlanetUsingNode(entry[1]);
			if (originPlanet != null) {
				Planet destinationPlanet = findPlanetUsingNode(entry[2]);
				if (destinationPlanet != null) {
					BigDecimal distance = new BigDecimal(entry[3]);
					Route routeEntry = new Route(originPlanet, destinationPlanet, distance);
					saveRoute(routeEntry);
				}
			}
		}
	}
}
