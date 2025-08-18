package com.hub4.domain.json;

import com.hub4.api.dto.ContractDTO;
import com.hub4.domain.model.ContractContents;
import com.hub4.domain.utils.CurrencyFormatter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ContentLoader {
    private final ContractContents contents;

    public ContentLoader(ContractDTO dto) {
        try (InputStream is = ContentLoader.class.getClassLoader().getResourceAsStream("template.json")) {
            Objects.requireNonNull(is, "template not found");

            JSONParser parser = new JSONParser();
            JSONObject template = (JSONObject) parser.parse(new InputStreamReader(is));

            Map<String, String> placeholders = buildPlaceholders(dto);
            for (Object key : template.keySet()) {
                Object value = template.get(key);

                if (value instanceof String strValue) {
                    String replaced = replacePlaceholders(strValue, placeholders);
                    template.put(key, replaced);
                }
            }

            this.contents = dtoFrom(template);
        } catch (Exception e) {
            System.out.println("Unable to load template");
            throw new RuntimeException("Erro ao carregar o template JSON", e);
        }
    }

    public ContractContents getContents() {
        return contents;
    }

    private String replacePlaceholders(String text, Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            text = text.replace(entry.getKey(), entry.getValue());
        }
        return text;
    }

    private Map<String, String> buildPlaceholders(ContractDTO dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("{{email_consignante}}", dto.consignorEmail());
        placeholders.put("{{nome_consignante}}", dto.consignorName());
        placeholders.put("{{cpf_consignante}}", dto.consignorCPF());
        placeholders.put("{{prod_tipo}}", dto.prodType());
        placeholders.put("{{prod_marca}}", dto.prodBrand());
        placeholders.put("{{prod_modelo}}", dto.prodModel());
        placeholders.put("{{prod_ano}}", dto.prodYear());
        placeholders.put("{{prod_estado_conservacao}}", dto.conservationState());
        placeholders.put("{{prod_acessorios}}", dto.prodAccessories());

        BigDecimal sellValue = dto.prodSellValue();
        String formattedValue = CurrencyFormatter.format(sellValue);

        placeholders.put("{{prod_valor_venda}}", formattedValue);

        placeholders.put("{{data}}", LocalDate.now().format(formatter));
        System.out.println("Placeholders gerados:");
        placeholders.forEach((k, v) -> System.out.println(k + " = " + v));

        return placeholders;
    }

    private ContractContents dtoFrom(JSONObject json) {
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