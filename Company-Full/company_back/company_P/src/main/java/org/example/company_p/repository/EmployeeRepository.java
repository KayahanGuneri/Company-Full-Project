package org.example.company_p.repository;
import org.example.company_p.entity.Employee;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;


/**
 * EmployeeRepository,Employee entity'si için veritabanı işlemlerini yapan JPA arayüzüdür
 * CRUD işlemleri ve soft delete filtreli sorgular içerir
 */
@Repository  //Veritabanı ile beraber çalıştığı için Repository kullanılır diğerlerine göre daha iyi belirtiyor
@Qualifier("PostgreSql")
public interface EmployeeRepository extends JpaRepository<Employee,Long>{
        //Yalnızca silinmemiş kayıtları getir
        List<Employee> findByIsDeletedFalse();
        //Verilen ID'ye sahip ve soft delete yapılmamış çalışanı döndürür
        Optional<Employee> findByIdAndIsDeletedFalse(Long id);

        Optional<Employee> findByEmail(String email);
}