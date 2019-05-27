package org.openobservatory.ooniprobe.common;

import androidx.test.filters.SmallTest;

import org.junit.Assert;
import org.junit.Test;

@SmallTest
public class MKCollectorResubmitTaskTest {
    @Test
    public void standardTimeout() {
        Assert.assertEquals(MKCollectorResubmitTask.getTimeout(2000), 11);
    }

    public void zeroTimeout() {
        Assert.assertEquals(MKCollectorResubmitTask.getTimeout(0), 10);
    }

    public void maxTimeout() {
        Assert.assertEquals(MKCollectorResubmitTask.getTimeout(Long.MAX_VALUE), Long.MAX_VALUE / 2000 + 10);
    }
}
