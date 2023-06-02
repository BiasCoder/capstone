package academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.controller;

import academy.doku.da3duawebserviceapi.common.dto.BaseResponse;
import academy.doku.da3duawebserviceapi.common.utils.CheckRoleUtil;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.dto.PaymentMethodResponse;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.dto.UpdatePaymentMethodStatusRequest;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.service.UpdatePaymentMethodStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class UpdatePaymentMethodStatusController {

    private final UpdatePaymentMethodStatusService updatePaymentMethodStatusService;

    @CrossOrigin(origins = "*",allowedHeaders = "*")
    @PatchMapping("payment-methods/{paymentMethodId}")
    public ResponseEntity<BaseResponse<PaymentMethodResponse>> updatePaymentMethodStatus(
            @PathVariable Integer paymentMethodId,
            @RequestBody UpdatePaymentMethodStatusRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CheckRoleUtil.checkSuperAdminOnlyAccess(authentication);

        return new ResponseEntity<>(BaseResponse.<PaymentMethodResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Payment method updated")
                .data(updatePaymentMethodStatusService.updateState(paymentMethodId, request.getActive()))
                .build(), HttpStatus.OK);
    }
}
