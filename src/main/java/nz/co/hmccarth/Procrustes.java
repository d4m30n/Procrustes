package nz.co.hmccarth;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import nz.co.hmccarth.controller.ControllerInterface;

public class Procrustes {
	private long _MeasureIntervalInMillis;
	private ControllerInterface _controller;
	private boolean _running = false;
	private Thread _timeThread = new Thread(new Runnable() {

		@Override
		public void run() {
			while (_running) {
				long startTime = System.currentTimeMillis();
				_controller.evaluate();
				long endTime = System.currentTimeMillis();
				if ((endTime - startTime) < _MeasureIntervalInMillis) {
					try {
						TimeUnit.MILLISECONDS.sleep((endTime - startTime));
					} catch (InterruptedException e) {
					}
				}
			}
		}
	});

	public Procrustes(@Nonnegative long MeasureIntervalInSec, @Nonnull ControllerInterface controller) {
		_MeasureIntervalInMillis = TimeUnit.SECONDS.toMillis(MeasureIntervalInSec);
		_controller = controller;
	}

	public boolean start() {
		if (_running) {
			return false;
		}
		_running = true;
		_timeThread.start();
		return true;
	}

	public boolean stop() {
		_running = false;
		try {
			_timeThread.join();
		} catch (InterruptedException e) {
		}
		return true;
	}

}