package nz.co.hmccarth.measure;

import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

public class MeasureCPU implements MeasureInterface {

    private static OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory
            .getOperatingSystemMXBean();

    private static final int NUMBEROFCORES = operatingSystemMXBean.getAvailableProcessors();

    @Override
    public double measure() {
        return operatingSystemMXBean.getProcessCpuLoad() * (NUMBEROFCORES);
    }

}