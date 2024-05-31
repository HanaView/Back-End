package com.hana.config;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    private static final String LOCAL_SERVER_URL = "http://localhost:80";
    private static final String PROD_SERVER_URL = "https://www.hanaview.com";
    private static final String ACCESS_TOKEN = "AccessToken";
    private static final String REFRESH_TOKEN = "RefreshToken";

    @Bean
    public OpenAPI openAPI() {
        // Bearer AccessToken 토큰을 사용한 JWT 인증 방식
        SecurityScheme accessTokenSecurityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        // Bearer RefreshToken 토큰을 사용한 JWT 인증 방식
        SecurityScheme refreshTokenSecurityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("RefreshToken");

        // SecurityRequirement 설정
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(ACCESS_TOKEN)
                .addList(REFRESH_TOKEN);

        // Components 설정
        Components components = new Components()
                .addSecuritySchemes(ACCESS_TOKEN, accessTokenSecurityScheme)
                .addSecuritySchemes(REFRESH_TOKEN, refreshTokenSecurityScheme);

        // 서버 URL 설정
        Server localServer = new Server().url(LOCAL_SERVER_URL).description("Local server");
        Server prodServer = new Server().url(PROD_SERVER_URL).description("Production server");

        // OpenAPI 객체 생성 및 보안 스키마 설정
        return new OpenAPI()
                .addServersItem(localServer)
                .addServersItem(prodServer)
                .components(components)
                .addSecurityItem(securityRequirement)
                .info(new Info().title("HanaView API").description("화상 창구 서비스 HanaView API 명세서 입니다.").version("1.0.0"));
    }

    @Bean
    public GroupedOpenApi userOpenApi() {
        return createGroupedOpenApi("USER API", "/api/**", new String[]{"/api/admins/**"}, "HanaView API", "화상 창구 서비스 HanaView API 명세서 입니다.", "1.0.0");
    }

    @Bean
    public GroupedOpenApi adminOpenApi() {
        return createGroupedOpenApi("ADMIN API", "/api/admins/**", null, "Admin", "화상 창구 서비스 HanaView Admin API 명세서 입니다.", "1.0.0");
    }

    private GroupedOpenApi createGroupedOpenApi(String group, String path, String[] excludePaths, String title, String description, String version) {
        GroupedOpenApi.Builder builder = GroupedOpenApi.builder()
                .group(group)
                .pathsToMatch(path);

        if (excludePaths != null) {
            builder.pathsToExclude(excludePaths);
        }

        builder.addOpenApiCustomizer(
                openApi -> openApi.setInfo(
                        new Info()
                                .title(title)
                                .description(description)
                                .version(version)
                )
        );

        return builder.build();
    }
}
