package project_management.Project_Management.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

class JwtTokenValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Get the JWT string from the Authorization header
        String jwt = request.getHeader("Authorization");

        // Check if the Authorization header is present and starts with "Bearer"
        if (jwt != null && jwt.startsWith("Bearer ")) {
            // Extract the token from the header
            String jwtToken = jwt.substring(7);

            try {
                // Define the secret key (must match the key used when generating the token)
                SecretKey jwtSecretKey = Keys.hmacShaKeyFor("asdfghjklmnbghyujnbvcxsertyubcxbvf".getBytes());

                // Parse the JWT token and extract claims
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtSecretKey)
                        .build()
                        .parseClaimsJws(jwtToken) // <--- Use jwtToken here
                        .getBody();

                // Extract email and authorities from claims
                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));

                // Convert authorities string into a list of GrantedAuthority objects
                List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                // Create authentication token with email and authorities
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, grantedAuthorities);

                // Set authentication in security context
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // Handle any token parsing or validation errors
                throw new BadCredentialsException("Invalid token", e);
            }
        }

        // Continue with the filter chain if the token is valid or not present
        chain.doFilter(request, response);
    }
}
