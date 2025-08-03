package org.example.company_p.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.company_p.dto.ProjectExperienceDto;
import org.example.company_p.exception.DuplicateEmailException;
import org.example.company_p.mapper.EmployeeMapper;
import org.example.company_p.mapper.ProjectExperienceMapper;
import org.example.company_p.repository.ProjectExperienceRepository;
import org.example.company_p.service.EmployeeService;
import org.example.company_p.dto.EmployeeDto;
import org.example.company_p.entity.Employee;
import org.example.company_p.repository.EmployeeRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * EmployeeServiceImpl, EmployeeService arayüzünü uygulayan servis katmanıdır.
 * Çalışanlarla ilgili tüm iş mantığını (listeleme, oluşturma, güncelleme, silme) içerir.
 * Mapper ile DTO <-> Entity dönüşümlerini yapar, Repository ile veritabanı işlemlerini yürütür.
 */
@Service  //Spring'e bu sınıfın bir "servis" olduğunu bildirir
@RequiredArgsConstructor //final alanları için otomatik constructor oluşturur
public class EmployeeServiceImpl implements EmployeeService {
    //Repository ve Mapper bağımlılıkğını oluşturur
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final ProjectExperienceRepository projectExperienceRepository;
    private final ProjectExperienceMapper projectExperienceMapper;






    /**
     *
     * Yeni bir çalışan kaydeder
     * @param employeeDto:Kullanıcıdan gelen çalışan verileri
     * @return :Kaydedilen çalışanın DTO karşılığı
     */
    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        Optional<Employee> existing = employeeRepository.findByEmail(employeeDto.getEmail());

        if (existing.isPresent()) {
            throw new DuplicateEmailException("This email already exist: " + employeeDto.getEmail());
        }

        Employee employee = employeeMapper.employeeDtoToEmployee(employeeDto); // mapper  mevcut olan
        Employee saved = employeeRepository.save(employee);
        return employeeMapper.employeeToEmployeeDto(saved); // DTO'ya çevirip geri döner
    }



    /**
     * Silinmmemiş tüm çalışanları getirir
     * @return Çalışanların DTO listesi
     */


    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees=employeeRepository.findByIsDeletedFalse();

        return employees.stream().map(employee->{
            EmployeeDto dto=employeeMapper.employeeToEmployeeDto(employee);

            return dto;
        }).collect(Collectors.toList());

    }

    /**
     * ID'ye göre bir çalışan getirir
     * @param id Aranacak çalışanın ID'si
     * @return DTO olarak çalışan ,bulunmazsa boş döner
     */
    @Cacheable("getEmployeeById")
    @Override
    public Optional<EmployeeDto> getEmployeeById(Long id) {
        Optional<Employee> employeeOpt = employeeRepository.findByIdAndIsDeletedFalse(id);
        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            EmployeeDto dto = employeeMapper.employeeToEmployeeDto(employee);
            return Optional.of(dto); // sadece temel bilgiler
        }
        return Optional.empty();
    }





    /**
     * Belirli bir çalışanın bilgilerini günceller
     *
     * @param id:Güncellenecek çalışanın ID'si
     * @param dto:Yeni bilgiler
     * @return  GÜncellenmiş çalışanını DTO'su
     */
    @Override
    public EmployeeDto updateEmployee(Long id,EmployeeDto dto) {
        //Çalışan bulunmazsa istisna fırlatır
        Employee employee = employeeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(()-> new NoSuchElementException("Employee not found"));

        Optional<Employee> existing = employeeRepository.findByEmail(dto.getEmail());

        if (existing.isPresent() && !existing.get().getId().equals(id)) {

            //Aynı email başka çalışana aitse hata ver
            throw new DuplicateEmailException("This email already exists: " + dto.getEmail());
        }


        //Mevcut çalışanın bilgileri güncellenir
        employee.setName(dto.getName());
        employee.setSurname(dto.getSurname());
        employee.setEmail(dto.getEmail());

        //Veritabanında güncelleme yapılır ve DTO'ya dönüştürülerek döndürülür
        return employeeMapper.employeeToEmployeeDto(employeeRepository.save(employee));
    }

    /**
     * Belirli bir çalışanı "soft delete" yapar
     * @param id:Silinecek çalışananın ID'si
     */

    @Override
    public void deleteEmployee(Long id) {
        //Sadece silinmemiş kayıtlar içinde ID ile çalışan arar
        Employee employee = employeeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(()->new NoSuchElementException("employee not found"));
        //Silme işlemi yerine,isDeleted=true yapılır
        employee.setDeleted(true);
        employeeRepository.save(employee);

    }


    @Override
    public void patchUpdate(Long id, Map<String, Object> updates) {
        Employee employee = employeeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Email kontrolü
        if (updates.containsKey("email")) {
            String newEmail = ((String) updates.get("email")).trim();

            // Aynı email başka biri tarafından kullanılıyor mu?
            Optional<Employee> existingEmailOwner = employeeRepository.findByEmail(newEmail);

            // Eğer bu email başka bir çalışana aitse hata fırlat
            if (existingEmailOwner.isPresent() && !existingEmailOwner.get().getId().equals(id)) {
                throw new IllegalArgumentException("This email is already in use by another employee.");
            }

            employee.setEmail(newEmail);
        }

        // Diğer alanlar
        if (updates.containsKey("name")) {
            employee.setName((String) updates.get("name"));
        }

        if (updates.containsKey("surname")) {
            employee.setSurname((String) updates.get("surname"));
        }

        employeeRepository.save(employee);
    }

    @Override
    public Optional<EmployeeDto> getEmployeeWithProjects(Long id) {
        Optional<Employee> employeeOpt = employeeRepository.findByIdAndIsDeletedFalse(id);
        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            EmployeeDto dto = employeeMapper.employeeToEmployeeDto(employee);

            // Bu metot mapper ile dönüşüm + süre + aktiflik hesaplamasını yapmalı
            List<ProjectExperienceDto> entities = projectExperienceRepository.selectProjectExp(id);
            List<ProjectExperienceDto> projects = entities.stream()
                    .map(project -> {
                        ProjectExperienceDto pdto = project;
                        return pdto;
                    })
                    .collect(Collectors.toList());

            dto.setProjects(projects);
            return Optional.of(dto);
        }
        return Optional.empty();
    }



    @Override
    public Optional<EmployeeDto> getEmployeeWithoutProjects(Long id) {
        Optional<Employee> employeeOpt = employeeRepository.findByIdAndIsDeletedFalse(id);
        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            EmployeeDto dto = employeeMapper.employeeToEmployeeDto(employee);

            // Burada projeleri set etme!
            return Optional.of(dto);
        }
        return Optional.empty();
    }

    @Override
    public List<EmployeeDto> getActiveEmployee(){
        return employeeRepository.findByIsDeletedFalse()
                .stream()
                .map(employeeMapper::employeeToEmployeeDto)
                .collect(Collectors.toList());
    }



}

