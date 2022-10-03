package launch;

import java.io.File;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.descriptor.web.ErrorPage;

import jakarta.servlet.http.HttpServletResponse;

public class EmbeddedPersonServer {
    public static void main(String... args) throws LifecycleException {
        String webappsDirLocation = "src/main/webapp";
        int port = 8000;

        try{
            if (args.length > 0 ){
                port = Integer.valueOf(args[0]);
            }
        } catch (Exception e){
            System.out.format("Invalid port number '%s' %n", args[0]);
            System.exit(-1);
        }
        // Init server
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.valueOf(port));

        StandardContext ctx = (StandardContext) tomcat.addWebapp("", 
                                                                 new File(webappsDirLocation).getAbsolutePath());
        WebResourceRoot root = new StandardRoot(ctx);

        // Map WEB-INF/classes 
        var additionWebInfClasses = new File("target/classes");
        
        root.addPreResources(new DirResourceSet(root, "/WEB-INF/classes",
                                  additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(root);

        // Map Home pages
        var additionHomePages = new File("src/main/webapp/WEB-INF/home");
        root.addPreResources(new DirResourceSet(root, "/",
                                                additionHomePages.getAbsolutePath(), "/"));
        ctx.setResources(root);

        // Map WEB-INF/pages
        // File additionWebPages = new File("target/resources/pages");
        // root.addPreResources(new DirResourceSet(root, "/WEB-INF/pages",
        //                                         additionWebPages.getAbsolutePath(), "/"));
        // ctx.setResources(root);
        
        // // Map WEB-INF/data 
        // File additionWebData = new File("target/resources/data");
        // root.addPreResources(new DirResourceSet(root, "/WEB-INF/data",
        //                                         additionWebData.getAbsolutePath(), "/"));
        // ctx.setResources(root);
        
        // // Map WEB-INF/props 
        // File additionWebProps = new File("target/resources/props");
        // root.addPreResources(new DirResourceSet(root, "/WEB-INF/props",
        //                                         additionWebProps.getAbsolutePath(), "/"));
        // ctx.setResources(root);
        
        // Add Error page
        ErrorPage errorPage = new ErrorPage();
        errorPage.setErrorCode(HttpServletResponse.SC_NOT_FOUND);
        
        String errorLocation = "/WEB-INF/pages/404.html";
        errorPage.setLocation(errorLocation);
        ctx.addErrorPage(errorPage);
        
        // Start server
        tomcat.start();
        tomcat.getService().addConnector(tomcat.getConnector());

        tomcat.getServer().await();
    }
}
