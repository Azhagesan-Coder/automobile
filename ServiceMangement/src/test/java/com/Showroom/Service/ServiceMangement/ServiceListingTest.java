package com.Showroom.Service.ServiceMangement;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import com.Showroom.Service.ServiceMangement.Enitity.ServiceRecord;
import com.Showroom.Service.ServiceMangement.Repository.ServiceRepository;
import com.Showroom.Service.ServiceMangement.Service.ServiceImpl;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
class ServiceListingTest {
    @Test void listReturnsStoredAppointments() {
        ServiceRepository repository = mock(ServiceRepository.class);
        ServiceRecord record = new ServiceRecord(); record.setId(4L);
        when(repository.findAll()).thenReturn(List.of(record));
        ServiceImpl service = new ServiceImpl();
        ReflectionTestUtils.setField(service, "repositoryImpl", repository);
        assertEquals(4L, service.getAllServices().get(0).getId());
    }
}
