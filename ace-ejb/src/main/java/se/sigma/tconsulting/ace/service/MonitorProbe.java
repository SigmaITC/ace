package se.sigma.tconsulting.ace.service;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.pow;

import javax.ejb.Stateless;
import javax.inject.Inject;

import se.sigma.tconsulting.ace.model.SUEConfiguration;
import se.sigma.tconsulting.ace.model.SUEData;
/*
 * A probe should enable communication to collect from the SUE.
 * Depending on how the SUE is built, a probe can have various forms; polling, event-driven, REST, etc..
 * 
 * In this simulation, we simply accept a configuration directly from the Effector and generate
 * the result from here.
 */
@Stateless
public class MonitorProbe {
	
	@Inject
	private Monitor monitor;
	
	public void feed(SUEConfiguration config) {
		SUEData data = new SUEData();
		data.setConfiguration(config);
		data.setResult(simulation(config.getValue()));
		monitor.handleData(data);
	}
	
	private Double simulation(Double configValue) {
		// Creates a curve with two peaks
		Double result = (-pow(configValue - 3, 2) * pow(configValue/5 - 3, 2) + 70);
		// Limit result between 0 and 100
		return max(0, min(result, 100));
	}
}
