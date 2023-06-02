package academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.service.impl;

import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.repository.PaymentMethodRepository;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.dto.PaymentMethodResponse;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.entity.PaymentMethodEntity;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.exception.PaymentMethodNotFoundException;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.service.GetAllPaymentMethodService;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.service.UpdatePaymentMethodStatusService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements GetAllPaymentMethodService, UpdatePaymentMethodStatusService {

    private final ModelMapper modelMapper;
    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    @Cacheable(value = "payment-method")
    public List<PaymentMethodResponse> getAllPaymentMethods() {
        List<PaymentMethodEntity> paymentMethodList = paymentMethodRepository.findAllByOrderByIdAsc();
        return paymentMethodList.stream().map(method -> modelMapper.map(method, PaymentMethodResponse.class)).toList();
    }

    @Override
    @CacheEvict(value = "payment-method", allEntries = true)
    public PaymentMethodResponse updateState(Integer paymentMethodId, Boolean stateRequest) {
        PaymentMethodEntity paymentMethod = paymentMethodRepository.findById(paymentMethodId).orElseThrow(
                () -> new PaymentMethodNotFoundException(
                        String.format("Payment method with ID: %s is not found.", paymentMethodId)
                )

        );
        paymentMethod.setActive(stateRequest);
        paymentMethodRepository.save(paymentMethod);

        return modelMapper.map(paymentMethod, PaymentMethodResponse.class);
    }
}
