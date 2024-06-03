package com.hana.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    // 로컬 및 프로덕션 서버 URL
    private static final String LOCAL_SERVER_URL = "http://localhost:80";
    private static final String PROD_SERVER_URL = "https://www.hanaview.com";

    // 보안 스키마 이름
    private static final String ACCESS_TOKEN = "AccessToken";
    private static final String REFRESH_TOKEN = "RefreshToken";

    // OpenAPI 빈 정의
    @Bean
    public OpenAPI openAPI() {
        // Bearer JWT를 사용하는 AccessToken 보안 스키마 정의
        SecurityScheme accessTokenSecurityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        // API 키를 사용하는 RefreshToken 보안 스키마 정의
        SecurityScheme refreshTokenSecurityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("RefreshToken");

        // 보안 스키마를 컴포넌트에 추가
        Components components = new Components()
                .addSecuritySchemes(ACCESS_TOKEN, accessTokenSecurityScheme)
                .addSecuritySchemes(REFRESH_TOKEN, refreshTokenSecurityScheme);

        // 서버 URL 설정
        Server localServer = new Server().url(LOCAL_SERVER_URL).description("Local server");
        Server prodServer = new Server().url(PROD_SERVER_URL).description("Production server");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(ACCESS_TOKEN).addList(REFRESH_TOKEN);

        // OpenAPI 객체 생성 및 보안 스키마 설정
        return new OpenAPI()
                .addServersItem(localServer)
                .addServersItem(prodServer)
                .components(components)
                .security(Arrays.asList(securityRequirement))
                .info(new Info().title("HanaView API").description("화상 창구 서비스 HanaView API 명세서 입니다.").version("1.0.0"));
    }

    // 사용자 API 그룹 생성
    @Bean
    public GroupedOpenApi userOpenApi() {
        return createGroupedOpenApi("USER API", "/api/**", new String[]{"/api/admins/**"}, "HanaView API", "화상 창구 서비스 HanaView API 명세서 입니다.", "1.0.0");
    }

    // 관리자 API 그룹 생성
    @Bean
    public GroupedOpenApi adminOpenApi() {
        return createGroupedOpenApi("ADMIN API", "/api/admins/**", null, "Admin", "화상 창구 서비스 HanaView Admin API 명세서 입니다.", "1.0.0");
    }

    // GroupedOpenApi 객체를 생성하는 메서드
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

    // 특정 엔드포인트에 대해 RefreshToken 보안 설정을 추가하는 커스터마이저
    @Bean
    public OpenApiCustomizer refreshTokenEndpointCustomizer() {
        return openApi -> {
            SecurityRequirement refreshTokenRequirement = new SecurityRequirement().addList(ACCESS_TOKEN).addList(REFRESH_TOKEN);

            openApi.getPaths().forEach((path, pathItem) -> {
                if (path.startsWith("/api/admins/")) {  // /api/admins/로 시작하는 경로에 대해
                    pathItem.readOperations().forEach(operation -> operation.addSecurityItem(refreshTokenRequirement));  // 보안 요구 사항 추가
                }
            });
        };
    }
}
