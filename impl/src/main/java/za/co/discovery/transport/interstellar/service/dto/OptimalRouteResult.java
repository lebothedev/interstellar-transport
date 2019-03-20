package za.co.discovery.transport.interstellar.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptimalRouteResult {
	private Integer number;
	private String node;
	private String planetName;
}
