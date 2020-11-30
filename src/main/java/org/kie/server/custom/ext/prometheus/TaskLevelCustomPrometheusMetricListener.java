package org.kie.server.custom.ext.prometheus;

import java.util.List;

import org.jbpm.services.task.lifecycle.listeners.TaskLifeCycleEventListener;
import org.kie.api.task.TaskEvent;
import org.kie.api.task.model.OrganizationalEntity;
import org.kie.server.custom.ext.constants.CustomPrometheusMetricConstants;
import org.kie.server.custom.ext.util.CustomPrometheusMetricUtil;
import org.kie.server.services.api.KieContainerInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.prometheus.client.Counter;

public class TaskLevelCustomPrometheusMetricListener implements TaskLifeCycleEventListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskLevelCustomPrometheusMetricListener.class);
	
	private  KieContainerInstance kieContainer;
	
	private static Counter numberOfTasksAssigned = null;
	
	private static Counter numberOfTasksCompleted = null;
	
	public TaskLevelCustomPrometheusMetricListener(final KieContainerInstance kieContainer) {
		 this.kieContainer = kieContainer;
	}
	
	public TaskLevelCustomPrometheusMetricListener() {
		init();
	}
	
	public void init() {
		LOGGER.debug("Initialising the metrics");
		// Register intesa_kie_server_user_tasks_assigned_total
		if(!CustomPrometheusMetricUtil.doesMetricRegistered(CustomPrometheusMetricConstants.USER_TASKS_ASSIGNED_TOTAL)) {
			numberOfTasksAssigned = CustomPrometheusMetricUtil.registerCounterMetric(CustomPrometheusMetricConstants.USER_TASKS_ASSIGNED_TOTAL, 
					CustomPrometheusMetricConstants.USER_TASKS_ASSIGNED_TOTAL_HELP, 
					CustomPrometheusMetricConstants.LABEL_CONTAINERID, CustomPrometheusMetricConstants.LABEL_PROCESSID, CustomPrometheusMetricConstants.LABEL_USER);
		}
		
		// Register intesa_kie_server_user_tasks_complted_total
		if(!CustomPrometheusMetricUtil.doesMetricRegistered(CustomPrometheusMetricConstants.USER_TASKS_COMPLETED_TOTAL)) {
			numberOfTasksCompleted = CustomPrometheusMetricUtil.registerCounterMetric(CustomPrometheusMetricConstants.USER_TASKS_COMPLETED_TOTAL, 
					CustomPrometheusMetricConstants.USER_TASKS_TOTAL_COMPLETED_HELP, 
					CustomPrometheusMetricConstants.LABEL_CONTAINERID, CustomPrometheusMetricConstants.LABEL_PROCESSID, CustomPrometheusMetricConstants.LABEL_USER);
		}
	}

	public void afterTaskAssignmentsAddedEvent(TaskEvent event, AssignmentType type, List<OrganizationalEntity> entities){
		System.out.println("afterTaskAssignmentsAddedEvent [" + event.getTaskContext().getUserId() +"]");
	}
	
    public void afterTaskClaimedEvent( TaskEvent event) {
    	System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
    	if(event.getTask() != null & event.getTask().getTaskData() != null && event.getTask().getTaskData().getActualOwner() != null && event.getTask().getTaskData().getActualOwner().getId() != null) {
			numberOfTasksAssigned.labels(event.getTask().getTaskData().getDeploymentId(), event.getTask().getTaskData().getProcessId(), event.getTask().getTaskData().getActualOwner().getId()).inc();
		}
    }

	public void beforeTaskActivatedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void beforeTaskClaimedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void beforeTaskSkippedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void beforeTaskStartedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void beforeTaskStoppedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void beforeTaskCompletedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void beforeTaskFailedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void beforeTaskAddedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void beforeTaskExitedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void beforeTaskReleasedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void beforeTaskResumedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void beforeTaskSuspendedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void beforeTaskForwardedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void beforeTaskDelegatedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void beforeTaskNominatedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void afterTaskActivatedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void afterTaskSkippedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void afterTaskStartedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void afterTaskStoppedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void afterTaskCompletedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
		numberOfTasksCompleted.labels(event.getTask().getTaskData().getDeploymentId(), event.getTask().getTaskData().getProcessId(), event.getTaskContext().getUserId()).inc();
	}

	public void afterTaskFailedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void afterTaskAddedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
		if(event.getTask() != null & event.getTask().getTaskData() != null && event.getTask().getTaskData().getActualOwner() != null && event.getTask().getTaskData().getActualOwner().getId() != null) {
			numberOfTasksAssigned.labels(event.getTask().getTaskData().getDeploymentId(), event.getTask().getTaskData().getProcessId(), event.getTask().getTaskData().getActualOwner().getId()).inc();
		}
	}

	public void afterTaskExitedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void afterTaskReleasedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void afterTaskResumedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void afterTaskSuspendedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void afterTaskForwardedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void afterTaskDelegatedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}

	public void afterTaskNominatedEvent(TaskEvent event) {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName() + " Task perforemd User [" + event.getTaskContext().getUserId() + "] ActualOwner [" + event.getTask().getTaskData().getActualOwner() +"]");
	}
}
