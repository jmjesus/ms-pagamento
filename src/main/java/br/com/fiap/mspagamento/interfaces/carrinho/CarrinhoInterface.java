package br.com.fiap.mspagamento.interfaces.carrinho;

import br.com.fiap.mspagamento.interfaces.carrinho.request.AtualizarCarrinhoDTO;
import br.com.fiap.mspagamento.interfaces.carrinho.response.CarrinhoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-carrinho-compras", path="/carrinho")
public interface CarrinhoInterface {

    @GetMapping
    CarrinhoResponse obterCarrinho(@RequestHeader("Authorization") String token);

    @PutMapping
    CarrinhoResponse atualizarCarrinho(@RequestHeader("Authorization") String token,
                                       @RequestBody AtualizarCarrinhoDTO atualizarCarrinhoDTO);
}
