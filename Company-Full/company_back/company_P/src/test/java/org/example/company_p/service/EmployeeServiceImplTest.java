package org.example.company_p.service;

import org.example.company_p.dto.EmployeeDto;
import org.example.company_p.entity.Employee;
import org.example.company_p.mapper.EmployeeMapper;
import org.example.company_p.repository.EmployeeRepository;
import org.example.company_p.repository.ProjectExperienceRepository;
import org.example.company_p.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.example.company_p.util.TestDataFactory.*;

@ExtendWith(MockitoExtension.class)  //Mockito ve Junit birlikte çalışsın diye
public class EmployeeServiceImplTest {
    @Mock //Sahte (mock) repository
    private EmployeeRepository employeeRepository;


    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks //Gerçek servis içine yukardıdaki mockl'lar otomatik olarak eklenir
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeDto employeeDto;

    //Test verilerini setup içinde değil,test sınıfından alyıyoruz
    @BeforeEach //Her testten önce çalışır
    void setUp() {
        employee=createTestEmployee(); //Entity(veritabanı modeli)
        employeeDto=createTestEmployeeDto(); //DTO(API modeli)
    }
    @Test
    void saveEmployee_shouldReturnSavedEmployee() {
        //DTO nesnesini Entity'ye dönüştürüldüğünde,bu employee nesnesini döndür
        when(employeeMapper.employeeDtoToEmployee(employeeDto)).thenReturn(employee);

        //employee kaydedilirse,aynı nesneyi döndür(veritabanına gitmeden taklit eder)
        when(employeeRepository.save(employee)).thenReturn(employee);

        //Kaydedilen employee tekrar DTO'ya çevirilidiğinde,bu DTO'yu döndür
        when(employeeMapper.employeeToEmployeeDto(employee)).thenReturn(employeeDto);

        EmployeeDto result=employeeService.saveEmployee(employeeDto);

        assertEquals("Kaya",result.getName());
        verify(employeeRepository).save(employee);
    }


    @Test
    void getEmployeeById_whenNotExists_shouldReturnEmpty() {
        //ID'si 2 olan çalışan bulunmazsa,boş Optional dön
        when(employeeRepository.findByIdAndIsDeletedFalse(2L)).thenReturn(Optional.empty());
        Optional<EmployeeDto> result=employeeService.getEmployeeById(2L);
        assertFalse(result.isPresent());
    }


    @Test
    void updateEmployee_shouldUpdateAndReturnEmployee(){
        //Güncellenecek çalışan veritabanında varsa,bu employee nesnesini döndürür
        when(employeeRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(employee));

        //Güncellenmiş çalışan kaydedilince aynı nesneyi döndür
        when(employeeRepository.save(employee)).thenReturn(employee);

        //Kaydedilen nesne DTO'ya çevirilidiğinde bu DTO'yu döndür
        when(employeeMapper.employeeToEmployeeDto(employee)).thenReturn(employeeDto);

        EmployeeDto updated=employeeService.updateEmployee(1L, employeeDto);

        assertEquals("Kaya",updated.getName());
        verify(employeeRepository).save(employee);

    }

    @Test
    void deleteEmployee_shouldSetIsDeletedTrue(){
        //Silinecek çalışan bulunduğunda employee nesnesini döndür
        when(employeeRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1L);

        assertTrue(employee.isDeleted());
        verify(employeeRepository).save(employee); //silindi olarak işaretlenip kaydedildi mi

    }

    @Test
    void updateEmployee_whenNotFound_shouldThrowException(){
        //Güncellenmek istenen çalışan bulunamazsa,boş Optional dön
        when(employeeRepository.findByIdAndIsDeletedFalse(99L)).thenReturn(Optional.empty());

        //Bu durumda NoSuchElementException hatası fırlatılmalı
        assertThrows(NoSuchElementException.class,()
        -> employeeService.updateEmployee(99L, employeeDto));
    }

}
