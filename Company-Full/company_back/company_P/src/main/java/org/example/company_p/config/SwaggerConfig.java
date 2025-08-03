package org.example.company_p.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  //Bu sınıfın bir yapılandırma (config) sınıfı olduğunu beliritr
public class SwaggerConfig {

    //OpenAPI bean'i tanımlar,swagger UI için ayarlar yapılır
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                //API hakkında genel bilgiler
                .info(new Info()
                        .title("Company API")  //Dökümantsyonda gözükecek başlık
                        .version("1.0")        //API versiyonu
                        .description("Company API"))  //Kısa açıklama

                //Swagger arayüzünde güvenlik eklenir
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))

                //JWT yapılandırması burada tanımlanır
                .components(new Components()

                        //Güvenlik şeması adı
                        .addSecuritySchemes("bearerAuth",

                                new SecurityScheme()

                                        .name("Authorization") //Header adı
                                        .type(SecurityScheme.Type.HTTP) //HTTP tabanlı güvenlik
                                        .scheme("bearer")  //Bearer token kullanılır
                                        .bearerFormat("JWT") //Token formatı JWT
                        ));
    }
}
