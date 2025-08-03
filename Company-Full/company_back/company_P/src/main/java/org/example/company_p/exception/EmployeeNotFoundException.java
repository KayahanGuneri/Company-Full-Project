package org.example.company_p.exception;

/**
 * Bu sınıf,çalışan (Employee) bulunmadığında fırlatılan özel bir hataadır
 * RuntimeException kalıtımı ile oluşturulur,yani çalışma zamanında fırlatılır
 */
public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(Long id) {
        super("employee not found"+id);
    }
}
