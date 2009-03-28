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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BasicDaoTest
{

	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URI = "jdbc:mysql://localhost:3306/logging_tests";
	private static final String DB_USERNAME = "logging_tests";
	private static final String DB_PASSWORD = "logging_tests";
	private static DataSource dataSource;
	private static HashMap <String, String> namedQueries;

	/**
	 * TODO Method description for <code>setUpBeforeClass()</code>
	 * 
	 * @throws SQLException
	 */
	@BeforeClass
	public static void setUpBeforeClass () throws SQLException
	{
		// Create the dao mapping
		namedQueries = new HashMap <String, String> ();
		namedQueries.put ("FindEventById", "src/main/resources/com.ianzepp.logging.jms.service.FindEventById.sql");
		namedQueries.put ("InsertEvent", "src/main/resources/com.ianzepp.logging.jms.service.InsertEvent.sql");
		namedQueries.put ("InsertException", "src/main/resources/com.ianzepp.logging.jms.service.InsertEventException.sql");
		namedQueries.put ("InsertLocation", "src/main/resources/com.ianzepp.logging.jms.service.InsertEventLocation.sql");
		namedQueries.put ("InsertUserRequest", "src/main/resources/com.ianzepp.logging.jms.service.InsertEventUserRequest.sql");

		// Create and save the datasource
		BasicDataSource basicDataSource = new BasicDataSource ();
		basicDataSource.setDriverClassName (DB_DRIVER);
		basicDataSource.setUrl (DB_URI);
		basicDataSource.setUsername (DB_USERNAME);
		basicDataSource.setPassword (DB_PASSWORD);
		dataSource = basicDataSource;
	}

	/**
	 * TODO Method description for <code>setUp()</code>
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp () throws Exception
	{
		// Create the tables
		Statement statement = getConnection ().createStatement ();
		statement.execute ("DROP TABLE IF EXISTS \"EventException\"");
		statement.execute ("DROP TABLE IF EXISTS \"EventLocation\"");
		statement.execute ("DROP TABLE IF EXISTS \"EventUserRequest\"");
		statement.execute ("DROP TABLE IF EXISTS \"Event\"");
		statement.execute (readFile ("src/main/resources/com.ianzepp.logging.jms.service.CreateEventTable.sql"));
		statement.execute (readFile ("src/main/resources/com.ianzepp.logging.jms.service.CreateEventExceptionTable.sql"));
		statement.execute (readFile ("src/main/resources/com.ianzepp.logging.jms.service.CreateEventLocationTable.sql"));
		statement.execute (readFile ("src/main/resources/com.ianzepp.logging.jms.service.CreateEventUserRequestTable.sql"));
	}

	private static Connection getConnection () throws SQLException
	{
		return dataSource.getConnection ();
	}

	/**
	 * TODO Method description for <code>newInstance()</code>
	 * 
	 * @return
	 */
	public BasicDao newInstance ()
	{
		return new BasicDao ();
	}

	/**
	 * TODO Method description for <code>newInitializedInstance()</code>
	 * 
	 * @return
	 */
	public BasicDao newInitializedInstance ()
	{
		BasicDao basicDao = newInstance ();
		basicDao.setDataSource (dataSource);
		basicDao.setNamedQueries (namedQueries);
		return basicDao;
	}

	/**
	 * TODO Method description for <code>testGetNamedQueries()</code>
	 */
	@Test
	public final void testGetNamedQueries ()
	{
		BasicDao basicDao = newInstance ();
		basicDao.setNamedQueries (namedQueries);
		assertNotNull (basicDao.getNamedQueries ());
	}

	/**
	 * TODO Method description for <code>testGetQuery()</code>
	 */
	@Test
	public final void testGetQuery ()
	{
		BasicDao basicDao = newInstance ();
		basicDao.setNamedQueries (namedQueries);

		for (String queryName : namedQueries.keySet ())
		{
			String filePath = namedQueries.get (queryName);
			String fileData = readFile (filePath);
			assertEquals (fileData, basicDao.getQuery (queryName));
		}
	}

	/**
	 * TODO Method description for <code>newInitializedEventMap()</code>
	 * 
	 * @param eventId
	 * @return
	 */
	public final HashMap <String, Object> newInitializedEventMap (UUID eventId)
	{
		HashMap <String, Object> hashMap = new HashMap <String, Object> ();
		hashMap.put ("CorrelationId", "correlationId");
		hashMap.put ("Host", "host");
		hashMap.put ("Id", eventId.toString ());
		hashMap.put ("Level", "level");
		hashMap.put ("Logger", "logger");
		hashMap.put ("Message", "message");
		hashMap.put ("MessageId", "messageId");
		hashMap.put ("Project", "project");
		hashMap.put ("Service", "service");
		hashMap.put ("Thread", "thread");
		hashMap.put ("Timestamp", "timestamp");
		return hashMap;
	}

	/**
	 * TODO Method description for <code>testSaveEvent()</code>
	 * 
	 * @throws Exception
	 */
	@Test
	public final void testSaveEvent () throws Exception
	{
		// Save the id first
		HashMap <String, Object> paramMap = newInitializedEventMap (UUID.randomUUID ());

		// Run the save
		assertTrue (newInitializedInstance ().executeQuery ("InsertEvent", paramMap) > 0);

		// Test the result
		String statementSql = "SELECT * FROM \"Event\" WHERE \"Id\" = '" + paramMap.get ("Id") + "'";
		Statement statement = getConnection ().createStatement ();

		// Check the query
		assertTrue (statement.execute (statementSql));

		// Check the result set
		ResultSet resultSet = statement.getResultSet ();

		assertTrue ("No result set data was returned.", resultSet.first ());

		for (String columnName : paramMap.keySet ())
		{
			assertEquals (paramMap.get (columnName), resultSet.getString (columnName));
		}

		assertFalse ("Too many results were returned", resultSet.next ());
	}

	/**
	 * TODO Method description for <code>testSaveException()</code>
	 */
	@Test
	@Ignore
	public final void testSaveException ()
	{
		fail ("Not yet implemented");
	}

	/**
	 * TODO Method description for <code>testSaveLocation()</code>
	 */
	@Test
	@Ignore
	public final void testSaveLocation ()
	{
		fail ("Not yet implemented");
	}

	/**
	 * TODO Method description for <code>testSaveUserRequest()</code>
	 */
	@Test
	@Ignore
	public final void testSaveUserRequest ()
	{
		fail ("Not yet implemented");
	}

	/**
	 * TODO Method description for <code>readTableMigration()</code>
	 * 
	 * @param tableName
	 * @return
	 */
	private static String readFile (String filePath)
	{
		StringBuffer fileData = new StringBuffer (4096);

		try
		{
			FileReader reader = new FileReader (filePath);
			char [] charBuffer = new char [2048];
			int readSize = -1;

			while ((readSize = reader.read (charBuffer)) >= 0)
			{
				fileData.append (charBuffer, 0, readSize);
			}

			reader.close ();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace ();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace ();
		}

		return fileData.toString ();
	}
}
