package com.hub4.dao;

import com.hub4.dto.ContractDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

public class MockContractDAO{
    private MockContractDAO() {}

    public static ContractDTO get() throws IOException {
        InputStream is = MockContractDAO.class.getClassLoader().getResourceAsStream("HUB4-preset-logos-15.jpg");
        assert is != null;
        String encodedLogo = Base64.getEncoder().encodeToString(is.readAllBytes());

        return new ContractDTO(
                "Lucas de Mesquita",
                "448.347.458-80",
                "lucas.m.barros1954@gmail.com",
                "Bike",
                "Cannondale",
                "SuperSix Evo",
                "2019",
                "Conservado",
                "",
                "R$12.990,00",
                encodedLogo,
                "application/jpeg",
                "imagem"
                );
    }
}
