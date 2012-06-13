package restmock;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import restmock.request.FrontController;
import restmock.request.HttpMethod;
import restmock.request.Route;
import restmock.request.RouteManager;
import restmock.request.RouteRegister;

public class RestMock {

	private static Server server;

	public static RestMockResponse whenGet(String uri) {
		return new RouteRegister(RouteManager.getInstance(), new Route(HttpMethod.GET, uri));
	}

	public static RestMockResponse whenPost(String uri) {
		return new RouteRegister(RouteManager.getInstance(), new Route(HttpMethod.POST, uri));
	}

	public static void startServer() {
		startServer(8080);
	}

	public static void startServer(int port) {
		initContext(port);

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					server.start();
					server.join();
				} catch (Exception e) {
					throw new RuntimeException("Could not start the server!", e);
				}
			}
		};

		Thread thread = new Thread(runnable);
		thread.setDaemon(true);
		thread.start();
	}

	private static void initContext(int port) {
		server = new Server(port);
		ServletContextHandler context = new ServletContextHandler();

		context.setContextPath("/");
		context.setResourceBase(".");
		context.setClassLoader(Thread.currentThread().getContextClassLoader());
		context.addServlet(FrontController.class, "/");

		server.setHandler(context);
	}

	public static void stopServer() {
		try {
			server.stop();
			RouteManager.getInstance().clean();
		} catch (Exception e) {
			throw new RuntimeException("Could not stop the server!", e);
		}
	}

}
