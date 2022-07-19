package filters;

import dataaccess.UserDB;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.User;

public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();
        String email = (String)session.getAttribute("email");
        
        // if email is null or user didn't entered the email
        if( email == null ){
            httpResponse.sendRedirect("login");
            return;
        }
        
        // get the user object from the database using UserDB class in dataaccess layer
        UserDB userDB = new UserDB();
        User user = userDB.get(email);
        
        // check user's role, if it is other than Admin (Role id is 1) redirect it to notes page
        if(user.getRole().getRoleId() != 1){   
            httpResponse.sendRedirect("notes");
            return;
        }
        
        // if user's role is admin, load the admin servlet
        chain.doFilter(request, response);
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
