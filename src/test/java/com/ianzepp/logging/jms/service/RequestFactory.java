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

import org.jdom.Element;
import org.jdom.Namespace;

public abstract class RequestFactory
{
	public static Namespace getNamespace ()
	{
		return Namespace.getNamespace ("log", "http://ianzepp.com/logging");
	}

	/**
	 * 
	 * TODO Method description for <code>newSimplex()</code>
	 * 
	 * @return
	 */
	public static Element newSimplex ()
	{
		Namespace ns = getNamespace ();

		Element element = new Element ("eventRequest", ns);
		element.addContent (new Element ("host", ns).setText ("host"));
		element.addContent (new Element ("logger", ns).setText ("logger"));
		element.addContent (new Element ("level", ns).setText ("level"));
		element.addContent (new Element ("message", ns).setText ("message"));

		// Done
		return element;
	}

	/**
	 * 
	 * TODO Method description for <code>newSimplexWithOptionals()</code>
	 * 
	 * @return
	 */
	public static Element newSimplexWithOptionals ()
	{
		Namespace ns = getNamespace ();
		Element element = newSimplex ();

		// Add optional top-level elements
		element.addContent (new Element ("timestamp", ns).setText ("timestamp"));
		element.addContent (new Element ("thread", ns).setText ("thread"));
		element.addContent (new Element ("project", ns).setText ("project"));
		element.addContent (new Element ("service", ns).setText ("service"));
		element.addContent (new Element ("correlationId", ns).setText ("correlationId"));
		element.addContent (new Element ("messageId", ns).setText ("messageId"));

		// Done
		return element;
	}
}
