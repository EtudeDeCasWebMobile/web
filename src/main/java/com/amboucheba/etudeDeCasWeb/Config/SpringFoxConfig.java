package com.amboucheba.etudeDeCasWeb.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.function.Predicate;

@Configuration
public class SpringFoxConfig {
//<<<<<<< HEAD

    @Bean
    public Docket documentation() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.amboucheba.etudeDeCasWeb"))
                .paths(PathSelectors.any())
                .paths(Predicate.not(PathSelectors.ant("/documentation")) )
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo(){
        return new ApiInfo(
                "Etude de cas web mobile Api Documentation",
                "This is the 'Etude de cas web mobile' api implemented a school project",
                "1.0",
                null,
                new Contact("Team 4", null, "kechEmail@gmail.com"),
                null,
                null,
                Collections.emptyList()
                );
    }
//=======
//
//	@Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//          .select()
//          .apis(RequestHandlerSelectors.any())
//          .paths(PathSelectors.any())
//          .build();
//    }

//    @Bean
//    public Docket documentation() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .useDefaultResponseMessages(false)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.amboucheba.seriesTemporellesTpWeb"))
//                .paths(PathSelectors.any())
//                .paths(Predicate.not(PathSelectors.ant("/documentation")) )
//
//                .build()
//                .apiInfo(apiInfo());
//    }
//
//    private ApiInfo apiInfo(){
//        return new ApiInfo(
//                "Series Temporelles Api Documentation",
//                "This is the 'Series Temporelles' api implemented for Web Technologies's  Homework, it describes the crud operation of the series temporelles, events and tag entity",
//                "1.0",
//                null,
//                new Contact("Amine Bouchebaba", null, "bouchebaba.amine@gmail.com"),
//                null,
//                null,
//                Collections.emptyList()
//                );
//    }
//>>>>>>> 6dfda2f8c8fa49fdb61c8904c98f5fb0dba3b385
}
