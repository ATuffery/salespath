package fr.iutrodez.salespath.config;

import fr.iutrodez.salespath.repository.IAccountRepository;
import fr.iutrodez.salespath.service.AccountService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    @Autowired
    private ObjectProvider<AccountService> accountServiceProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Récupérer l'API Key depuis les en-têtes
        String apiKey = request.getHeader("X-API-KEY");

        // Vérifier si l'API key est nécessaire pour cette requête
        String path = request.getRequestURI();
        if (!"/account/login".equals(path) && !"/account/add".equals(path)) { // Si ce n'est pas la route de login
            if (apiKey == null || apiKey.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Missing API Key");
                return;
            }

            // Vérifier que la clé existe dans la base de données
            boolean isValid = accountServiceProvider.getIfAvailable().existsByApiKey(apiKey);
            if (!isValid) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Invalid API Key");
                return;
            }

            // Définir un Authentication dans le SecurityContextHolder
            PreAuthenticatedAuthenticationToken authentication =
                    new PreAuthenticatedAuthenticationToken(apiKey, null, AuthorityUtils.NO_AUTHORITIES);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continuer la chaîne de filtres
        filterChain.doFilter(request, response);
    }
}
