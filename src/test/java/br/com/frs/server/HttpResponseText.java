package br.com.frs.server;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.junit.Before;
import org.junit.Test;

public class HttpResponseText {

	private Request baseRequest;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private PrintWriter printWriter;
	private ContextHandler context;
	private HttpResponse subject;

	@Before
	public void setUp() throws IOException {
		context = mock(ContextHandler.class);
		baseRequest = mock(Request.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		printWriter = mock(PrintWriter.class);

		when(response.getWriter()).thenReturn(printWriter);

		subject = new HttpResponse(context);
	}

	@Test
	public void testPlainTextResponse() throws Exception {
		subject.thenReturn("Hello World!").withType(MediaType.TEXT_PLAIN);
		subject.handle("", baseRequest, request, response);

		verify(baseRequest).setHandled(true);
		verify(response).setContentType("text/plain");
		verify(response).setStatus(HttpServletResponse.SC_OK);
		verify(response).getWriter();
		verify(printWriter).println("Hello World!");
	}

	@Test
	public void testHtmlResponse() throws Exception {
		subject.thenReturn("<h1>Mock rules</h1>").withType(MediaType.TEXT_HTML);
		subject.handle("", baseRequest, request, response);

		verify(response).setContentType("text/html");
		verify(printWriter).println("<h1>Mock rules</h1>");
	}
	
}