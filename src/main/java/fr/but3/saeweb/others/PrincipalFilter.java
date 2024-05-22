package fr.but3.saeweb.others;

import java.io.IOException;

import jakarta.annotation.Priority;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
/*import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;*/

@WebFilter(urlPatterns = {"/*"})
@Priority(1)
public class PrincipalFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
    }
    /*
    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        // Travail effectif du filtre
        // Loguer la requête, par exemple
        // Puis propagation de la requête le long de la chaîne
        
        String requestURI = req.getRequestURI();
        System.out.println("//////////////////////");
        System.out.println(requestURI);
        System.out.println("//////////////////////");
        if(!requestURI.equals("/login")){
            System.out.println("Mon filtre fonctionne");
            HttpSession session = req.getSession();
            Principal principal = (Principal) session.getAttribute("principal");
            System.out.println(principal);
            if(principal == null){
                res.sendRedirect(req.getContextPath()+"/login");
            }
            
        }else{
            System.out.println("Login page");
        }
        System.out.println("//////////////////////");
        filterChain.doFilter(req, res);
    }*/

    /*
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
                String requestURI = ((HttpServletRequest) req).getRequestURI();

        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse httpResponse = (HttpServletResponse) res;

        System.out.println("//////////////////////");

        System.out.println(httpRequest.getRequestURI());

        HttpSession session = ((HttpServletRequest) req).getSession();
        Principal principal = (Principal) session.getAttribute("principal");

        if(principal == null && !httpRequest.getRequestURI().equals("/login")){
            System.out.println("Mon filtre fonctionne");
            System.out.println(principal);
            if(principal == null){
                //((HttpServletResponse) res).sendRedirect(((HttpServletRequest) req).getContextPath() + "/login");
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            }
            
        }else if(httpRequest.getRequestURI().equals("/login")){
            System.out.println("Login page");
        }
        System.out.println("//////////////////////");
        filterChain.doFilter(req, res);

    }
    */
}
