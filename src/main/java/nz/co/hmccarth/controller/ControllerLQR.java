package nz.co.hmccarth.controller;

import com.mccarthy.control.LQR;
import com.mccarthy.control.SS;
import com.mccarthy.control.UnableToEvaluateStateSolution;

import org.ejml.simple.SimpleMatrix;

public class ControllerLQR implements ControllerInterface {

    public enum LQRMatrix {
        A, B, C, D, Q, R, x, u;
    }

    private SS _system;
    private LQR _gain;
    private SimpleMatrix _x;
    private SimpleMatrix _u;
    private SimpleMatrix _setpoints;

    public ControllerLQR(double[][] A, double[][] B, double[][] C, double[][] D, double[][] Q, double[][] R, double[] x,
            double[] u, double[] setpoints) throws UnableToEvaluateStateSolution {

        _system = new SS(new SimpleMatrix(A), new SimpleMatrix(B), new SimpleMatrix(C), new SimpleMatrix(D));
        _gain = new LQR(_system, new SimpleMatrix(Q), new SimpleMatrix(R));

        double[][] _x_ = new double[1][x.length];
        for (int i = 0; i < x.length; i++) {
            _x_[0][i] = x[i];
        }
        this._x = new SimpleMatrix(_x_);
        double[][] _u_ = new double[1][u.length];
        for (int i = 0; i < u.length; i++) {
            _u_[0][i] = u[i];
        }
        this._u = new SimpleMatrix(_u_);

        if (setpoints.length != x.length)
            throw new AssertionError("setpoints must match x in size");
        double[][] _setpoints_ = new double[1][setpoints.length];
        for (int i = 0; i < setpoints.length; i++) {
            _setpoints_[0][i] = setpoints[i];
        }
        this._setpoints = new SimpleMatrix(_setpoints_);
    }

    /**
     * Allows for updating the setpoints if required
     * 
     * @param setpoints - A new set of setpoints. Must be the same size as the
     *                  original
     */
    public void updateSetpoints(double[] setpoints) {
        if (this._setpoints.numRows() != setpoints.length)
            throw new AssertionError("The new setpoints must be the same size as the last setpoints given");
        double[][] _setpoints_ = new double[1][setpoints.length];
        for (int i = 0; i < setpoints.length; i++) {
            _setpoints_[0][i] = setpoints[i];
        }
        this._setpoints = new SimpleMatrix(_setpoints_);
    }

    public double[] getSetpoints() {
        double[] tmpSetpointHolder = new double[this._setpoints.numRows()];
        for (int i = 0; i < this._setpoints.numRows(); i++) {
            tmpSetpointHolder[i] = this._setpoints.get(0, i);
        }
        return tmpSetpointHolder;
    }

    @Override
    public double[] evaluate() {
        this._x = this._system.stepSystem(this._x, this._u);
        this._u = this._gain.getK().mult(this._u.minus(this._setpoints));
        double[] returnValue = new double[this._u.numRows()];
        for (int i = 0; i < this._u.numRows(); i++) {
            returnValue[i] = this._u.get(i, 0);
        }
        return returnValue;
    }

}