package br.com.fiap.mspagamento.application.controller;

import br.com.fiap.mspagamento.application.controller.request.PagamentoRequestDTO;
import br.com.fiap.mspagamento.domain.security.TokenService;
import br.com.fiap.mspagamento.domain.service.PagamentoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final TokenService tokenService;
    private final PagamentoService pagamentoService;

    public PagamentoController(TokenService tokenService, PagamentoService pagamentoService) {
        this.tokenService = tokenService;
        this.pagamentoService = pagamentoService;
    }

    @GetMapping
    public Mono<String> healthCheck() {
        return Mono.just("OK");
    }

//    @GetMapping("/pagar")
//    public Mono<String> realizarPagamento(@RequestHeader("Authorization") String token) {
//        String tokenPreparado = token.replace("Bearer ", "");
//        String id = tokenService.validateToken(tokenPreparado);
//        return pagamentoService.retornaToken(id);
//    }

    @PostMapping("/pagar")
    public Mono<ResponseEntity<String>> realizarPagamento(@RequestHeader("Authorization") String token,
                                          @RequestBody PagamentoRequestDTO pagamentoRequestDTO) throws JsonProcessingException {
        String tokenPreparado = token.replace("Bearer ", "");
        String id = tokenService.validateToken(tokenPreparado);
        return pagamentoService.realizarPagamento(id, pagamentoRequestDTO.formaPagamento())
                .map(ResponseEntity::ok);
    }
}
