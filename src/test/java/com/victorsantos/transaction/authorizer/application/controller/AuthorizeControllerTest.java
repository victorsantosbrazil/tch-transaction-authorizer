package com.victorsantos.transaction.authorizer.application.controller;

import static com.victorsantos.transaction.authorizer.application.controller.ControllerPath.TRANSACTIONS_PATH;
import static com.victorsantos.transaction.authorizer.application.usecase.authorize.AuthorizeUseCaseRequestExampleBuilder.oneAuthorizeUseCaseRequest;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.victorsantos.transaction.authorizer.application.constant.AuthorizationCode;
import com.victorsantos.transaction.authorizer.application.usecase.authorize.AuthorizeUseCase;
import com.victorsantos.transaction.authorizer.application.usecase.authorize.AuthorizeUseCaseResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthorizeController.class)
@ContextConfiguration(classes = AuthorizeControllerImpl.class)
class AuthorizeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorizeUseCase authorizeUseCase;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Given a request, when call endpoint, then return authorization response")
    void givenRequest_whenCallEndpoint_thenReturnAuthorizationResponse() throws Exception {
        var request = oneAuthorizeUseCaseRequest();
        var requestJson = objectMapper.writeValueAsString(request);

        var response = new AuthorizeUseCaseResponse(AuthorizationCode.APPROVED);
        var responseJson = objectMapper.writeValueAsString(response);

        when(authorizeUseCase.run(request)).thenReturn(response);

        mockMvc.perform(post(TRANSACTIONS_PATH).content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));
    }

    @Test
    @DisplayName(
            "Given a request, when call endpoint and exception thrown, then return authorization response with 'other' code")
    void givenRequest_whenCallEndpoint_andExceptionThrown_thenReturnAuthorizationResponseWithOtherCode()
            throws Exception {
        var request = oneAuthorizeUseCaseRequest();
        var requestJson = objectMapper.writeValueAsString(request);

        var response = new AuthorizeUseCaseResponse(AuthorizationCode.OTHER);
        var responseJson = objectMapper.writeValueAsString(response);

        doThrow(new RuntimeException("any error")).when(authorizeUseCase).run(request);

        mockMvc.perform(post(TRANSACTIONS_PATH).content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));
    }
}
