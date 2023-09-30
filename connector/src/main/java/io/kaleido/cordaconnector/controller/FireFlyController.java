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

package io.kaleido.cordaconnector.controller;

import io.kaleido.cordaconnector.exception.CordaConnectionException;
import io.kaleido.cordaconnector.model.common.*;
import io.kaleido.cordaconnector.model.request.ConnectorRequest;
import io.kaleido.cordaconnector.model.response.ConnectorResponse;
import io.kaleido.cordaconnector.service.FireFlyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class FireFlyController {
    private static final Logger logger = LoggerFactory.getLogger(FireFlyController.class);

    @Autowired
    private FireFlyService fireFlyService;

    @PostMapping("/broadcastBatch")
    public ConnectorResponse<String> broadcastBatch(@RequestBody ConnectorRequest<BroadcastBatchData> request) throws CordaConnectionException, InterruptedException, ExecutionException {
        String txHash = fireFlyService.broadcastBatch(request.getData());
        logger.info("Request({}), broadcastBatch (batchID:{}, groupID:{}, payloadRef:{}) creation transaction {} was successful.", request.getId(), request.getData().getBatchId(), request.getData().getGroupId(), request.getData().getPayloadRef(),  txHash);
        return new ConnectorResponse(txHash);
    }

    @PostMapping("/paymentRequest")
    public ConnectorResponse<String> paymentRequest(@RequestBody ConnectorRequest<PaymentRequestData> request) throws CordaConnectionException, InterruptedException, ExecutionException {
        String txHash = fireFlyService.paymentRequest(request.getData());
        logger.info("Request({}), broadcastBatch (batchID:{}, groupID:{}, payloadRef:{}) creation transaction {} was successful.", request.getId(),   txHash);
        return new ConnectorResponse(txHash);
    }

//    @PostMapping("/paymentAcceptence")
//    public ConnectorResponse<String> paymentAcceptence(@RequestBody ConnectorRequest<PaymentAcceptenceData> request) throws CordaConnectionException, InterruptedException, ExecutionException {
//        String txHash = fireFlyService.paymentAcceptence(request.getData());
//        logger.info("Request({}), broadcastBatch (batchID:{}, groupID:{}, payloadRef:{}) creation transaction {} was successful.", request.getId(),   txHash);
//        return new ConnectorResponse(txHash);
//    }

    @PostMapping("/paymentComplete")
    public ConnectorResponse<String> paymentAcceptenceBesu(@RequestBody PaymentAcceptenceBesuData request) throws CordaConnectionException, InterruptedException, ExecutionException {
        String txHash = fireFlyService.paymentAcceptenceBesu(request);
        logger.info("Request({}), broadcastBatch (batchID:{}, groupID:{}, payloadRef:{}) creation transaction {} was successful.",   txHash);
        return new ConnectorResponse(txHash);
    }

    @PostMapping("/loadRequest")
    public ConnectorResponse<String> loadRequest(@RequestBody ConnectorRequest<CashMovementRequestData> request) throws CordaConnectionException, InterruptedException, ExecutionException {
        String txHash = fireFlyService.loadRequest(request.getData());
        logger.info("Request({}), broadcastBatch (batchID:{}, groupID:{}, payloadRef:{}) creation transaction {} was successful.", request.getId(),  txHash);
        return new ConnectorResponse(txHash);
    }

//    @PostMapping("/loadAcceptence")
//    public ConnectorResponse<String> loadAcceptence(@RequestBody ConnectorRequest<CashMovementAcceptenceData> request) throws CordaConnectionException, InterruptedException, ExecutionException {
//        String txHash = fireFlyService.loadAcceptence(request.getData());
//        logger.info("Request({}), broadcastBatch (batchID:{}, groupID:{}, payloadRef:{}) creation transaction {} was successful.", request.getId(),  txHash);
//        return new ConnectorResponse(txHash);
//    }

    @PostMapping("/loadCompleted")
    public ConnectorResponse<String> loadAcceptenceBesu(@RequestBody LoadAcceptenceDataBesu request) throws CordaConnectionException, InterruptedException, ExecutionException {
        String txHash = fireFlyService.loadAcceptenceBesu(request);
        logger.info("Request({}), broadcastBatch (batchID:{}, groupID:{}, payloadRef:{}) creation transaction {} was successful.",  txHash);
        return new ConnectorResponse(txHash);
    }

    @PostMapping("/transferRequest")
    public ConnectorResponse<String> transferRequest(@RequestBody ConnectorRequest<CashMovementRequestData> request) throws CordaConnectionException, InterruptedException, ExecutionException {
        String txHash = fireFlyService.transferRequest(request.getData());
        logger.info("Request({}), broadcastBatch (batchID:{}, groupID:{}, payloadRef:{}) creation transaction {} was successful.", request.getId(),  txHash);
        return new ConnectorResponse(txHash);
    }

    @PostMapping("/transferAcceptence")
    public ConnectorResponse<String> transferAcceptence(@RequestBody ConnectorRequest<CashMovementAcceptenceData> request) throws CordaConnectionException, InterruptedException, ExecutionException {
        String txHash = fireFlyService.transferAcceptence(request.getData());
        logger.info("Request({}), broadcastBatch (batchID:{}, groupID:{}, payloadRef:{}) creation transaction {} was successful.", request.getId(),  txHash);
        return new ConnectorResponse(txHash);
    }

    @PostMapping("/unloadRequest")
    public ConnectorResponse<String> unloadRequest(@RequestBody ConnectorRequest<CashMovementRequestData> request) throws CordaConnectionException, InterruptedException, ExecutionException {
        String txHash = fireFlyService.unloadRequest(request.getData());
        logger.info("Request({}), broadcastBatch (batchID:{}, groupID:{}, payloadRef:{}) creation transaction {} was successful.", request.getId(),  txHash);
        return new ConnectorResponse(txHash);
    }

//    @PostMapping("/unloadAcceptence")
//    public ConnectorResponse<String> unloadAcceptence(@RequestBody ConnectorRequest<CashMovementAcceptenceData> request) throws CordaConnectionException, InterruptedException, ExecutionException {
//        String txHash = fireFlyService.unloadAcceptence(request.getData());
//        logger.info("Request({}), broadcastBatch (batchID:{}, groupID:{}, payloadRef:{}) creation transaction {} was successful.", request.getId(),  txHash);
//        return new ConnectorResponse(txHash);
//    }

    @PostMapping("/unloadCompleted")
    public ConnectorResponse<String> unloadAcceptenceBesu(@RequestBody UnloadAcceptenceDataBesu unloadAcceptenceDataBesu) throws CordaConnectionException, InterruptedException, ExecutionException {
        String txHash = fireFlyService.unloadAcceptenceBesu(unloadAcceptenceDataBesu);
        logger.info("Request({}), broadcastBatch (batchID:{}, groupID:{}, payloadRef:{}) creation transaction {} was successful.",   txHash);
        return new ConnectorResponse(txHash);
    }
}
