package org.example.company_p.service;

import org.example.company_p.dto.EmployeeDto;
import org.example.company_p.repository.EmployeeRepository;
import org.example.company_p.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Bu sınıf, EmployeeService arayüzünü test eder.
 * Servisin nasıl çağrıldığını, doğru döndürüp döndürmediğini kontrol eder.
 * Gerçek veri yerine sahte (mock) veri kullanılır.
 */
public class UnitTestCheckerTest {
        private EmployeeService employeeService; //mock(sahte) servis
        private EmployeeDto employeeDto;  //Test verisi

    @BeforeEach
    void setUp() {
        //Servisi sahte olarak (mock) oluştururz
        employeeService=mock(EmployeeService.class);

        //Hazır test verisini TestDataFactory'den alırız
        employeeDto=TestDataFactory.createTestEmployeeDto();
    }

    @Test
    void saveEmployee_shouldReturnEmployeeDto() {
        //Sahte servis:Herhangi bir DTO geldiğinde employeeDTO döner
        when(employeeService.saveEmployee(any(EmployeeDto.class))).thenReturn(employeeDto);

        //Metodu çağrırırz
        EmployeeDto result=employeeService.saveEmployee(new EmployeeDto());

        //Dönen değer null olmamalı ve adı "Kaya" olmalı
        assertNotNull(result);
        assertEquals("Kaya",result.getName());
    }

    @Test
    void getEmployeeById_shouldReturnOptionalEmployeeDto() {
        //ID=1 ile çağırıldığında DTO dönmesini sağlarız
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(employeeDto));

        //SErvisi çağır
        Optional<EmployeeDto> found=employeeService.getEmployeeById(1L);

        //Değer dönmeli ve adı doğru olmalı
        assertTrue(found.isPresent());
        assertEquals("Kaya",found.get().getName());

    }

    @Test
    void getAllEmployees_shouldReturnList(){
        //Sahte servis,tek elemanlık bir liste döner
        when(employeeService.getAllEmployees()).thenReturn(List.of(employeeDto));

        //Servisi çağır
        List<EmployeeDto> all=employeeService.getAllEmployees();

        //Liste 1 eleman içermeli ve soyadı "Güneri" olmalı
        assertEquals(1,all.size());
        assertEquals("Güneri",all.get(0).getSurname());

    }

    @Test
    void deleteEmployee_shouldNotThrowException(){
        //Silme işlemi hata fırlatmamalı
        doNothing().when(employeeService).deleteEmployee(1L);

        //Silme çağırıldığında istisna olmamalı
        assertDoesNotThrow(()->employeeService.deleteEmployee(1L));
    }

}
