package za.co.discovery.transport.interstellar.service.api.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PlanetResponse implements Serializable {

	private static final long serialVersionUID = -4234649442404099515L;
	@XmlElement
	private Long planetId;
	@XmlElement
	private String node;
	@XmlElement
	private String name;

	public PlanetResponse() {
	}
	public PlanetResponse(String node, String name) {
		this.node = node;
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("%s - %s", node, name);
	}
}
