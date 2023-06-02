package academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.service;

import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.dto.PaymentMethodResponse;

import java.util.List;

public interface GetAllPaymentMethodService {
    List<PaymentMethodResponse> getAllPaymentMethods();
}
