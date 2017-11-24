package se.sigma.tconsulting.ace.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import se.sigma.tconsulting.ace.model.SUEConfiguration;

/*
 * The effector tells the SUE to use a specific configuration. It is also responsible
 * to translate the new configuration to a format/message understandable by the SUE.
 * 
 * In this case it simply contact the monitor probe directly
 */
@Stateless
public class Effector {
	@Inject
	private MonitorProbe probe;

	public void push(SUEConfiguration configuration) {
		probe.feed(configuration);
	}
}
