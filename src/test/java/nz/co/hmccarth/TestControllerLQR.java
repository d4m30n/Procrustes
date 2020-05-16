package nz.co.hmccarth;

import org.junit.Test;

import nz.co.hmccarth.controller.ControllerLQR;

public class TestControllerLQR {

	private ControllerLQR lqr = null;

	@Test
	public void buildSystem() {
		double[][] A = { { -0.5, 0 }, { 0, -0.3 } };
		double[][] B = { { 5, 6, 3 }, { 4, 2, 3 } };
		double[][] C = { { 1, 0 }, { 0, 1 } };
		double[][] D = { { 0, 0, 0 }, { 0, 0, 0 } };
	}
}