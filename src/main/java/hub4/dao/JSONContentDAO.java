package hub4.dao;

import hub4.dto.ContractDTO;
import hub4.json.JSONLoader;
import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.util.Map;

public class JSONContentDAO {
    private JSONContentDAO() {}

    public static JSONObject get() throws FileNotFoundException {
        ContractDTO dto = MockContractDAO.get();
        JSONObject json = JSONLoader.loadJson("contract-content.json");

        if(json == null){
            throw new FileNotFoundException();
        }

        String consignorID = json.get("consignorID").toString();
        String productDetails = json.get("productDetails").toString();

        Map<String, String> consignorIDPlaceholders = Map.of(
                "{{email_consignante}}", dto.consignorEmail(),
                "{{nome_consignante}}", dto.consignorName(),
                "{{cpf_consignante}}", dto.consignorCPF()
        );

        Map<String, String> productDetailsPlaceholders = Map.of(
                "{{prod_tipo}}", dto.prodType(),
                "{{prod_marca}}", dto.prodBrand(),
                "{{prod_modelo}}", dto.prodModel(),
                "{{prod_ano}}", dto.prodYear(),
                "{{prod_estado_conservacao}}", dto.conservationState(),
                "{{prod_acessorios}}", dto.prodAccessories(),
                "{{prod_valor_venda}}", dto.prodSellValue()
        );

        json.replace(consignorID, replacePlaceholders(consignorID, consignorIDPlaceholders));
        json.replace(productDetails, replacePlaceholders(productDetails, productDetailsPlaceholders));
        replacePlaceholders(productDetails, productDetailsPlaceholders);

        return json;
    }

    private static String replacePlaceholders(String text, Map<String, String> placeholders) {
        for(Map.Entry<String, String> placeholder : placeholders.entrySet()) {
            text = text.replace(placeholder.getKey(), placeholder.getValue());
        }

        return text;
    }
}
