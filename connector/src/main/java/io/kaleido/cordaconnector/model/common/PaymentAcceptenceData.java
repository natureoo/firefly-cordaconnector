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

public class PaymentAcceptenceData {
//    private String batchId;
//
//    private String groupId;

    private String linearId;
    private String settlementBankRef;
    private String settlementDate;

    public PaymentAcceptenceData() {
    }

//    public String getBatchId() {
//        return batchId;
//    }
//
//    public void setBatchId(String batchId) {
//        this.batchId = batchId;
//    }
//
//    public String getGroupId() {
//        return groupId;
//    }
//
//    public void setGroupId(String groupId) {
//        this.groupId = groupId;
//    }

    public String getLinearId() {
        return linearId;
    }

    public void setLinearId(String linearId) {
        this.linearId = linearId;
    }

    public String getSettlementBankRef() {
        return settlementBankRef;
    }

    public void setSettlementBankRef(String settlementBankRef) {
        this.settlementBankRef = settlementBankRef;
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
    }

    @Override
    public String toString() {
        return "PaymentAcceptenceData{" +
//                "batchId='" + batchId + '\'' +
//                ", groupId='" + groupId + '\'' +
                ", linearId='" + linearId + '\'' +
                ", settlementBankRef='" + settlementBankRef + '\'' +
                ", settlementDate=" + settlementDate +
                '}';
    }
}
