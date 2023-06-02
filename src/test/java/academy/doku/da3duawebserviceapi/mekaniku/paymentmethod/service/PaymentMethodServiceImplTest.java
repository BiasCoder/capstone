package academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.service;

import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.dto.PaymentMethodResponse;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.dto.UpdatePaymentMethodStatusRequest;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.entity.PaymentCategory;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.entity.PaymentMethodEntity;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.exception.PaymentMethodNotFoundException;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.repository.PaymentMethodRepository;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.service.impl.PaymentMethodServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentMethodServiceImplTest {
    @InjectMocks
    private PaymentMethodServiceImpl paymentMethodService;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @Mock
    private PaymentMethodRepository paymentMethodRepository;


    @Test
    void get_payment_method_list() {

        List<PaymentMethodEntity> expectedData = new ArrayList<>();

        expectedData.add(new PaymentMethodEntity(1, "VIRTUAL_ACCOUNT_BCA", "BCA", PaymentCategory.VIRTUAL_ACCOUNT, true, "https://mekaniku-storage.s3-ap-southeast-1.amazonaws.com/payment-method/bca.png",  LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10), LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10)));
        expectedData.add(new PaymentMethodEntity(2, "VIRTUAL_ACCOUNT_CIMB", "CIMB Niaga", PaymentCategory.VIRTUAL_ACCOUNT, true, "https://mekaniku-storage.s3-ap-southeast-1.amazonaws.com/payment-method/cimbniaga.png", LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10), LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10)));
        expectedData.add(new PaymentMethodEntity(3, "EMONEY_OVO", "OVO", PaymentCategory.EMONEY, true, "https://mekaniku-storage.s3-ap-southeast-1.amazonaws.com/payment-method/ovo.png", LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10), LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10)));

        when(paymentMethodRepository.findAllByOrderByIdAsc()).thenReturn(expectedData);

        List<PaymentMethodResponse> res = paymentMethodService.getAllPaymentMethods();

        // Verify method call
        verify(paymentMethodRepository, times(1)).findAllByOrderByIdAsc();

        // Assert ID
        assertEquals(expectedData.get(0).getId(), res.get(0).getId());
        assertEquals(expectedData.get(1).getId(), res.get(1).getId());
        assertEquals(expectedData.get(2).getId(), res.get(2).getId());

        // Assert Name
        assertEquals(expectedData.get(0).getName(), res.get(0).getName());
        assertEquals(expectedData.get(1).getName(), res.get(1).getName());
        assertEquals(expectedData.get(2).getName(), res.get(2).getName());

        // Assert Description
        assertEquals(expectedData.get(0).getDescription(), res.get(0).getDescription());
        assertEquals(expectedData.get(1).getDescription(), res.get(1).getDescription());
        assertEquals(expectedData.get(2).getDescription(), res.get(2).getDescription());
    }

    @Test
    void update_payment_method_state_success() {
        Integer reqId = 1;

        UpdatePaymentMethodStatusRequest request = new UpdatePaymentMethodStatusRequest(false);
        PaymentMethodEntity oldPaymentMethod = new PaymentMethodEntity(1, "VIRTUAL_ACCOUNT_BCA", "BCA", PaymentCategory.VIRTUAL_ACCOUNT, true, "https://mekaniku-storage.s3-ap-southeast-1.amazonaws.com/payment-method/bca.png",  LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10), LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10));

        when(paymentMethodRepository.findById(1)).thenReturn(Optional.of(oldPaymentMethod));
        when(paymentMethodRepository.save(any(PaymentMethodEntity.class))).thenReturn(oldPaymentMethod);

        PaymentMethodResponse updatedPaymentMethod = paymentMethodService.updateState(reqId, request.getActive());

        // Verify method call
        verify(paymentMethodRepository, times(1)).findById(reqId);
        verify(paymentMethodRepository, times(1)).save(any(PaymentMethodEntity.class));

        // Check updatedPaymentMethod not null
        assertNotNull(updatedPaymentMethod.getName());
        assertNotNull(updatedPaymentMethod.getDescription());
        assertNotNull(updatedPaymentMethod.getCategory());

        // check if payment method status is disabled/enabled (active: true/false)
        assertEquals(request.getActive(), updatedPaymentMethod.getActive());
    }


    @Test
    void update_payment_method_state_invalid_id() {
        Integer paymentMethodId = 100;

        UpdatePaymentMethodStatusRequest request = new UpdatePaymentMethodStatusRequest(false);

        assertThrows(PaymentMethodNotFoundException.class, () -> {
            paymentMethodService.updateState(paymentMethodId, request.getActive());
        }, "PaymentMethodNotFoundException was expected");
    }

}