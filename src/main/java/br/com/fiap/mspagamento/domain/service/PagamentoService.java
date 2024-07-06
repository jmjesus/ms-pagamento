package br.com.fiap.mspagamento.domain.service;

import br.com.fiap.mspagamento.application.controller.exceptions.NaoEncontradoException;
import br.com.fiap.mspagamento.application.controller.exceptions.StandardError;
import br.com.fiap.mspagamento.domain.enums.PagamentoEnum;
import br.com.fiap.mspagamento.domain.enums.StatusEnum;
import br.com.fiap.mspagamento.interfaces.carrinho.CarrinhoInterface;
import br.com.fiap.mspagamento.interfaces.carrinho.request.AtualizarCarrinhoDTO;
import br.com.fiap.mspagamento.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class PagamentoService {

    private final CarrinhoInterface carrinhoInterface;
    private final Utils utils;

    public PagamentoService(CarrinhoInterface carrinhoInterface, Utils utils) {
        this.carrinhoInterface = carrinhoInterface;
        this.utils = utils;
    }

    public Mono<String> retornaToken(String token) {
        return Mono.just(token);
    }

    public Mono<String> realizarPagamento(String idUsuario, PagamentoEnum formaPagamento) throws JsonProcessingException {
        return Mono.fromCallable(() -> {
                    try {
                        carrinhoInterface.obterCarrinho(idUsuario);
                    } catch (Exception e) {
                        if (e.getMessage().toLowerCase().contains("Nao Encontrado Exception".toLowerCase())) {
                            StandardError error = this.utils.getErrorMessageFeignIntegration(e.getMessage());
                            throw new NaoEncontradoException(error.getMessage());
                        }
                    }
                    return null;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .then(Mono.fromCallable(() -> {
                            try {
                                AtualizarCarrinhoDTO atualizarCarrinhoDTO = new AtualizarCarrinhoDTO(StatusEnum.PAGO, formaPagamento);
                                carrinhoInterface.atualizarCarrinho(idUsuario, atualizarCarrinhoDTO);
                                return "Pagamento realizado com sucesso!";
                            } catch (Exception e) {
                                throw new RuntimeException("Deu algum erro!");
                            }
                        })
                        .subscribeOn(Schedulers.boundedElastic()));
    }
}
