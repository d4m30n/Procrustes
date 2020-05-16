package nz.co.hmccarth.controller;

import javax.annotation.Nonnull;

import nz.co.hmccarth.measure.MeasureInterface;

public class ControllerPID implements ControllerInterface {

	private final MeasureInterface _measurement;
	private final double[] _parameters;
	private final double[] _weights;
	private final double[] K = { 1, 1, 1 };
	private double _setpoint;

	private double _pastError = 0;
	private double _integral = 0;
	private long updateTimeInSeconds = 1;

	private double _lastMeasurement = 0;
	private double _currentMeasurement = 0;

	/**
	 * Create a PID controller
	 * 
	 * @param measurement - The measurement class that is being utilised
	 * @param parameters  - Double array of parameters the controller can change
	 * @param weights     - List of weights for the respective parameters
	 * @param setpoint    - The setpoint that needs to be met by the PID controller
	 */
	public ControllerPID(@Nonnull MeasureInterface measurement, @Nonnull double[] parameters, @Nonnull double[] weights,
			double setpoint) {
		if (parameters == null)
			throw new AssertionError("Parameters can not be null");
		if (measurement == null)
			throw new AssertionError("Measurement Interface can not be null");
		if (weights == null) {
			double[] defaultWeights = new double[parameters.length];
			for (int i = 0; i < parameters.length; i++) {
				defaultWeights[i] = 1;
			}
		}
		if (parameters.length == weights.length)
			throw new AssertionError("Parameters and weights must be the same size");
		this._measurement = measurement;
		this._parameters = parameters;
		this._weights = weights;
		this._setpoint = setpoint;
	}

	/**
	 * Create a PID Controller
	 * 
	 * @param measurement - The measurment class that is being utilised
	 * @param parameters  - Double array of parameters the controller can change
	 * @param setpoint    - The setpoint that needs to be met by the PID controller
	 */
	public ControllerPID(@Nonnull MeasureInterface measurement, @Nonnull double[] parameters, double setpoint) {
		this(measurement, parameters, null, setpoint);
	}

	/**
	 * Allows the setpoint in the controller to be updated
	 * 
	 * @param setpoint - The new setpoint to use
	 */
	public void updateSetpoint(double setpoint) {
		this._setpoint = setpoint;
	}

	/**
	 * Gets the measurment take one back from the current
	 * 
	 * @return - The last measurement
	 */
	public double getLastMeasurement() {
		return this._lastMeasurement;
	}

	/**
	 * Gets the current measurement that was take on the last evaluate call
	 * 
	 * @return - Gets the current measurement
	 */
	public double getCurrentMeasurement() {
		return this._currentMeasurement;
	}

	/**
	 * Allows for setting a diffrent K value
	 * 
	 * @param Kp - The propotional gain
	 * @param Ki - The integral gain
	 * @param Kd - The derivative gain
	 */
	public void setK(double Kp, double Ki, double Kd) {
		K[0] = Kp;
		K[1] = Ki;
		K[2] = Kd;
	}

	@Override
	public double[] evaluate() {
		double measurement = _measurement.measure();
		double error = this._setpoint - measurement;
		double integral = this._integral + (error * updateTimeInSeconds);
		double derivative = (error - this._pastError) / updateTimeInSeconds;
		double output = (K[0] * error) + (K[1] * integral) + (K[2] * derivative);

		for (int i = 0; i < this._parameters.length; i++) {
			this._parameters[i] += (output * this._weights[i]);
		}

		this._pastError = error;
		this._integral = integral;
		this._lastMeasurement = this._currentMeasurement;
		this._currentMeasurement = measurement;
		return this._parameters;
	}

}