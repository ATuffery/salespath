package fr.iutrodez.salespath.account.dto;

import fr.iutrodez.salespath.account.model.SalesPerson;
import io.swagger.v3.oas.annotations.media.Schema;

public class SalesPersonUpdateRequest {

    @Schema(description = "Ancien mot de passe du commercial")
    private String oldPassword;

    @Schema(implementation = SalesPerson.class)
    private SalesPerson salesPerson;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public SalesPerson getSalesPerson() {
        return salesPerson;
    }

    public void setSalesPerson(SalesPerson salesPerson) {
        this.salesPerson = salesPerson;
    }
}
