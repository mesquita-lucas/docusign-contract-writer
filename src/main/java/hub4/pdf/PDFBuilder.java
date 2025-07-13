package hub4.pdf;
import hub4.dto.ContractDTO;
import hub4.model.ContractContents;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.TextPosition;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class PDFBuilder{
    private static final Map<String, String> contract = new LinkedHashMap<>();

    private PDFBuilder() {}

    public static void build(JSONObject json) {
        Objects.requireNonNull(json, "Contract content json cannot be null");

        ContractContents content = loadContentFrom(json);
    }

    private static ContractContents loadContentFrom(JSONObject json){
        return new ContractContents(
                json.get("contracTitle").toString(),
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
