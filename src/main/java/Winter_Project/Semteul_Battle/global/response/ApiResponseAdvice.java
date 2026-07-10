package Winter_Project.Semteul_Battle.global.response;

import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import Winter_Project.Semteul_Battle.global.status.SuccessStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(basePackages = "Winter_Project.Semteul_Battle.domain")
@RequiredArgsConstructor
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {
    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !BaseResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {
        if (isSwaggerRequest(request) || body instanceof BaseResponse<?>) {
            return body;
        }

        BaseResponse<Object> wrapped = wrapBody(body, response);
        if (StringHttpMessageConverter.class.isAssignableFrom(selectedConverterType)) {
            try {
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return objectMapper.writeValueAsString(wrapped);
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("API ????????轅붽틓????????거????????怨뚯댅???????????낆젵.", e);
            }
        }
        return wrapped;
    }

    private BaseResponse<Object> wrapBody(Object body, ServerHttpResponse response) {
        int status = getStatus(response);
        if (status >= 400) {
            return BaseResponse.onFailure(ErrorStatus.fromHttpStatus(status), body);
        }
        return BaseResponse.onSuccess(SuccessStatus.fromHttpStatus(status), body);
    }

    private int getStatus(ServerHttpResponse response) {
        if (response instanceof ServletServerHttpResponse servletResponse) {
            HttpServletResponse rawResponse = servletResponse.getServletResponse();
            return rawResponse.getStatus();
        }
        return HttpServletResponse.SC_OK;
    }

    private boolean isSwaggerRequest(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        return path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui");
    }
}
