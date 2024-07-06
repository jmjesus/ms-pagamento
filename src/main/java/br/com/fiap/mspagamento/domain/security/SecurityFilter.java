package br.com.fiap.mspagamento.domain.security;

import br.com.fiap.mspagamento.interfaces.usuario.UsuarioInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@EnableAsync
public class SecurityFilter implements WebFilter {
    private final TokenService tokenService;
    private final UsuarioInterface usuarioInterface;

    public SecurityFilter(TokenService tokenService, @Qualifier("usuarioInterfaceMock") UsuarioInterface usuarioInterface) {
        this.tokenService = tokenService;
        this.usuarioInterface = usuarioInterface;
    }

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith("Bearer "))
                .map(authHeader -> authHeader.substring(7))
                .flatMap(token -> {
                    String login = tokenService.validateToken(token);
                    return Mono.fromCallable(() -> usuarioInterface.findByLogin(login))
                            .subscribeOn(Schedulers.boundedElastic())
                            .flatMap(user -> Mono.justOrEmpty(user).map(usuario -> {
                                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                                return usuario;
                            }));
                })
                .then(chain.filter(exchange));
    }

//    private String recoverToken(HttpServletRequest request){
//        var authHeader = request.getHeader("Authorization");
//        if(authHeader == null) return null;
//        return authHeader.replace("Bearer ", "");
//    }
}
