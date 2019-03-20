package za.co.discovery.transport.interstellar.service.helper;

import za.co.discovery.transport.interstellar.service.api.dto.PlanetResponse;
import za.co.discovery.transport.interstellar.service.api.dto.RouteResponse;
import za.co.discovery.transport.interstellar.service.api.exception.TransportSystemException;
import za.co.discovery.transport.interstellar.service.galaxy.Galaxy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Determines the shortest path between two planets in the galaxy.
 */
public class OptimalRouteCalculator {

	private List<PlanetResponse> planets;
	private List<RouteResponse> routes;

	private Set<PlanetResponse> discoveredPlanets;
	private Set<PlanetResponse> remainingPlanets;

	private Map<PlanetResponse, PlanetResponse> predecessors;
	private Map<PlanetResponse, BigDecimal> distance;

	public OptimalRouteCalculator(Galaxy galaxy) {
		this.planets = galaxy.getPlanets();
		this.routes = galaxy.getRoutes();
	}

	public void begin(PlanetResponse start) {
		discoveredPlanets = new HashSet<PlanetResponse>();
		remainingPlanets = new HashSet<PlanetResponse>();

		distance = new HashMap<PlanetResponse, BigDecimal>();
		predecessors = new HashMap<PlanetResponse, PlanetResponse>();

		distance.put(start, BigDecimal.ZERO);
		remainingPlanets.add(start);

		while (remainingPlanets.size() > 0) {
			PlanetResponse planet = getClosest(remainingPlanets);
			discoveredPlanets.add(planet);
			remainingPlanets.remove(planet);
			findShortestDistance(planet);
		}
	}

	public LinkedList<PlanetResponse> getPath(PlanetResponse planet) {
		LinkedList<PlanetResponse> path = new LinkedList<PlanetResponse>();
		PlanetResponse step = planet;

		if (predecessors.get(step) == null) {
			return null;
		}
		path.add(step);
		while (predecessors.get(step) != null) {
			step = predecessors.get(step);
			path.add(step);
		}

		Collections.reverse(path);
		return path;
	}

	private void findShortestDistance(PlanetResponse planet) {
		List<PlanetResponse> adjacentPlanets = getNeighbours(planet);
		for (PlanetResponse target : adjacentPlanets) {
			if (getShortestDistance(target).compareTo(getShortestDistance(planet).add(getDistance(planet, target))) > 0) {
				distance.put(target, getShortestDistance(planet).add(getDistance(planet, target)));
				predecessors.put(target, planet);
				remainingPlanets.add(target);
			}
		}
	}

	private BigDecimal getDistance(PlanetResponse planet, PlanetResponse target) {
		for (RouteResponse route : routes) {
			if (route.getOrigin().equals(planet) && route.getDestination().equals(target)) {
				return route.getDistance();
			}
		}
		throw new TransportSystemException("Route configuration error in system");
	}

	private List<PlanetResponse> getNeighbours(PlanetResponse planet) {
		List<PlanetResponse> neighbours = new ArrayList<PlanetResponse>();
		for (RouteResponse route : routes) {
			if (route.getOrigin().equals(planet) && !isDiscovered(route.getDestination())) {
				neighbours.add(route.getDestination());
			}
		}
		return neighbours;
	}

	private boolean isDiscovered(PlanetResponse planet) {
		return discoveredPlanets.contains(planet);
	}

	private PlanetResponse getClosest(Set<PlanetResponse> planets) {
		PlanetResponse closest = null;

		for (PlanetResponse planet : planets) {
			if (closest == null) {
				closest = planet;
			} else {
				if (getShortestDistance(planet).compareTo(getShortestDistance(closest)) < 0) {
					closest = planet;
				}
			}
		}
		return closest;
	}

	private BigDecimal getShortestDistance(PlanetResponse destination) {
		BigDecimal distanceToDestination = distance.get(destination);
		if (distanceToDestination == null) {
			return BigDecimal.valueOf(Integer.MAX_VALUE);
		} else {
			return distanceToDestination;
		}
	}
}
