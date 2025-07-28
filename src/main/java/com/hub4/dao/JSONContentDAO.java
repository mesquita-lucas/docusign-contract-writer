package com.hub4.dao;

import com.hub4.dto.ContractDTO;
import com.hub4.json.JSONLoader;
import com.hub4.model.ContractContents;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class JSONContentDAO {
    private JSONContentDAO() {}

    public static ContractContents get() throws IOException {
        ContractDTO dto = MockContractDAO.get();

        JSONObject json = loadContractContents(dto);
        ContractContents contractContents = dtoFrom(json);

        return contractContents;
    }

    private static JSONObject loadContractContents(ContractDTO dto) {
        JSONObject json = JSONLoader.loadJson("template.json");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String consignorID = json.get("consignorID").toString();
        String productDetails = json.get("productDetails").toString();
        String signatureDate = json.get("signatures").toString();

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

        Map<String, String> signatureDatePlaceholders = Map.of(
                "{{data}}", LocalDate.now().format(formatter).toString()
        );

        json.replace("consignorID", replacePlaceholders(consignorID, consignorIDPlaceholders));
        json.replace("productDetails", replacePlaceholders(productDetails, productDetailsPlaceholders));
        json.replace("signatures", replacePlaceholders(signatureDate, signatureDatePlaceholders));

        return json;
    }

    private static String replacePlaceholders(String text, Map<String, String> placeholders) {
        for(Map.Entry<String, String> placeholder : placeholders.entrySet()) {
            text = text.replace(placeholder.getKey(), placeholder.getValue());
        }

        return text;
    }

    private static ContractContents dtoFrom(JSONObject json){
        return new ContractContents(
                json.get("contractTitle").toString(),
                json.get("consignorID").toString(),
                json.get("consigneeID").toString(),
                json.get("contractObject").toString(),
                json.get("productDetails").toString(),
                json.get("documentation").toString(),
                json.get("transport").toString(),
                json.get("truthfulnessDeclaration").toString(),
                json.get("nonApparentDamageDisclaimer").toString(),
                json.get("exclusiveSale").toString(),
                json.get("insurance").toString(),
                json.get("generalProvisions").toString(),
                json.get("signatures").toString(),
                json.get("annex").toString()
        );
    }
}
