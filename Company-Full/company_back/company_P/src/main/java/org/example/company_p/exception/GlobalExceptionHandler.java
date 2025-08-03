package org.example.company_p.exception;

import org.springframework.http.HttpStatus; // HTTP durum kodlarını belirtmek için kullanılır
import org.springframework.http.ResponseEntity; // HTTP yanıtı oluşturmak için kullanılır
import org.springframework.web.bind.MethodArgumentNotValidException; // Validasyon hatalarını temsil eder
import org.springframework.web.bind.annotation.ExceptionHandler; // Hangi exception'ı yakalayacağımızı belirtir
import org.springframework.web.bind.annotation.RestControllerAdvice; // Global exception handler olarak çalışmasını sağlar

import java.util.HashMap;
import java.util.Map;

/**
 * Bu sınıf, tüm uygulama boyunca oluşabilecek hataları merkezi bir şekilde yönetir.
 * @RestControllerAdvice anotasyonu, Controller'lar içinde oluşan istisnaları burada yakalamamızı sağlar.
 */
@RestControllerAdvice // Tüm controller'lar için geçerli bir exception handler olduğunu belirtir
public class GlobalExceptionHandler {

    /**
     * Çalışan (employee) bulunamadığında bu metot tetiklenir.
     * @param e: EmployeeNotFoundException tipi exception
     * @return 404 Not Found ve açıklayıcı mesaj
     */
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<String> handleEmployeeNotFound(EmployeeNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)  // HTTP 404 (Not Found) durum kodu
                .body(e.getMessage());         // Exception içindeki mesaj kullanıcıya iletilir
    }

    /**
     * Kullanıcıdan gelen veriler @Valid, @NotBlank gibi kurallara uymadığında bu blok devreye girer.
     * @param e: Validasyon hatasını temsil eden istisna
     * @return 400 Bad Request ve hangi alanlarda hangi hata olduğunu açıklayan bir Map
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>(); // Alan ismi,Hata mesajı eşleşmesi için boş map oluşturulur

        // Her hatalı alan için mesaj eklenir
        e.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity
                .badRequest()   // HTTP 400 Bad Request
                .body(errors); // Hatalar JSON olarak döner (field: message)
    }

    /**
     * Yukarıdaki tanımlanmış istisnalar dışında oluşan tüm genel (bilinmeyen) hataları yakalar.
     * @param e: Exception tipi genel hata
     * @return 500 Internal Server Error ve hata mesajı
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericError(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)       // HTTP 500 Internal Server Error  Sunucu hatası
                .body("Bir hata oluştu: " + e.getMessage());    // Hata mesajı response olarak döner
    }

    /**
     *Aynı email bulunuyorsa eğer bu hatayı fırlatır
     */
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<String> handleDuplicateEmail(DuplicateEmailException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(Exception e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Database constraint error: " + e.getMessage());
    }


}
