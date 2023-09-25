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

package io.kaleido.cordaconnector.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.kaleido.cordaconnector.exception.CordaConnectionException;
import io.kaleido.cordaconnector.model.common.*;
import io.kaleido.cordaconnector.rpc.NodeRPCClient;
import io.kaleido.firefly.cordapp.flows.CreateBroacastBatchFlow;
import io.kaleido.firefly.cordapp.flows.dvp.*;
import io.kaleido.firefly.cordapp.flows.payment.PaymentAcceptFlow;
import io.kaleido.firefly.cordapp.flows.payment.PaymentCreateFlow;
import io.kaleido.firefly.cordapp.metadata.*;
import io.kaleido.firefly.cordapp.util.StringUtil;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.messaging.FlowProgressHandle;
import net.corda.core.transactions.SignedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class FireFlyService {
    private static final Logger logger = LoggerFactory.getLogger(FireFlyService.class);
    @Autowired
    NodeRPCClient rpcClient;

    ObjectMapper mapper = new ObjectMapper();

    private final static String PURPOSE_CODE  = "purposeCode";
    private final static String PAYMENT_TYPE  = "paymentType";

    public FireFlyService() {
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    public Set<Party> validateAndGetParticipants(List<String> participants) throws CordaConnectionException {
        logger.debug("Validating participant list {}", participants);
        Set<Party> uniqueParticipants = new HashSet<>();
        for(String observer: participants) {
            Party party = rpcClient.getRpcProxy().wellKnownPartyFromX500Name(CordaX500Name.parse(observer));
            if(party == null) {
                logger.error("No party with name {} found", observer);
                throw new RuntimeException("No party with name "+observer+" found.");
            }
            uniqueParticipants.add(party);
        }
        logger.debug("Valid unique participant set {}", uniqueParticipants);
        return uniqueParticipants;
    }


    public String broadcastBatch(BroadcastBatchData batchData) throws CordaConnectionException, ExecutionException, InterruptedException {
        Set<Party> uniqueParticipants = validateAndGetParticipants(batchData.getObservers());
        Party me = rpcClient.getRpcProxy().nodeInfo().getLegalIdentities().get(0);
        uniqueParticipants.add(me);
        List<Party> otherObservers = uniqueParticipants.stream().filter(party -> !party.equals(me)).collect(Collectors.toList());
        SignedTransaction signedTransaction = rpcClient.getRpcProxy().startTrackedFlowDynamic(CreateBroacastBatchFlow.class, batchData.getBatchId(), batchData.getPayloadRef(), otherObservers, batchData.getGroupId()).getReturnValue().get();
        return signedTransaction.getId().toString();
    }

    public String paymentRequest(PaymentRequestData paymentRequestData) throws CordaConnectionException, ExecutionException, InterruptedException {
//        Set<Party> uniqueParticipants = validateAndGetParticipants(paymentRequestData.getObservers());
        Party me = rpcClient.getRpcProxy().nodeInfo().getLegalIdentities().get(0);
        Set<Party> uniqueParticipants = new HashSet<>();
        uniqueParticipants.add(me);
        List<Party> otherObservers = uniqueParticipants.stream().filter(party -> !party.equals(me)).collect(Collectors.toList());

//        UUID groupId = UUID.fromString(paymentRequestData.getGroupId());
        Party settlementBankParty = getParty(paymentRequestData.getSettlementBank());
        Participant payeeParticipant = null;
        Participant payerParticipant = new Participant(getParty(paymentRequestData.getPayer()), "", "", paymentRequestData.getDbtrBicfi(), null, null);
        if(StringUtil.isNotNullAndEmpty(paymentRequestData.getPayee()))
            payeeParticipant = new Participant(getParty(paymentRequestData.getPayee()), "", "", paymentRequestData.getCdtrBicfi(), null, null);
        AccountInfo payerAccountInfo = new AccountInfo(paymentRequestData.getFromAccount(),paymentRequestData.getFromAccount(), null, null, paymentRequestData.getDbtrBicfi(), paymentRequestData.getFromAccount());
        AccountInfo payeeAccountInfo = new AccountInfo(paymentRequestData.getToAccount(), paymentRequestData.getToAccount(), null, null, paymentRequestData.getCdtrBicfi(), paymentRequestData.getToAccount());
        String customerRefNumber = paymentRequestData.getCustomerRefNumber();
        BigDecimal instructedMVUnit = BigDecimal.valueOf( Long.valueOf(paymentRequestData.getAmount()));// instructedMVUnit
        Currency currency = Currency.getInstance(paymentRequestData.getCurrencyCode());
        ChargeBearer chargeBearer = ChargeBearer.valueOf(paymentRequestData.getChargeBearer());
        RemittanceInformation remittanceInformation = new RemittanceInformation();
        BankInformation bankInformation = new BankInformation();
        try {
            String[] strings = paymentRequestData.getRemittanceInfo().split(",");
            for(String str: strings)
                remittanceInformation.getNarratives().add(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PurposeCode purposeCode = new PurposeCode(PURPOSE_CODE);
        Instant valueDate = Instant.now();
        String paymentType = PAYMENT_TYPE;
        SignedTransaction signedTransaction = null;

        if(payeeParticipant == null) {
            signedTransaction = rpcClient.getRpcProxy().startTrackedFlowDynamic(PaymentCreateFlow.PaymentRequest.class,
                    settlementBankParty, payerParticipant,
                    payerAccountInfo, payeeAccountInfo, customerRefNumber, instructedMVUnit, currency, chargeBearer,
                    remittanceInformation, purposeCode, valueDate, bankInformation, paymentType).getReturnValue().get();
        }
        else
            signedTransaction = rpcClient.getRpcProxy().startTrackedFlowDynamic(PaymentCreateFlow.PaymentRequest.class,
                    settlementBankParty, payerParticipant,
                    payeeParticipant, payerAccountInfo, payeeAccountInfo, customerRefNumber, instructedMVUnit, currency,
                    chargeBearer, remittanceInformation, purposeCode, valueDate, bankInformation, paymentType).getReturnValue().get();

        return signedTransaction.getId().toString();
    }



    public String paymentAcceptence(PaymentAcceptenceData paymentAcceptenceData) throws CordaConnectionException, ExecutionException, InterruptedException {
//        UUID groupId = UUID.fromString(paymentAcceptenceData.getGroupId());
        UniqueIdentifier identifier = UniqueIdentifier.Companion.fromString(paymentAcceptenceData.getLinearId());
        Instant settlementDate = Instant.parse(paymentAcceptenceData.getSettlementDate());
        SignedTransaction signedTransaction = rpcClient.getRpcProxy().startTrackedFlowDynamic(PaymentAcceptFlow.PaymentAcceptance.class,  identifier, paymentAcceptenceData.getSettlementBankRef(), settlementDate).getReturnValue().get();
        return signedTransaction.getId().toString();
    }

    public String loadRequest(CashMovementRequestData cashMovementRequestData) throws CordaConnectionException, ExecutionException, InterruptedException {
//        Set<Party> uniqueParticipants = validateAndGetParticipants(paymentRequestData.getObservers());
        Party me = rpcClient.getRpcProxy().nodeInfo().getLegalIdentities().get(0);
        Set<Party> uniqueParticipants = new HashSet<>();
        uniqueParticipants.add(me);
        List<Party> otherObservers = uniqueParticipants.stream().filter(party -> !party.equals(me)).collect(Collectors.toList());

        Party settlementBankParty = getParty(cashMovementRequestData.getSettlementBank());
        Participant payerParticipant = new Participant(getParty(cashMovementRequestData.getPayer()), "", "", cashMovementRequestData.getDbtrBicfi(), null, null);
        Participant payeeParticipant = new Participant(getParty(cashMovementRequestData.getPayee()), "", "", cashMovementRequestData.getCdtrBicfi(), null, null);
        AccountInfo payerAccountInfo = new AccountInfo(cashMovementRequestData.getFromAccount(),cashMovementRequestData.getFromAccount(), null, null, cashMovementRequestData.getDbtrBicfi(), cashMovementRequestData.getFromAccount());
        AccountInfo payeeAccountInfo = new AccountInfo(cashMovementRequestData.getToAccount(), cashMovementRequestData.getToAccount(), null, null, cashMovementRequestData.getCdtrBicfi(), cashMovementRequestData.getToAccount());
        String customerRefNumber = cashMovementRequestData.getCustomerRefNumber();
        BigDecimal instructedMVUnit = BigDecimal.valueOf( Long.valueOf(cashMovementRequestData.getAmount()));// instructedMVUnit
        Currency currency = Currency.getInstance(cashMovementRequestData.getCurrencyCode());

        SignedTransaction signedTransaction = rpcClient.getRpcProxy().startTrackedFlowDynamic(LoadRequestFlow.LoadRequest.class,
                settlementBankParty, payerParticipant,payeeParticipant,
                payerAccountInfo, payeeAccountInfo, customerRefNumber, instructedMVUnit, currency).getReturnValue().get();

        return signedTransaction.getId().toString();
    }

    public String loadAcceptence(CashMovementAcceptenceData cashMovementAcceptenceData) throws CordaConnectionException, ExecutionException, InterruptedException {
        UniqueIdentifier identifier = UniqueIdentifier.Companion.fromString(cashMovementAcceptenceData.getLinearId());
        Instant settlementDate = Instant.parse(cashMovementAcceptenceData.getSettlementDate());
        SignedTransaction signedTransaction = rpcClient.getRpcProxy().startTrackedFlowDynamic(LoadAcceptFlow.LoadAcceptance.class,  identifier, cashMovementAcceptenceData.getSettlementBankRef(), settlementDate).getReturnValue().get();
        return signedTransaction.getId().toString();
    }

    public String transferRequest(CashMovementRequestData cashMovementRequestData) throws CordaConnectionException, ExecutionException, InterruptedException {
//        Set<Party> uniqueParticipants = validateAndGetParticipants(paymentRequestData.getObservers());
        Party me = rpcClient.getRpcProxy().nodeInfo().getLegalIdentities().get(0);
        Set<Party> uniqueParticipants = new HashSet<>();
        uniqueParticipants.add(me);
        List<Party> otherObservers = uniqueParticipants.stream().filter(party -> !party.equals(me)).collect(Collectors.toList());

        Party settlementBankParty = getParty(cashMovementRequestData.getSettlementBank());
        Participant payerParticipant = new Participant(getParty(cashMovementRequestData.getPayer()), "", "", cashMovementRequestData.getDbtrBicfi(), null, null);
        Participant payeeParticipant = new Participant(getParty(cashMovementRequestData.getPayee()), "", "", cashMovementRequestData.getCdtrBicfi(), null, null);
        AccountInfo payerAccountInfo = new AccountInfo(cashMovementRequestData.getFromAccount(),cashMovementRequestData.getFromAccount(), null, null, cashMovementRequestData.getDbtrBicfi(), cashMovementRequestData.getFromAccount());
        AccountInfo payeeAccountInfo = new AccountInfo(cashMovementRequestData.getToAccount(), cashMovementRequestData.getToAccount(), null, null, cashMovementRequestData.getCdtrBicfi(), cashMovementRequestData.getToAccount());
        String customerRefNumber = cashMovementRequestData.getCustomerRefNumber();
        BigDecimal instructedMVUnit = BigDecimal.valueOf( Long.valueOf(cashMovementRequestData.getAmount()));// instructedMVUnit
        Currency currency = Currency.getInstance(cashMovementRequestData.getCurrencyCode());

        SignedTransaction signedTransaction = rpcClient.getRpcProxy().startTrackedFlowDynamic(TransferRequestFlow.TransferRequest.class,
                settlementBankParty, payerParticipant,payeeParticipant,
                payerAccountInfo, payeeAccountInfo, customerRefNumber, instructedMVUnit, currency).getReturnValue().get();

        return signedTransaction.getId().toString();
    }

    public String transferAcceptence(CashMovementAcceptenceData cashMovementAcceptenceData) throws CordaConnectionException, ExecutionException, InterruptedException {
        UniqueIdentifier identifier = UniqueIdentifier.Companion.fromString(cashMovementAcceptenceData.getLinearId());
        Instant settlementDate = Instant.parse(cashMovementAcceptenceData.getSettlementDate());
        SignedTransaction signedTransaction = rpcClient.getRpcProxy().startTrackedFlowDynamic(TransferAcceptFlow.TransferAcceptance.class,  identifier, cashMovementAcceptenceData.getSettlementBankRef(), settlementDate).getReturnValue().get();
        return signedTransaction.getId().toString();
    }


    public String unloadRequest(CashMovementRequestData cashMovementRequestData) throws CordaConnectionException, ExecutionException, InterruptedException {
//        Set<Party> uniqueParticipants = validateAndGetParticipants(paymentRequestData.getObservers());
        Party me = rpcClient.getRpcProxy().nodeInfo().getLegalIdentities().get(0);
        Set<Party> uniqueParticipants = new HashSet<>();
        uniqueParticipants.add(me);
        List<Party> otherObservers = uniqueParticipants.stream().filter(party -> !party.equals(me)).collect(Collectors.toList());

        Party settlementBankParty = getParty(cashMovementRequestData.getSettlementBank());
        Participant payerParticipant = new Participant(getParty(cashMovementRequestData.getPayer()), "", "", cashMovementRequestData.getDbtrBicfi(), null, null);
        Participant payeeParticipant = new Participant(getParty(cashMovementRequestData.getPayee()), "", "", cashMovementRequestData.getCdtrBicfi(), null, null);
        AccountInfo payerAccountInfo = new AccountInfo(cashMovementRequestData.getFromAccount(),cashMovementRequestData.getFromAccount(), null, null, cashMovementRequestData.getDbtrBicfi(), cashMovementRequestData.getFromAccount());
        AccountInfo payeeAccountInfo = new AccountInfo(cashMovementRequestData.getToAccount(), cashMovementRequestData.getToAccount(), null, null, cashMovementRequestData.getCdtrBicfi(), cashMovementRequestData.getToAccount());
        String customerRefNumber = cashMovementRequestData.getCustomerRefNumber();
        BigDecimal instructedMVUnit = BigDecimal.valueOf( Long.valueOf(cashMovementRequestData.getAmount()));// instructedMVUnit
        Currency currency = Currency.getInstance(cashMovementRequestData.getCurrencyCode());

        SignedTransaction signedTransaction = rpcClient.getRpcProxy().startTrackedFlowDynamic(UnloadRequestFlow.UnloadRequest.class,
                settlementBankParty, payerParticipant,payeeParticipant,
                payerAccountInfo, payeeAccountInfo, customerRefNumber, instructedMVUnit, currency).getReturnValue().get();

        return signedTransaction.getId().toString();
    }

    public String unloadAcceptence(CashMovementAcceptenceData cashMovementAcceptenceData) throws CordaConnectionException, ExecutionException, InterruptedException {
        UniqueIdentifier identifier = UniqueIdentifier.Companion.fromString(cashMovementAcceptenceData.getLinearId());
        Instant settlementDate = Instant.parse(cashMovementAcceptenceData.getSettlementDate());
        SignedTransaction signedTransaction = rpcClient.getRpcProxy().startTrackedFlowDynamic(UnloadAcceptFlow.UnloadAcceptance.class,  identifier, cashMovementAcceptenceData.getSettlementBankRef(), settlementDate).getReturnValue().get();
        return signedTransaction.getId().toString();
    }


    public Party getParty(String partyName){
        Party party = null;
        try {
            party = rpcClient.getRpcProxy().wellKnownPartyFromX500Name(CordaX500Name.parse(partyName));
        } catch (CordaConnectionException e) {
            e.printStackTrace();
        }
        return party;
    }
}
