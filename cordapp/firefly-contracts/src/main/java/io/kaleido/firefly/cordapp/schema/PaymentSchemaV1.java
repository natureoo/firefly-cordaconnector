//
//// PaymentSchemaV1.java
//package io.kaleido.firefly.cordapp.schema;
//
//import io.kaleido.firefly.cordapp.metadata.PaymentStatus;
//import kotlin.collections.CollectionsKt;
//import kotlin.jvm.internal.Intrinsics;
//import net.corda.core.schemas.MappedSchema;
//import net.corda.core.schemas.PersistentState;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Table;
//import java.math.BigDecimal;
//import java.time.Instant;
//import java.util.Currency;
//import java.util.UUID;
//
//
// class PaymentSchema{
//
//}
//
//public final class PaymentSchemaV1 extends MappedSchema {
//
//    public static final PaymentSchemaV1 INSTANCE;
//
//    public PaymentSchemaV1() {
//        super(PaymentSchema.class, 1, (Iterable)CollectionsKt.listOf(PersistentPayment.class));
//    }
//
//    static {
//        PaymentSchemaV1 var0 = new PaymentSchemaV1();
//        INSTANCE = var0;
//    }
//
//    @Entity
//    @Table(
//            name = "payment_states"
//    )
//    public static final class PersistentPayment extends PersistentState {
//        @Column(
//                name = "settlement_bank"
//        )
//
//        private String settlementBankName;
//        @Column(
//                name = "payer"
//        )
//
//        private String payerName;
//        @Column(
//                name = "payee"
//        )
//
//        private String payeeName;
//        @Column(
//                name = "from_account_number"
//        )
//
//        private String fromAccountNum;
//        @Column(
//                name = "from_account_name"
//        )
//
//        private String fromAccountName;
//        @Column(
//                name = "to_account_number"
//        )
//
//        private String toAccountNum;
//        @Column(
//                name = "to_account_name"
//        )
//
//        private String toAccountName;
//        @Column(
//                name = "customer_ref_number"
//        )
//
//        private String customerRefNumber;
//        @Column(
//                name = "remittance_info"
//        )
//
//        private String remittanceInformation;
//        @Column(
//                name = "purpose_code"
//        )
//
//        private String purposeCode;
//        @Column(
//                name = "ultimate_dbtr_name"
//        )
//
//        private String ultimateDbtrName;
//        @Column(
//                name = "ultimate_cdtr_name"
//        )
//
//        private String ultimateCdtrName;
//        @Column(
//                name = "instructed_mv_unit"
//        )
//
//        private BigDecimal instructedMVUnit;
//        @Column(
//                name = "instructed_mv_currency"
//        )
//
//        private Currency instructedMVCurrency;
//        @Column(
//                name = "settlement_bank_ref"
//        )
//
//        private String settlementBankRef;
//        @Column(
//                name = "settlement_date"
//        )
//
//        private Instant settlementDate;
//        @Column(
//                name = "value_date"
//        )
//
//        private Instant valueDate;
//        @Column(
//                name = "reject_code"
//        )
//
//        private String rejectCode;
//        @Column(
//                name = "reject_reason"
//        )
//
//        private String rejectReason;
//        @Column(
//                name = "reject_date"
//        )
//
//        private Instant rejectDate;
//        @Column(
//                name = "entry_date"
//        )
//
//        private Instant entryDate;
//        @Column(
//                name = "bank_info"
//        )
//
//        private String bankInformation;
//        @Column(
//                name = "payment_type"
//        )
//
//        private String paymentType;
//        @Column(
//                name = "status"
//        )
//
//        private PaymentStatus status;
//        @Column(
//                name = "linear_id"
//        )
//
//        private UUID linearId;
//
//
//        public final String getSettlementBankName() {
//            return this.settlementBankName;
//        }
//
//        public final void setSettlementBankName( String var1) {
//            Intrinsics.checkNotNullParameter(var1, "<set-?>");
//            this.settlementBankName = var1;
//        }
//
//
//        public final String getPayerName() {
//            return this.payerName;
//        }
//
//        public final void setPayerName( String var1) {
//            Intrinsics.checkNotNullParameter(var1, "<set-?>");
//            this.payerName = var1;
//        }
//
//
//        public final String getPayeeName() {
//            return this.payeeName;
//        }
//
//        public final void setPayeeName( String var1) {
//            Intrinsics.checkNotNullParameter(var1, "<set-?>");
//            this.payeeName = var1;
//        }
//
//
//        public final String getFromAccountNum() {
//            return this.fromAccountNum;
//        }
//
//        public final void setFromAccountNum( String var1) {
//            Intrinsics.checkNotNullParameter(var1, "<set-?>");
//            this.fromAccountNum = var1;
//        }
//
//
//        public final String getFromAccountName() {
//            return this.fromAccountName;
//        }
//
//        public final void setFromAccountName( String var1) {
//            Intrinsics.checkNotNullParameter(var1, "<set-?>");
//            this.fromAccountName = var1;
//        }
//
//
//        public final String getToAccountNum() {
//            return this.toAccountNum;
//        }
//
//        public final void setToAccountNum( String var1) {
//            Intrinsics.checkNotNullParameter(var1, "<set-?>");
//            this.toAccountNum = var1;
//        }
//
//
//        public final String getToAccountName() {
//            return this.toAccountName;
//        }
//
//        public final void setToAccountName( String var1) {
//            Intrinsics.checkNotNullParameter(var1, "<set-?>");
//            this.toAccountName = var1;
//        }
//
//
//        public final String getCustomerRefNumber() {
//            return this.customerRefNumber;
//        }
//
//        public final void setCustomerRefNumber( String var1) {
//            Intrinsics.checkNotNullParameter(var1, "<set-?>");
//            this.customerRefNumber = var1;
//        }
//
//
//        public final String getRemittanceInformation() {
//            return this.remittanceInformation;
//        }
//
//        public final void setRemittanceInformation( String var1) {
//            this.remittanceInformation = var1;
//        }
//
//
//        public final String getPurposeCode() {
//            return this.purposeCode;
//        }
//
//        public final void setPurposeCode( String var1) {
//            this.purposeCode = var1;
//        }
//
//
//        public final String getUltimateDbtrName() {
//            return this.ultimateDbtrName;
//        }
//
//        public final void setUltimateDbtrName( String var1) {
//            this.ultimateDbtrName = var1;
//        }
//
//
//        public final String getUltimateCdtrName() {
//            return this.ultimateCdtrName;
//        }
//
//        public final void setUltimateCdtrName( String var1) {
//            this.ultimateCdtrName = var1;
//        }
//
//
//        public final BigDecimal getInstructedMVUnit() {
//            return this.instructedMVUnit;
//        }
//
//        public final void setInstructedMVUnit( BigDecimal var1) {
//            Intrinsics.checkNotNullParameter(var1, "<set-?>");
//            this.instructedMVUnit = var1;
//        }
//
//
//        public final Currency getInstructedMVCurrency() {
//            return this.instructedMVCurrency;
//        }
//
//        public final void setInstructedMVCurrency( Currency var1) {
//            this.instructedMVCurrency = var1;
//        }
//
//
//        public final String getSettlementBankRef() {
//            return this.settlementBankRef;
//        }
//
//        public final void setSettlementBankRef( String var1) {
//            this.settlementBankRef = var1;
//        }
//
//
//        public final Instant getSettlementDate() {
//            return this.settlementDate;
//        }
//
//        public final void setSettlementDate( Instant var1) {
//            this.settlementDate = var1;
//        }
//
//
//        public final Instant getValueDate() {
//            return this.valueDate;
//        }
//
//        public final void setValueDate( Instant var1) {
//            this.valueDate = var1;
//        }
//
//
//        public final String getRejectCode() {
//            return this.rejectCode;
//        }
//
//        public final void setRejectCode( String var1) {
//            this.rejectCode = var1;
//        }
//
//
//        public final String getRejectReason() {
//            return this.rejectReason;
//        }
//
//        public final void setRejectReason( String var1) {
//            this.rejectReason = var1;
//        }
//
//
//        public final Instant getRejectDate() {
//            return this.rejectDate;
//        }
//
//        public final void setRejectDate( Instant var1) {
//            this.rejectDate = var1;
//        }
//
//
//        public final Instant getEntryDate() {
//            return this.entryDate;
//        }
//
//        public final void setEntryDate( Instant var1) {
//            this.entryDate = var1;
//        }
//
//
//        public final String getBankInformation() {
//            return this.bankInformation;
//        }
//
//        public final void setBankInformation( String var1) {
//            this.bankInformation = var1;
//        }
//
//
//        public final String getPaymentType() {
//            return this.paymentType;
//        }
//
//        public final void setPaymentType( String var1) {
//            this.paymentType = var1;
//        }
//
//
//        public final PaymentStatus getStatus() {
//            return this.status;
//        }
//
//        public final void setStatus( PaymentStatus var1) {
//            Intrinsics.checkNotNullParameter(var1, "<set-?>");
//            this.status = var1;
//        }
//
//
//        public final UUID getLinearId() {
//            return this.linearId;
//        }
//
//        public final void setLinearId( UUID var1) {
//            Intrinsics.checkNotNullParameter(var1, "<set-?>");
//            this.linearId = var1;
//        }
//
//        public PersistentPayment(){
//
//        }
//
//        public PersistentPayment( String settlementBankName,  String payerName,  String payeeName,  String fromAccountNum,  String fromAccountName,  String toAccountNum,  String toAccountName,  String customerRefNumber,  String remittanceInformation,  String purposeCode,  String ultimateDbtrName,  String ultimateCdtrName,  BigDecimal instructedMVUnit,  Currency instructedMVCurrency,  String settlementBankRef,  Instant settlementDate,  Instant valueDate,  String rejectCode,  String rejectReason,  Instant rejectDate,  Instant entryDate,  String bankInformation,  String paymentType,  PaymentStatus status,  UUID linearId) {
//            this.settlementBankName = settlementBankName;
//            this.payerName = payerName;
//            this.payeeName = payeeName;
//            this.fromAccountNum = fromAccountNum;
//            this.fromAccountName = fromAccountName;
//            this.toAccountNum = toAccountNum;
//            this.toAccountName = toAccountName;
//            this.customerRefNumber = customerRefNumber;
//            this.remittanceInformation = remittanceInformation;
//            this.purposeCode = purposeCode;
//            this.ultimateDbtrName = ultimateDbtrName;
//            this.ultimateCdtrName = ultimateCdtrName;
//            this.instructedMVUnit = instructedMVUnit;
//            this.instructedMVCurrency = instructedMVCurrency;
//            this.settlementBankRef = settlementBankRef;
//            this.settlementDate = settlementDate;
//            this.valueDate = valueDate;
//            this.rejectCode = rejectCode;
//            this.rejectReason = rejectReason;
//            this.rejectDate = rejectDate;
//            this.entryDate = entryDate;
//            this.bankInformation = bankInformation;
//            this.paymentType = paymentType;
//            this.status = status;
//            this.linearId = linearId;
//        }
//
//
//    }
//}
