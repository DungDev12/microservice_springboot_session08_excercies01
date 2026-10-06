package com.exam.appointmentservice.service;

import com.exam.appointmentservice.DoctorResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final RestTemplate restTemplate;

    @CircuitBreaker(
            name = "doctorServiceCB",
            fallbackMethod = "doctorServiceFallback"
    )
    public DoctorResponse checkDoctor(Long doctorId) {

        return restTemplate.getForObject(
                "http://DOCTOR-SERVICE/api/v1/doctors/" + doctorId,
                DoctorResponse.class
        );
    }

    public DoctorResponse doctorServiceFallback(
            Long doctorId,
            Throwable throwable
    ) {

        System.out.println(
                "Doctor-Service không khả dụng: "
                        + throwable.getMessage()
        );

        return null;
    }
}
