package fr.iutrodez.salespath.account.dto;

import fr.iutrodez.salespath.account.model.SalesPerson;

public class SalesPersonUpdateRequest {

    private String oldPassword;
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
