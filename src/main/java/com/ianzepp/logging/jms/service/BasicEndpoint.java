/**
 * The MIT License
 * 
 * Copyright (c) 2009 Ian Zepp
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 * @author Ian Zepp
 * @package 
 */
package com.ianzepp.logging.jms.service;

import java.io.IOException;
import java.io.StringReader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.springframework.ws.server.endpoint.AbstractJDomPayloadEndpoint;

/**
 * TODO Type description
 * 
 * @author izepp
 */
public class BasicEndpoint extends AbstractJDomPayloadEndpoint
{

	private BasicService service;
	private static final XMLOutputter serviceOutputter = new XMLOutputter ();
	private static final String REQUEST_INSERT = "InsertEventRequest".toLowerCase ();
	private static final String REQUEST_PURGE_BY_TIMESTAMP = "PurgeEventByTimestampRequest".toLowerCase ();
	private static final String REQUEST_PURGE_BY_LEVEL = "PurgeEventByLevelRequest".toLowerCase ();

	/**
	 * TODO Method description for <code>getService()</code>
	 * 
	 * @return
	 */
	public BasicService getService ()
	{
		return service;
	}

	/**
	 * Called by Spring from a JMS request scope.
	 * 
	 * @param request
	 * @return
	 */
	public String handleMessage (final String request)
	{
		Element response = handleRequest (toXml (request));

		if (response == null)
		{
			return null;
		}
		else
		{
			return serviceOutputter.outputString (response);
		}
	}

	/**
	 * TODO Method description for <code>handleRequest()</code>
	 * 
	 * @param request
	 * @return
	 */
	public Element handleRequest (final Element request)
	{
		final String requestName = request.getName ().toLowerCase ();

		if (REQUEST_INSERT.equals (requestName))
		{
			return getService ().insertEvent (request);
		}

		if (REQUEST_PURGE_BY_TIMESTAMP.equals (requestName))
		{
			return getService ().purgeEventByTimestamp (request);
		}

		if (REQUEST_PURGE_BY_LEVEL.equals (requestName))
		{
			return getService ().purgeEventByLevel (request);
		}

		return null;
	}

	/**
	 * Called by Spring from a SOAP request scope.
	 * 
	 * @param request
	 * @return
	 */
	@Override
	protected Element invokeInternal (final Element request)
	{
		return handleRequest (request);
	}

	/**
	 * TODO Method description for <code>setService()</code>
	 * 
	 * @param service
	 */
	public void setService (final BasicService service)
	{
		this.service = service;
	}

	/**
	 * TODO Method description for <code>toXml()</code>
	 * 
	 * @param data
	 * @return
	 */
	private Element toXml (final String data)
	{
		Document document = null;

		try
		{
			document = new SAXBuilder ().build (new StringReader (data));
		}
		catch (JDOMException e)
		{
			e.printStackTrace ();
		}
		catch (IOException e)
		{
			e.printStackTrace ();
		}

		if (document != null)
		{
			return document.getRootElement ();
		}
		else
		{
			return null;
		}
	}
}
