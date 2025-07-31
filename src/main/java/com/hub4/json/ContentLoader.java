package com.hub4.json;

import com.hub4.FormReceiverApplication;
import com.hub4.dto.ContractDTO;
import com.hub4.model.ContractContents;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ContentLoader {
    private final ContractContents contents;

    public ContentLoader(ContractDTO dto) {
        try (InputStream is = FormReceiverApplication.class.getClassLoader().getResourceAsStream("template.json"))
        {
            Objects.requireNonNull(is, "template not found");

            JSONParser parser = new JSONParser();
            JSONObject template = (JSONObject) parser.parse(new InputStreamReader(is));

            String consignorID = template.get("consignorID").toString();
            String productDetails = template.get("productDetails").toString();
            String signatureDate = template.get("signatures").toString();

            List<Map<String, String>> storedPlaceholders = storePlaceholders(dto);

            template.replace("consignorID", replacePlaceholders(consignorID, storedPlaceholders.get(0)));
            template.replace("productDetails", replacePlaceholders(productDetails, storedPlaceholders.get(1)));
            template.replace("signatureDate", replacePlaceholders(signatureDate, storedPlaceholders.get(2)));

            this.contents = dtoFrom(template);
        } catch (Exception e) {
            System.out.println("Unable to load template");

            throw new RuntimeException(e.getMessage());
        }
    }

    public ContractContents getContents() {
        return contents;
    }

    private String replacePlaceholders(String text, Map<String, String> placeholders) {
        for(Map.Entry<String, String> placeholder : placeholders.entrySet()) {
            text = text.replace(placeholder.getKey(), placeholder.getValue());
        }

        return text;
    }

    private List<Map<String, String>> storePlaceholders(ContractDTO dto) {
        List<Map<String, String>> list = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

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
                "{{data}}", LocalDate.now().format(formatter)
        );

        list.add(consignorIDPlaceholders);
        list.add(productDetailsPlaceholders);
        list.add(signatureDatePlaceholders);

        return list;
    }

    private ContractContents dtoFrom(JSONObject json){
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
