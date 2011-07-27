To run the test application:

mvn gwt:compile -Dtouchkit.version=2.0.0-SNAPSHOT
 * Compiles widgetset into target/vaadin-touchkit-agpl-3.0-2.0.0-SNAPSHOT
 
Run com.vaadin.addon.touchkit.TestServer as a java application (in debug mode)

To use GWT dev mode:
 * Edit pom.xml and change
  <extraJvmArgs>-Xmx512M -Xss1024k</extraJvmArgs>
  into
  <!--extraJvmArgs>-Xmx512M -Xss1024k -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000</extraJvmArgs -->
 * Start GWT dev mode using
  mvn gwt:run
 * Connect to it through remote debugging (in Eclipse debug as -> "Remote Java Application" -> localhost:8000)

A remote debug connection is not needed for updating CSS