package nz.co.hmccarth.controller;

import javax.annotation.Nonnull;

import com.mccarthy.control.UnableToEvaluateStateSolution;

import nz.co.hmccarth.measure.MeasureInterface;

public class ControllerPIDAssistedLQR implements ControllerInterface {

	private final ControllerPID[] setpointControllers;
	private int controllersAdded = 0;
	private ControllerLQR systemController;

	public ControllerPIDAssistedLQR(double[][] A, double[][] B, double[][] C, double[][] D, double[][] Q, double[][] R,
			double[] x, double[] u, double[] setpoints) throws UnableToEvaluateStateSolution {
		setpointControllers = new ControllerPID[setpoints.length];
		systemController = new ControllerLQR(A, B, C, D, Q, R, x, u, setpoints);
	}

	public boolean addSetpoint(@Nonnull MeasureInterface measurement, double setpoint) {
		assert measurement != null : "measurement interface can not be null";

		if (controllersAdded == setpointControllers.length)
			return false;
		double[] tmpParameter = { systemController.getSetpoints()[controllersAdded] };
		ControllerPID tmpControllerHolder = new ControllerPID(measurement, tmpParameter, setpoint);
		setpointControllers[controllersAdded] = tmpControllerHolder;
		return true;
	}

	@Override
	public double[] evaluate() {
		assert setpointControllers != null : "setpoint controllers must not be null";
		assert controllersAdded == setpointControllers.length : "invalid all PID controllers must be added";

		double[] setpoints = new double[setpointControllers.length];
		for (int i = 0; i < setpointControllers.length; i++) {
			setpoints[i] = setpointControllers[i].evaluate()[0];
		}
		systemController.updateSetpoints(setpoints);
		return systemController.evaluate();
	}

}