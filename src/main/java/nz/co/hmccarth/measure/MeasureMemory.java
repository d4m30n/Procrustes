package nz.co.hmccarth.measure;

import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

public class MeasureMemory implements MeasureInterface {

    private static OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory
            .getOperatingSystemMXBean();

    @Override
    public double measure() {
        long totalAvalableMemory = operatingSystemMXBean.getTotalPhysicalMemorySize()
                + operatingSystemMXBean.getTotalSwapSpaceSize();
        long totalFreeMemory = operatingSystemMXBean.getFreePhysicalMemorySize()
                + operatingSystemMXBean.getFreeSwapSpaceSize();
        long totalUsedMemory = totalAvalableMemory - totalFreeMemory;
        return 100.0 * totalUsedMemory / totalAvalableMemory;
    }

}