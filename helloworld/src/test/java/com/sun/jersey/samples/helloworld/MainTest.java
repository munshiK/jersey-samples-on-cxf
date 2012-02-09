/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 *
 * Portions Copyright 2012 Talend.
 */

package com.sun.jersey.samples.helloworld;

import javax.ws.rs.ext.RuntimeDelegate;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import javax.ws.rs.core.MediaType;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.AfterClass;

public class MainTest {

	final static String hwURI = "http://localhost:9998/helloworld";
	private static Server server;

	@BeforeClass
	public static void initialize() throws Exception {
		PersonApplication application = new PersonApplication();
		RuntimeDelegate delegate = RuntimeDelegate.getInstance();

		JAXRSServerFactoryBean bean = delegate.createEndpoint(application,
				JAXRSServerFactoryBean.class);
		bean.setAddress("http://localhost:9998" + bean.getAddress());
		server = bean.create();
		server.start();
	}

	@AfterClass
	public static void destroy() throws Exception {
		server.stop();
		server.destroy();
	}

	/**
	 * Test to see that the message "Hello World" is sent in the response.
	 */
	@Test
	public void testHelloWorld() {
		WebClient webResource = WebClient.create(hwURI);
		webResource.accept(MediaType.TEXT_PLAIN);
		String responseMsg = webResource.get(String.class);
		assertEquals("Hello World", responseMsg);
	}

	/**
	 * Test if a WADL document is available at the relative path "?_wadl".
	 */
	@Test
	public void testApplicationWadl() {
		WebClient webResource = WebClient.create(hwURI + "?_wadl");
		String serviceWadl = webResource.accept("application/xml").get(
				String.class);
		assertTrue(serviceWadl.length() > 0);
	}
}