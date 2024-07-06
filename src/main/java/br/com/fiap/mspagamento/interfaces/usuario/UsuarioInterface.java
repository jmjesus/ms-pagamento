package br.com.fiap.mspagamento.interfaces.usuario;


import br.com.fiap.mspagamento.interfaces.usuario.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-usuario", path = "/usuario")
public interface UsuarioInterface {

//    @GetMapping("/login")
//    UsuarioResponse login(@RequestBody UsuarioRequest usuarioRequest);

    @GetMapping("/login")
    UsuarioDTO findByLogin(@RequestBody String login);
}
