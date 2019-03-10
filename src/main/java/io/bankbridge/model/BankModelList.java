package io.bankbridge.model;

import java.util.List;

/**
 *
 * This is pojo class for list of bank models.
 *
 * @author XXX
 * @version 1.0
 * @since 09-03-2019
 *
 */
public class BankModelList {

    // Changed the access modifier of instance variable from public to private and
    // added getters and setter for accessing them. This will ensure encapsulation
    // in a class.
    private List<BankModel> banks;

    public BankModelList() {

    }

    public BankModelList(List<BankModel> bankModels) {
        super();
        this.banks = bankModels;
    }

    public List<BankModel> getBanks() {
        return banks;
    }

}