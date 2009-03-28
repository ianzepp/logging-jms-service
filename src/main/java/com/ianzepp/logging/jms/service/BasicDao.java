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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * TODO Type description
 * 
 * @author izepp
 */
public class BasicDao
{

	private final Logger LOG = Logger.getLogger (getClass ().getName ());
	private NamedParameterJdbcTemplate jdbcTemplate;
	private final Map <String, String> namedQueries = new HashMap <String, String> ();

	/**
	 * TODO Method description for <code>getJdbcTemplate()</code>
	 * 
	 * @return
	 */
	public NamedParameterJdbcTemplate getJdbcTemplate ()
	{
		return jdbcTemplate;
	}

	/**
	 * TODO Method description for <code>getNamedQueries()</code>
	 * 
	 * @return
	 */
	public Map <String, String> getNamedQueries ()
	{
		return namedQueries;
	}

	/**
	 * TODO Method description for <code>getQuery()</code>
	 * 
	 * @param queryName
	 * @return
	 */
	public String getQuery (final String queryName)
	{
		return getNamedQueries ().get (queryName);
	}

	/**
	 * TODO Method description for <code>readFromFile()</code>
	 * 
	 * @param filePath
	 * @return
	 */
	public String readFromFile (String filePath)
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

	/**
	 * TODO Method description for <code>executeQuery()</code>
	 * 
	 * @param queryName
	 * @param paramMap
	 * @return
	 */
	public int executeQuery (final String queryName, final Map <String, Object> paramMap)
	{
		LOG.fine (queryName + "(" + paramMap + ")");

		if (paramMap != null && paramMap.size () > 0)
		{
			return getJdbcTemplate ().update (getQuery (queryName), paramMap);
		}
		else
		{
			return 0;
		}

	}

	/**
	 * TODO Method description for <code>setDataSource()</code>
	 * 
	 * @param dataSource
	 */
	public void setDataSource (final DataSource dataSource)
	{
		setJdbcTemplate (new NamedParameterJdbcTemplate (dataSource));
	}

	/**
	 * TODO Method description for <code>setJdbcTemplate()</code>
	 * 
	 * @param jdbcTemplate
	 */
	public void setJdbcTemplate (final NamedParameterJdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * TODO Method description for <code>setNamedQueries()</code>
	 * 
	 * @param namedQueries
	 */
	public void setNamedQueries (final Map <String, String> namedQueries)
	{
		this.namedQueries.clear ();

		LOG.fine ("setNamedQueries(): Adding '" + namedQueries.size () + "' named query items...");

		for (String queryName : namedQueries.keySet ())
		{
			String filePath = namedQueries.get (queryName);
			String fileData = readFromFile (filePath);
			this.namedQueries.put (queryName, fileData);

			LOG.fine ("setNamedQueries(): '" + queryName + "' => '" + filePath + "'");
			LOG.fine (fileData);
		}
	}

}
