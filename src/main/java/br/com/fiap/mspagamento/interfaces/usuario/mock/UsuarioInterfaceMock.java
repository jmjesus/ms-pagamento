package br.com.fiap.mspagamento.interfaces.usuario.mock;

import br.com.fiap.mspagamento.interfaces.usuario.UsuarioInterface;
import br.com.fiap.mspagamento.interfaces.usuario.dto.UsuarioDTO;
import br.com.fiap.mspagamento.interfaces.usuario.enums.UsuarioRole;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioInterfaceMock implements UsuarioInterface {

    @Override
    public UsuarioDTO findByLogin(String login) {
        if(login.equals("gui@matos.com")) {
            return new UsuarioDTO(
                    "1",
                    login,
                    "$2y$10$aFoZefH/3.Ju3pF9RP5/UuDFXkpYZdJ2b3Sjps0IrreUMjYhrEKnu",
                    UsuarioRole.ADMIN
            );
        }

        return new UsuarioDTO(
                "2",
                login,
                "USER",
                UsuarioRole.USER
        );
    }
}
