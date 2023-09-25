// Copyright © 2021 Kaleido, Inc.
//
// SPDX-License-Identifier: Apache-2.0
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package io.kaleido.cordaconnector.model.common;

public class CashMovementRequestData {
    private String settlementBank;
    private String payer;
    private String payee;
    private String fromAccount;
    private String toAccount;
    private String customerRefNumber;
    private String amount;
    private String currencyCode;
//    private String chargeBearer;
//    private String remittanceInfo;
//    private String purposeCode;
//    private Instant valueDate;
//    private String bankInformation;
//    private String paymentType;
//
//    private String batchId;
//
//    private String groupId;

    private String dbtrBicfi ;
    private String cdtrBicfi;

    public CashMovementRequestData() {
    }

    public String getSettlementBank() {
        return settlementBank;
    }

    public void setSettlementBank(String settlementBank) {
        this.settlementBank = settlementBank;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getCustomerRefNumber() {
        return customerRefNumber;
    }

    public void setCustomerRefNumber(String customerRefNumber) {
        this.customerRefNumber = customerRefNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    //    public String getPurposeCode() {
//        return purposeCode;
//    }
//
//    public void setPurposeCode(String purposeCode) {
//        this.purposeCode = purposeCode;
//    }
//
//    public Instant getValueDate() {
//        return valueDate;
//    }
//
//    public void setValueDate(Instant valueDate) {
//        this.valueDate = valueDate;
//    }
//
//    public String getBankInformation() {
//        return bankInformation;
//    }
//
//    public void setBankInformation(String bankInformation) {
//        this.bankInformation = bankInformation;
//    }


    public String getDbtrBicfi() {
        return dbtrBicfi;
    }

    public void setDbtrBicfi(String dbtrBicfi) {
        this.dbtrBicfi = dbtrBicfi;
    }

    public String getCdtrBicfi() {
        return cdtrBicfi;
    }

    public void setCdtrBicfi(String cdtrBicfi) {
        this.cdtrBicfi = cdtrBicfi;
    }

    @Override
    public String toString() {
        return "CashMovementRequestData{" +
                "settlementBank='" + settlementBank + '\'' +
                ", payer='" + payer + '\'' +
                ", payee='" + payee + '\'' +
                ", fromAccount='" + fromAccount + '\'' +
                ", toAccount='" + toAccount + '\'' +
                ", customerRefNumber='" + customerRefNumber + '\'' +
                ", amount='" + amount + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", dbtrBicfi='" + dbtrBicfi + '\'' +
                ", cdtrBicfi='" + cdtrBicfi + '\'' +
                '}';
    }
}
