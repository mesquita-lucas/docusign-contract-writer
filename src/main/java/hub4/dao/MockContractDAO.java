package hub4.dao;

import hub4.dto.ContractDTO;

public class MockContractDAO{
    private MockContractDAO() {}

    public static ContractDTO get() {
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
                "R$12.990,00"
                );
    }
}
