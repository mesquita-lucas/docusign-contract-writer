package hub4.model;

public record ContractContents(
        String contractTitle,
        String consignorID,
        String consigneeID,
        String contractObject,
        String productDetails,
        String documentation,
        String transport,
        String truthfulnessDeclaration,
        String nonApparentDamageDisclaimer,
        String exclusiveSale,
        String insurance,
        String generalProvisions,
        String signatures,
        String annex
) {
}
