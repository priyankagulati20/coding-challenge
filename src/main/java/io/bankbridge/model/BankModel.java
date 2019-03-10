package io.bankbridge.model;

/**
 *
 * This model class contains the bank details.
 *
 * @author XXX
 * @version 1.0
 * @since 09-03-2019
 *
 */
public class BankModel {

    // Changed the access modifier of instance variable from public to private and
    // added getters and setter for accessing them. This will ensure encapsulation
    // in a class.
    private String bic;
    private String name;
    private String countryCode;
    private String auth;

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

}