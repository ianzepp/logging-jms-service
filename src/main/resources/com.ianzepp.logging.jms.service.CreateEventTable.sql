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
CREATE TABLE "Event" (
  "Id" char(36) NOT NULL,
  "Host" varchar(255) NOT NULL,
  "Logger" varchar(255) NOT NULL,
  "Level" varchar(10) NOT NULL,
  "Message" text character set utf8 NOT NULL,
  "Timestamp" varchar(255) default NULL,
  "Thread" varchar(36) character set utf8 default NULL,
  "Project" varchar(255) character set utf8 default NULL,
  "Service" varchar(255) character set utf8 default NULL,
  "CorrelationId" varchar(255) default NULL,
  "MessageId" varchar(255) default NULL,
  PRIMARY KEY  USING BTREE ("Id")
) ENGINE=InnoDB DEFAULT CHARSET=ascii;
