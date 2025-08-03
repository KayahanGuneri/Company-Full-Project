package org.example.company_p.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.company_p.CompanyProjectApplication;
import org.example.company_p.dto.EmployeeDto;


import org.example.company_p.service.EmployeeService;
import org.example.company_p.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Bu sınıf, EmployeeController sınıfının HTTP davranışlarını test eder.
 * Gerçek servis katmanı kullanılmaz, onun yerine sahte (mock) servis tanımlanır.
 */

@AutoConfigureMockMvc // MockMvc bean'ini otomatik tanımlar
@SpringBootTest
@WithMockUser(username = "admin", roles = {"ADMIN"})
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc; // HTTP isteklerini taklit eder (gerçek sunucu açılmaz)

    @MockBean
    private EmployeeService employeeService; // Gerçek servis yerine sahte versiyon

    @Autowired
    private ObjectMapper objectMapper; // Java objesini JSON'a çevirmek için kullanılır

    private EmployeeDto employeeDto; // Testlerde kullanılacak örnek DTO verisi

    @BeforeEach
    void setUp() {
        // Her testten önce örnek DTO TestDataFactory'den alınır
        employeeDto = TestDataFactory.createTestEmployeeDto();
    }

    @Test
    void saveEmployee_shouldReturnCreated() throws Exception {
        // Servis sahte olarak, save metodu çağrıldığında bir DTO döndürür
        when(employeeService.saveEmployee(any(EmployeeDto.class))).thenReturn(employeeDto);

        // POST isteği yapılır, yanıtın 201 ve JSON değerleri kontrol edilir
        mockMvc.perform(post("/api/employees/save")

                        // İstek gövdesinin tipi JSON olduğunu belirtir (application/json)
                        .contentType(MediaType.APPLICATION_JSON)

                        // Java nesnesini (employeeDto) JSON string'e çevirip isteğe ekler
                        .content(objectMapper.writeValueAsString(employeeDto)))

                // Dönen HTTP durum kodu 201 (Created) olmalı
                .andExpect(status().isCreated())

                // JSON çıktısındaki "name" alanı "Kaya" olmalı
                .andExpect(jsonPath("$.name").value("Kaya"))

                // JSON çıktısındaki "surname" alanı "Güneri" olmalı
                .andExpect(jsonPath("$.surname").value("Güneri"));
    }

    @Test
    void getAllEmployees_shouldReturnList() throws Exception {
        // Servis sahte olarak 1 elemanlık bir liste döner
        when(employeeService.getAllEmployees()).thenReturn(Arrays.asList(employeeDto));

        // GET isteğiyle çalışan listesi alınır ve doğrulanır
        mockMvc.perform(get("/api/employees/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Kaya"));
    }
   /*
    @Test
    void getEmployeeById_whenFound_shouldReturnEmployee() throws Exception {
        // ID ile çağrıldığında, servis doğru DTO'yu döner
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(employeeDto));

        // GET isteği yapılır ve veri bulunduğu için 200 döner
        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("kaya@example.com"));
    }

    */

    @Test
    void getEmployeeById_whenNotFound_shouldReturn404() throws Exception {
        // Verilmeyen ID için boş dönmesi sağlanır
        when(employeeService.getEmployeeById(99L)).thenReturn(Optional.empty());

        // GET isteği sonucunda 404 Not Found beklenir
        mockMvc.perform(get("/api/employees/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateEmployee_shouldReturnUpdatedEmployee() throws Exception {
        // Güncelleme çağrıldığında, sahte servis aynı DTO'yu döner
        when(employeeService.updateEmployee(eq(1L), any(EmployeeDto.class))).thenReturn(employeeDto);

        // PUT isteği yapılır, güncellenen veri ve 200 OK kontrol edilir
        mockMvc.perform(put("/api/employees/1")

                        // İstek gövdesinin tipi JSON olduğunu belirtir (application/json)
                        .contentType(MediaType.APPLICATION_JSON)

                        // Java nesnesini (employeeDto) JSON string'e çevirip isteğe ekler
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.surname").value("Güneri"));
    }

    @Test
    void deleteEmployee_shouldReturnAccepted() throws Exception {
        // Silme işlemi yapıldığında, hiçbir şey dönmez
        doNothing().when(employeeService).deleteEmployee(1L);

        // DELETE isteği yapılır, 202 Accepted kontrol edilir
        mockMvc.perform(delete("/api/employees/delete/1"))
                .andExpect(status().isAccepted());
    }

    @Test
    void getAllEmployees_shouldReturnOk() throws Exception {
        //TestDataFactory'den sahte DTO nesnesini al
        EmployeeDto employeeDto = TestDataFactory.createTestEmployeeDto();

        //EmployeeService davranışını mockla
        when(employeeService.getAllEmployees()).thenReturn(List.of(employeeDto));

        //GET isteği gönder ve sonucu kontrol et
        mockMvc.perform(get("/api/employees/all"))
                .andExpect(status().isOk()) // 200 OK bekleniyor
                .andExpect(jsonPath("$[0].name").value("Kaya")) // JSON içinde "Kaya" var mı?
                .andExpect(jsonPath("$[0].surname").value("Güneri"))
                .andExpect(jsonPath("$[0].email").value("kaya@example.com"));
    }




}
