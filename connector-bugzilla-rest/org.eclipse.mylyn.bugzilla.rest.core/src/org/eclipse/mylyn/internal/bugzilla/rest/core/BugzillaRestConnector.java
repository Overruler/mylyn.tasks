/*******************************************************************************
 * Copyright (c) 2013 Frank Becker and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Frank Becker - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.internal.bugzilla.rest.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.mylyn.commons.repositories.core.RepositoryLocation;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.eclipse.mylyn.tasks.core.sync.ISynchronizationSession;

public class BugzillaRestConnector extends AbstractRepositoryConnector {
	private static BugzillaRestConnector INSTANCE;

	public static BugzillaRestConnector getDefault() {
		return INSTANCE;
	}

	public BugzillaRestConnector() {
		super();
		INSTANCE = this;
	}

	@Override
	public boolean canCreateNewTask(TaskRepository repository) {
		return true;
	}

	@Override
	public boolean canCreateTaskFromKey(TaskRepository repository) {
		// ignore
		return false;
	}

	@Override
	public String getConnectorKind() {
		return BugzillaRestCore.CONNECTOR_KIND;
	}

	@Override
	public String getLabel() {
		return "Bugzilla 5.0 or later with REST";
	}

	@Override
	public String getRepositoryUrlFromTaskUrl(String taskUrl) {
		// ignore
		return null;
	}

	@Override
	public TaskData getTaskData(TaskRepository repository, String taskIdOrKey, IProgressMonitor monitor)
			throws CoreException {
		// ignore
		return null;
	}

	@Override
	public String getTaskIdFromTaskUrl(String taskUrl) {
		// ignore
		return null;
	}

	@Override
	public String getTaskUrl(String repositoryUrl, String taskIdOrKey) {
		// ignore
		return null;
	}

	@Override
	public boolean hasTaskChanged(TaskRepository taskRepository, ITask task, TaskData taskData) {
		// ignore
		return false;
	}

	@Override
	public IStatus performQuery(TaskRepository repository, IRepositoryQuery query, TaskDataCollector collector,
			ISynchronizationSession session, IProgressMonitor monitor) {
		// ignore
		return null;
	}

	@Override
	public void updateRepositoryConfiguration(TaskRepository taskRepository, IProgressMonitor monitor)
			throws CoreException {
		// ignore

	}

	@Override
	public void updateTaskFromTaskData(TaskRepository taskRepository, ITask task, TaskData taskData) {
		// ignore

	}

	@Override
	public AbstractTaskDataHandler getTaskDataHandler() {
		return new BugzillaRestTaskDataHandler();
	}

	public BugzillaRestClient createClient(TaskRepository repository) {
		BugzillaRestClient client = new BugzillaRestClient(new RepositoryLocation(repository.getProperties()));
		return client;
	}
}