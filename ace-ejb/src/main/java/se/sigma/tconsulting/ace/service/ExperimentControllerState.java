package se.sigma.tconsulting.ace.service;

/*
 * Different states to represent how the system behaves.
 * - Learning indicates that the system collects data to detect patterns
 * -Static indicates that the system has found optimal values and
 * is not trying to generate new information anymore.
 * - Optimizing indicates that the system has a base of learning data
 * and now tries to find optimal configuration values
 */
public enum ExperimentControllerState {
	LEARNING,
	STATIC,
	OPTIMIZING
}
