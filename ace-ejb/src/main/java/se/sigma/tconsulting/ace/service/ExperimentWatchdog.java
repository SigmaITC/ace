package se.sigma.tconsulting.ace.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import se.sigma.tconsulting.ace.model.OECData;

/*
 * This watchdog should guard the experiment.
 * If there are too many bad values it's bad for the organization. Therefore it should
 * contact the controller and suggest an abort of the experiment.
 */
@Stateless
public class ExperimentWatchdog {
	public static final Logger log = Logger.getLogger(ExperimentWatchdog.class);
	
	@Inject
	private ExperimentController controller;

	public void validate(OECData oec) {
		if (isOk(oec)) {
			controller.newInput();
		} else {
			/*
			 * Here logic for halting an experiment with too many values outside approved ranges
			 * should exist. For simulation, this is not needed as the simulation will not wander
			 * off from the vaild ranges
			 */
			log.error("OEC outside guardrails");
			oec.setBad(true);
			/*
			 * if too many bad:
			 *    contoller.suggestAbort();
			 * else:
			 */
			controller.newInput();
		}
	}

	// Validate OEC data
	private boolean isOk(OECData oec) {
		return oec.getOecValue() > 0 && oec.getOecValue() <= 100;
	}
}
