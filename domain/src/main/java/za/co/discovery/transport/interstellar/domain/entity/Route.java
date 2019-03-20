package za.co.discovery.transport.interstellar.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Represents a route consisting of two planets with a distance between them
 */
@Entity
@Table(name = "ROUTE")
@NamedQueries({
		@NamedQuery(name = Route.FIND_BY_ORIGIN_AND_DESTINATION, query = "SELECT r FROM Route r WHERE r.origin.node = :origin AND r.destination.node = :destination"),
		@NamedQuery(name = Route.FIND_ALL_ROUTES, query = "SELECT r FROM Route r"),
		@NamedQuery(name = Route.FIND_ROUTE_INCLUDING_PLANET, query = "SELECT r FROM Route r WHERE r.origin.node = :node OR r.destination.node = :node")
})
public class Route implements Serializable {

	private static final long serialVersionUID = -6018000542151840316L;

	public static final String FIND_BY_ORIGIN_AND_DESTINATION = "Route.findByOriginAndDestination";
	public static final String FIND_ALL_ROUTES = "Route.findAll";
	public static final String FIND_ROUTE_INCLUDING_PLANET = "Route.findRouteWithPlanet";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	@Getter @Setter
	private Long routeId;

	@Getter @Setter
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "ORIGIN_PLANET", referencedColumnName = "NODE")
	private Planet origin;

	@Getter @Setter
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "DESTINATION_PLANET", referencedColumnName = "NODE")
	private Planet destination;

	@Getter @Setter
	@Column(name = "DISTANCE")
	private BigDecimal distance;

	public Route() {}

	public Route(Planet originPlanet, Planet destinationPlanet, BigDecimal distance) {
		this.origin = originPlanet;
		this.destination = destinationPlanet;
		this.distance = distance;
	}
}
