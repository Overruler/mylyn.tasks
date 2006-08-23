/*******************************************************************************
 * Copyright (c) 2006 - 2006 Mylar eclipse.org project and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Mylar project committers - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylar.trac.tests;

import junit.framework.TestCase;

import org.eclipse.mylar.core.core.tests.support.MylarTestUtils;
import org.eclipse.mylar.core.core.tests.support.MylarTestUtils.Credentials;
import org.eclipse.mylar.core.core.tests.support.MylarTestUtils.PrivilegeLevel;
import org.eclipse.mylar.internal.trac.core.ITracClient;
import org.eclipse.mylar.internal.trac.core.TracClientFactory;
import org.eclipse.mylar.internal.trac.core.ITracClient.Version;

/**
 * Provides a base implementation for test cases that access trac repositories.
 * 
 * @author Steffen Pingel
 */
public abstract class AbstractTracClientTest extends TestCase {

	public String repositoryUrl;

	public ITracClient repository;

	public String username;

	public String password;

	public Version version;

	public AbstractTracClientTest(Version version) {
		this.version = version;
	}

	public ITracClient connect096() throws Exception {
		return connect(Constants.TEST_TRAC_096_URL);
	}

	public ITracClient connect010() throws Exception {
		return connect(Constants.TEST_TRAC_010_URL);
	}

	public ITracClient connect010DigestAuth() throws Exception {
		return connect(Constants.TEST_TRAC_010_DIGEST_AUTH_URL);
	}

	public ITracClient connect(String url) throws Exception {
		Credentials credentials = MylarTestUtils.readCredentials(PrivilegeLevel.USER);
		return connect(url, credentials.username, credentials.password);
	}

	public ITracClient connect(String url, String username, String password) throws Exception {
		this.repositoryUrl = url;
		this.username = username;
		this.password = password;
		this.repository = TracClientFactory.createClient(url, version, username, password);

		return this.repository;
	}

}
