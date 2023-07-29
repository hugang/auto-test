package io.hugang.jenkinsJob;

import io.hugang.util.Utils;
import org.junit.Test;

public class JenkinsJobTest {
    @Test
    public void testJenkinsJobXlsx() {
        Utils.execute("xlsx", "jenkinsJob/jenkinsJob.xlsx");
    }
}
