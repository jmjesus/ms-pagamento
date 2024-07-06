package br.com.fiap.mspagamento.interfaces.carrinho.valueobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record ItemCarrinhoResponse(
        String id,
        Integer quantidade,
        Double preco,
        String nome
) {
}
