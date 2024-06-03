package com.example.pfebackfinal.security;

import com.example.pfebackfinal.exception.ApplicationException;
import com.example.pfebackfinal.exception.ApplicationExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class AccessTokenFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final ObjectMapper objectMapper;
    private final UserDetailsService userDetailsService;
    private final ApplicationExceptionHandler applicationExceptionHandler;

    /**
     * Performs filtering of HTTP requests and responses.
     *
     * @param request     The HTTP servlet request to be filtered.
     * @param response    The HTTP servlet response to be filtered.
     * @param filterChain The filter chain to execute the next filter in the chain for this request and response.
     * @throws ServletException If an error occurs while processing the servlet request.
     * @throws IOException      If an error occurs while reading or writing the servlet request or response.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (StringUtils.hasText(authHeader)) {
                jwtService.validateTokenSignature(authHeader);
                final String username = jwtService.getClaim(authHeader, JWTService.USERNAME_CLAIM);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (userDetails != null) {
                    if (!userDetails.isEnabled()) {
                        log.error("User is disabled");
                        throw new ApplicationException("User is disabled");
                    }
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (ApplicationException exception) {
            ResponseEntity<ApplicationExceptionHandler.ErrorResponse> errorResponseResponseEntity = applicationExceptionHandler.handleApplicationException(exception);
            setHttpServletResponse(errorResponseResponseEntity, response);
        } catch (RuntimeException exception) {
            ResponseEntity<ApplicationExceptionHandler.ErrorResponse> errorResponseResponseEntity = applicationExceptionHandler.handleRuntimeException(exception);
            setHttpServletResponse(errorResponseResponseEntity, response);
        }
    }

    /**
     * Sets the HTTP servlet response based on the provided error response entity.
     *
     * @param errorResponseResponseEntity The response entity containing the error response.
     * @param httpServletResponse         The HTTP servlet response to be set.
     * @throws IOException If an error occurs while writing the response to the servlet output stream.
     */

    private void setHttpServletResponse(ResponseEntity<ApplicationExceptionHandler.ErrorResponse> errorResponseResponseEntity, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setStatus(errorResponseResponseEntity.getStatusCode().value());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String jsonStringValue = objectMapper.writeValueAsString(errorResponseResponseEntity.getBody());
        httpServletResponse.getWriter().write(jsonStringValue);
    }
}
