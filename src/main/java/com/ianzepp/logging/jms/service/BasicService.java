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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jdom.Element;
import org.jdom.Namespace;

/**
 * TODO Type description
 * 
 * @author izepp
 */
public class BasicService
{

	private BasicDao dao;

	/**
	 * TODO Method description for <code>getDao()</code>
	 * 
	 * @return
	 */
	public BasicDao getDao()
	{
		return dao;
	}

	/**
	 * TODO Method description for <code>saveEvent()</code>
	 * 
	 * @param request
	 * @return
	 */
	public Element insertEvent( final Element request )
	{
		final UUID requestId = UUID.randomUUID();

		getDao().executeQuery( "InsertEvent", newEventMap( requestId, request ) );
		getDao().executeQuery( "InsertEventException", newEventExceptionMap( requestId, request ) );
		getDao().executeQuery( "InsertEventLocation", newEventLocationMap( requestId, request ) );
		getDao().executeQuery( "InsertEventUserRequest", newEventUserRequestMap( requestId, request ) );

		return null;
	}

	/**
	 * TODO Method description for <code>newEventExceptionMap()</code>
	 * 
	 * @param requestId
	 * @param request
	 * @return
	 */
	protected Map<String, Object> newEventExceptionMap( final UUID requestId, final Element request )
	{
		final Map<String, Object> map = new HashMap<String, Object>();
		final Namespace ns = request.getNamespace();
		final Element requestNode = request.getChild( "exception", ns );

		if ( requestNode == null )
		{
			return map;
		}

		map.put( "Detail", requestNode.getChildText( "detail", ns ) );
		map.put( "EventId", requestId.toString() );
		map.put( "ExceptionName", requestNode.getChildText( "exceptionName", ns ) );
		map.put( "Id", UUID.randomUUID().toString() );
		map.put( "Message", requestNode.getChildText( "message", ns ) );

		return map;
	}

	/**
	 * TODO Method description for <code>newEventLocationMap()</code>
	 * 
	 * @param requestId
	 * @param request
	 * @return
	 */
	protected Map<String, Object> newEventLocationMap( final UUID requestId, final Element request )
	{
		final Map<String, Object> map = new HashMap<String, Object>();
		final Namespace ns = request.getNamespace();
		final Element requestNode = request.getChild( "location", ns );

		if ( requestNode == null )
		{
			return map;
		}

		map.put( "ClassName", requestNode.getChildText( "className", ns ) );
		map.put( "EventId", requestId.toString() );
		map.put( "FileName", requestNode.getChildText( "fileName", ns ) );
		map.put( "Id", UUID.randomUUID().toString() );
		map.put( "LineNumber", requestNode.getChildText( "lineNumber", ns ) );
		map.put( "MethodName", requestNode.getChildText( "methodName", ns ) );

		return map;
	}

	/**
	 * TODO Method description for <code>newEventMap()</code>
	 * 
	 * @param requestId
	 * @param request
	 * @return
	 */
	protected Map<String, Object> newEventMap( final UUID requestId, final Element request )
	{
		final Map<String, Object> map = new HashMap<String, Object>();
		final Namespace ns = request.getNamespace();

		map.put( "CorrelationId", request.getChildText( "correlationId", ns ) );
		map.put( "Host", request.getChildText( "host", ns ) );
		map.put( "Id", requestId.toString() );
		map.put( "Level", request.getChildText( "level", ns ) );
		map.put( "Logger", request.getChildText( "logger", ns ) );
		map.put( "Message", request.getChildText( "message", ns ) );
		map.put( "MessageId", request.getChildText( "messageId", ns ) );
		map.put( "Project", request.getChildText( "project", ns ) );
		map.put( "Service", request.getChildText( "service", ns ) );
		map.put( "Thread", request.getChildText( "thread", ns ) );
		map.put( "Timestamp", request.getChildText( "timestamp", ns ) );

		return map;
	}

	/**
	 * TODO Method description for <code>newEventUserRequestMap()</code>
	 * 
	 * @param requestId
	 * @param request
	 * @return
	 */
	protected Map<String, Object> newEventUserRequestMap( final UUID requestId, final Element request )
	{
		final Map<String, Object> map = new HashMap<String, Object>();
		final Namespace ns = request.getNamespace();
		final Element requestNode = request.getChild( "location", ns );

		if ( requestNode == null )
		{
			return map;
		}

		map.put( "EnvironmentVars", requestNode.getChildText( "environmentVars", ns ) );
		map.put( "EventId", requestId.toString() );
		map.put( "Id", UUID.randomUUID().toString() );
		map.put( "RequestData", requestNode.getChildText( "requestData", ns ) );
		map.put( "RequestUri", requestNode.getChildText( "requestUri", ns ) );
		map.put( "SessionData", requestNode.getChildText( "sessionData", ns ) );
		map.put( "SessionId", requestNode.getChildText( "sessionId", ns ) );

		return map;
	}

	/**
	 * TODO Method description for <code>purgeEventByLevel()</code>
	 * 
	 * @param request
	 * @return
	 */
	public Element purgeEventByLevel( final Element request )
	{
		final Map<String, Object> map = new HashMap<String, Object>();

		// Add to mapping
		map.put( "Level", request.getTextNormalize() );

		// Send to dao
		getDao().executeQuery( "PurgeEventByLevel", map );

		return null;
	}

	/**
	 * TODO Method description for <code>purgeEventByTimestamp()</code>
	 * 
	 * @param request
	 * @return
	 */
	public Element purgeEventByTimestamp( final Element request )
	{
		final Map<String, Object> map = new HashMap<String, Object>();
		final Namespace ns = request.getNamespace();
		final String timestampMin = request.getChildText( "timestampMin", ns );
		final String timestampMax = request.getChildText( "timestampMax", ns );

		// Add to mapping
		map.put( "TimestampMin", timestampMin != null ? timestampMin : "" ); // TODO
		map.put( "TimestampMax", timestampMax != null ? timestampMax : "" ); // TODO

		// Send to dao
		getDao().executeQuery( "PurgeEventByTimestamp", map );

		return null;
	}

	/**
	 * TODO Method description for <code>setDao()</code>
	 * 
	 * @param dao
	 */
	public void setDao( final BasicDao dao )
	{
		this.dao = dao;
	}

}
