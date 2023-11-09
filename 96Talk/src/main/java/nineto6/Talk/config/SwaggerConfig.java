package nineto6.Talk.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import nineto6.Talk.common.codes.ErrorCode;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.util.ObjectUtils;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        String title = "96Talk API 명세서";
        String description = "POST:'/api/members/login' (memberEmail(사용자 이메일), memberPwd(사용자 비밀번호)) 로그인 요청";
        Info info = new Info()
                .title(title)
                .description(description);

        // Security 스키마 설정
        SecurityScheme bearerAuth = new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("Authorization")
                        .in(SecurityScheme.In.HEADER)
                        .name(HttpHeaders.AUTHORIZATION);

        // Security 요청 설정
        SecurityRequirement addSecurityItem = new SecurityRequirement().addList("Authorization");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Authorization", bearerAuth))
                .addSecurityItem(addSecurityItem)
                .info(info);
    }

    public ApiResponse createApiResponse(String message, Content content){
        return new ApiResponse().description(message).content(content);
    }

    @Bean
    public GlobalOpenApiCustomizer customerGlobalHeaderOpenApiCustomiser() {
        return openApi -> {
            // 공통으로 사용되는 response 설정
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {;
                if(!ObjectUtils.isEmpty(operation.getDescription())) {
                    if(operation.getDescription().contains("토큰")) {
                        // operation 설명에 '토큰'이 들어가있으면 ApiResponse 생성
                        ApiResponses apiResponses = operation.getResponses();
                        apiResponses.addApiResponse("200", createApiResponse(apiResponses.get("200").getDescription(), apiResponses.get("200").getContent()));
                        apiResponses.addApiResponse("401", createApiResponse(ErrorCode.UNAUTHORIZED_ERROR.getMessage().concat(" (승인되지 않은 사용자)"), null));
                        apiResponses.addApiResponse("403", createApiResponse(ErrorCode.FORBIDDEN_ERROR.getMessage().concat(" (승인은 되었지만, 접근할 수 없는 권한)"), null));
                    }
                }
            }));
        };
    }
}