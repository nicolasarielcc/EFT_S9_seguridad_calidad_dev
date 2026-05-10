package com.duoc.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private AppointmentController appointmentController;

    @Test
    void createAppointmentShouldReturnBadRequestWhenPatientIdNull() {
        Appointment appointment = new Appointment();
        appointment.setPatientId(null);

        ResponseEntity<?> response = appointmentController.createAppointment(appointment);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createAppointmentShouldReturnBadRequestWhenPatientDoesNotExist() {
        Appointment appointment = new Appointment();
        appointment.setPatientId(10);
        when(patientRepository.existsById(10)).thenReturn(false);

        ResponseEntity<?> response = appointmentController.createAppointment(appointment);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Patient not found for patientId: 10", response.getBody());
    }

    @Test
    void createAppointmentShouldReturnCreatedWhenPatientExists() {
        Appointment appointment = new Appointment();
        appointment.setPatientId(10);
        when(patientRepository.existsById(10)).thenReturn(true);

        ResponseEntity<?> response = appointmentController.createAppointment(appointment);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(appointment, response.getBody());
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void createAppointmentShouldReturnBadRequestWhenSaveFails() {
        Appointment appointment = new Appointment();
        appointment.setPatientId(10);
        when(patientRepository.existsById(10)).thenReturn(true);
        doThrow(new RuntimeException("DB down")).when(appointmentRepository).save(appointment);

        ResponseEntity<?> response = appointmentController.createAppointment(appointment);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getAllAppointmentsShouldReturnOkWhenRepositorySucceeds() {
        List<Appointment> appointments = List.of(new Appointment(), new Appointment());
        when(appointmentRepository.findAll()).thenReturn(appointments);

        ResponseEntity<Iterable<Appointment>> response = appointmentController.getAllAppointments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(appointments, response.getBody());
    }

    @Test
    void getAllAppointmentsShouldReturnInternalServerErrorWhenRepositoryFails() {
        when(appointmentRepository.findAll()).thenThrow(new RuntimeException("DB down"));

        ResponseEntity<Iterable<Appointment>> response = appointmentController.getAllAppointments();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAppointmentByIdShouldReturnOkWhenFound() {
        Appointment appointment = new Appointment();
        appointment.setId(5);
        when(appointmentRepository.findById(5)).thenReturn(Optional.of(appointment));

        ResponseEntity<Appointment> response = appointmentController.getAppointmentById(5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(appointment, response.getBody());
    }

    @Test
    void getAppointmentByIdShouldReturnNotFoundWhenMissing() {
        when(appointmentRepository.findById(5)).thenReturn(Optional.empty());

        ResponseEntity<Appointment> response = appointmentController.getAppointmentById(5);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAppointmentByIdShouldReturnInternalServerErrorWhenRepositoryFails() {
        when(appointmentRepository.findById(5)).thenThrow(new RuntimeException("DB down"));

        ResponseEntity<Appointment> response = appointmentController.getAppointmentById(5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAppointmentsForPatientShouldReturnNotFoundWhenPatientMissing() {
        when(patientRepository.existsById(10)).thenReturn(false);

        ResponseEntity<List<Appointment>> response = appointmentController.getAppointmentsForPatient(10);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAppointmentsForPatientShouldReturnOkWhenPatientExists() {
        List<Appointment> appointments = List.of(new Appointment());
        when(patientRepository.existsById(10)).thenReturn(true);
        when(appointmentRepository.findByPatientId(10)).thenReturn(appointments);

        ResponseEntity<List<Appointment>> response = appointmentController.getAppointmentsForPatient(10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(appointments, response.getBody());
    }

    @Test
    void getAppointmentsForPatientShouldReturnInternalServerErrorWhenRepositoryFails() {
        when(patientRepository.existsById(10)).thenReturn(true);
        when(appointmentRepository.findByPatientId(10)).thenThrow(new RuntimeException("DB down"));

        ResponseEntity<List<Appointment>> response = appointmentController.getAppointmentsForPatient(10);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void updateAppointmentShouldReturnNotFoundWhenAppointmentMissing() {
        when(appointmentRepository.existsById(1)).thenReturn(false);

        ResponseEntity<String> response = appointmentController.updateAppointment(1, new Appointment());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateAppointmentShouldReturnBadRequestWhenPatientMissing() {
        when(appointmentRepository.existsById(1)).thenReturn(true);
        Appointment appointment = new Appointment();
        appointment.setPatientId(10);
        when(patientRepository.existsById(10)).thenReturn(false);

        ResponseEntity<String> response = appointmentController.updateAppointment(1, appointment);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Patient not found for patientId: 10", response.getBody());
    }

    @Test
    void updateAppointmentShouldReturnOkWhenValid() {
        when(appointmentRepository.existsById(1)).thenReturn(true);
        Appointment appointment = new Appointment();
        appointment.setPatientId(10);
        when(patientRepository.existsById(10)).thenReturn(true);

        ResponseEntity<String> response = appointmentController.updateAppointment(1, appointment);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Appointment updated successfully", response.getBody());
        assertEquals(1, appointment.getId());
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void deleteAppointmentShouldReturnNotFoundWhenMissing() {
        when(appointmentRepository.existsById(1)).thenReturn(false);

        ResponseEntity<String> response = appointmentController.deleteAppointment(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateAppointmentShouldReturnBadRequestWhenExceptionOccurs() {
        when(appointmentRepository.existsById(1)).thenThrow(new RuntimeException("DB down"));

        ResponseEntity<String> response = appointmentController.updateAppointment(1, new Appointment());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteAppointmentShouldReturnInternalServerErrorWhenDeleteFails() {
        when(appointmentRepository.existsById(1)).thenReturn(true);
        org.mockito.Mockito.doThrow(new RuntimeException("DB down"))
                .when(appointmentRepository).deleteById(1);

        ResponseEntity<String> response = appointmentController.deleteAppointment(1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void deleteAppointmentShouldReturnOkWhenExists() {
        when(appointmentRepository.existsById(1)).thenReturn(true);

        ResponseEntity<String> response = appointmentController.deleteAppointment(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Appointment deleted successfully", response.getBody());
        verify(appointmentRepository).deleteById(1);
    }
}
