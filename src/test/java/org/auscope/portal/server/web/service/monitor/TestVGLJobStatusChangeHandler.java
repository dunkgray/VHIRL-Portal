package org.auscope.portal.server.web.service.monitor;

import java.util.Date;

import org.auscope.portal.core.test.PortalTestClass;
import org.auscope.portal.server.vegl.VEGLJob;
import org.auscope.portal.server.vegl.VEGLJobManager;
import org.auscope.portal.server.vegl.VGLJobStatusAndLogReader;
import org.auscope.portal.server.vegl.mail.JobMailSender;
import org.auscope.portal.server.web.controllers.JobBuilderController;
import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for VGLJobStatusChangeHandler.
 *
 * @author Richard Goh
 */
public class TestVGLJobStatusChangeHandler extends PortalTestClass {
    private VGLJobStatusChangeHandler handler;
    private VEGLJobManager mockJobManager;
    private JobMailSender mockJobMailSender;
    private VEGLJob mockJob;
    private VGLJobStatusAndLogReader mockVGLJobStatusAndLogReader;

    @Before
    public void init() {
        //Mock objects required for the unit tests
        mockJobManager = context.mock(VEGLJobManager.class);
        mockJobMailSender = context.mock(JobMailSender.class);
        mockJob = context.mock(VEGLJob.class);
        mockVGLJobStatusAndLogReader = context.mock(VGLJobStatusAndLogReader.class);

        //This is the component under test
        handler = new VGLJobStatusChangeHandler(mockJobManager,
                mockJobMailSender,mockVGLJobStatusAndLogReader);
    }

    /**
     * Tests that the handle status change method do nothing
     * when the job being processed has unsubmitted status.
     */
    @Test
    public void testHandleStatusChange_UnsubmittedJob() {
        final String oldStatus = JobBuilderController.STATUS_PENDING;
        final String newStatus = JobBuilderController.STATUS_UNSUBMITTED;
        handler.handleStatusChange(mockJob, newStatus, oldStatus);
    }

    /**
     * Tests that the handle status change method succeeds
     * for a completed job with email notification disabled.
     */
    @Test
    public void testHandleStatusChange_JobDoneAndEmailNotificationDisabled() {
        final int jobId = 123;
        final String oldStatus = JobBuilderController.STATUS_PENDING;
        final String newStatus = JobBuilderController.STATUS_DONE;
        final String timeLog = "Time is 10";

        context.checking(new Expectations() {{
            allowing(mockJob).getId();will(returnValue(jobId));
            oneOf(mockJob).getEmailNotification();will(returnValue(false));
            oneOf(mockJob).setProcessDate(with(any(Date.class)));
            oneOf(mockVGLJobStatusAndLogReader).getSectionedLog(mockJob, "Time");will(returnValue(timeLog));
            oneOf(mockJob).setProcessTimeLog(timeLog);
            oneOf(mockJob).setStatus(newStatus);
            oneOf(mockJobManager).saveJob(mockJob);
            oneOf(mockJobManager).createJobAuditTrail(oldStatus, mockJob, "Job status updated.");
        }});

        handler.handleStatusChange(mockJob, newStatus, oldStatus);
    }

    /**
     * Tests that the handle status change method succeeds
     * for a completed job with email notification enabled.
     */
    @Test
    public void testHandleStatusChange_JobDoneAndEmailNotificationEnabled() {
        final int jobId = 123;
        final String oldStatus = JobBuilderController.STATUS_PENDING;
        final String newStatus = JobBuilderController.STATUS_DONE;
        final String timeLog = "Time is 10";

        context.checking(new Expectations() {{
            allowing(mockJob).getId();will(returnValue(jobId));
            oneOf(mockJob).getEmailNotification();will(returnValue(true));
            oneOf(mockJob).setProcessDate(with(any(Date.class)));
            oneOf(mockVGLJobStatusAndLogReader).getSectionedLog(mockJob, "Time");will(returnValue(timeLog));
            oneOf(mockJob).setProcessTimeLog(timeLog);
            oneOf(mockJob).setStatus(newStatus);
            oneOf(mockJobManager).saveJob(mockJob);
            oneOf(mockJobManager).createJobAuditTrail(oldStatus, mockJob, "Job status updated.");
            oneOf(mockJobMailSender).sendMail(mockJob);
        }});

        handler.handleStatusChange(mockJob, newStatus, oldStatus);
    }
}