package org.example.company_p.util;
import org.example.company_p.dto.EmployeeDto;
import org.example.company_p.entity.Employee;

/**
 * Bu sınıf ,testlerde tekrar tekrar kullanılabilecek örnek çalışan(Employee) verilerini oluşturur
 * Böylece her testte aynı verileri yazmak zorunda kalmayız
 */

public class TestDataFactory {

    /**
     * 1 numaralı örnek çalışan nesnesi(veritabanı modeli)
     *
     */

    public static Employee createTestEmployee() {
        return Employee.builder()
                .id(1L)
                .name("Kaya")
                .surname("Güneri")
                .email("kaya@example.com")
                .isDeleted(false)
                .build();
    }

    /**
     * 1 numaralı örnek DTO nesnesi (kullanıcıya dönecek veri)
     */
    public static EmployeeDto createTestEmployeeDto() {
        return EmployeeDto.builder()
                .id(1L)
                .name("Kaya")
                .surname("Güneri")
                .email("kaya@example.com")
                .build();
        //Kullanıcıya göstermek gereksiz olduğu için isDeleted kısmı yok

    }
    /**
     * 2.örnek çalışan(farklı test senaryoları için)
     */
    public static Employee createSecondEmployee() {
        return Employee.builder()
                .id(1L)
                .name("Kaan")
                .surname("Güneri")
                .email("kaan@example.com")
                .build();

    }
    public static EmployeeDto createSecondEmployeeDto() {
        return EmployeeDto.builder()
                .id(2L)
                .name("Kaan")
                .surname("güneri")
                .email("kaan@example.com")
                .build();
    }
}