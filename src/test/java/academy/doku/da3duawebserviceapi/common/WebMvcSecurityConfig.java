package academy.doku.da3duawebserviceapi.common;

import academy.doku.da3duawebserviceapi.mekaniku.user.repository.UserRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class WebMvcSecurityConfig {

    @MockBean
    UserRepository userRepository;
}
