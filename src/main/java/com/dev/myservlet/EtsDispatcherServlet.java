package com.dev.myservlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@SuppressWarnings("serial")
public class EtsDispatcherServlet extends DispatcherServlet {

	public EtsDispatcherServlet() {
		super();
	}

	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {

		super.doService(request, response);
	}

	public EtsDispatcherServlet(WebApplicationContext webApplicationContext) {
		super(webApplicationContext);
	}

}
