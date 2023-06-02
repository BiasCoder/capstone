package academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.controller;

import academy.doku.da3duawebserviceapi.common.dto.BaseResponse;
import academy.doku.da3duawebserviceapi.common.utils.CheckRoleUtil;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.dto.PaymentMethodResponse;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.service.GetAllPaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class GetAllPaymentMethodController {
    private final GetAllPaymentMethodService getAllPaymentMethodService;

    @CrossOrigin(origins = "*",allowedHeaders = "*")
    @GetMapping("payment-methods")
    public ResponseEntity<BaseResponse<List<PaymentMethodResponse>>> getPaymentMethodList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CheckRoleUtil.checkSuperAdminOnlyAccess(authentication);

        return new ResponseEntity<>(
                BaseResponse.<List<PaymentMethodResponse>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Payment Methods List")
                        .data(getAllPaymentMethodService.getAllPaymentMethods())
                        .build(),
                HttpStatus.OK
        );
    }
}
