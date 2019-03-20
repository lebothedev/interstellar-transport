package za.co.discovery.transport.interstellar.service.api.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RouteResponse implements Serializable {

	private static final long serialVersionUID = -7300104331030080557L;
	@XmlElement
	private Integer routeId;
	@XmlElement
	private PlanetResponse origin;
	@XmlElement
	private PlanetResponse destination;
	@XmlElement
	private BigDecimal distance;

	public RouteResponse() {
	}

	public RouteResponse(PlanetResponse origin, PlanetResponse destination, BigDecimal distance) {
		this.origin = origin;
		this.destination = destination;
		this.distance = distance;
	}
}
