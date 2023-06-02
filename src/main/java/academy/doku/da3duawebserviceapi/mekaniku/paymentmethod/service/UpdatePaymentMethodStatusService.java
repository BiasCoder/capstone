package academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.service;

import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.dto.PaymentMethodResponse;

public interface UpdatePaymentMethodStatusService {
    PaymentMethodResponse updateState(Integer paymentMethodId, Boolean stateRequest);
}
