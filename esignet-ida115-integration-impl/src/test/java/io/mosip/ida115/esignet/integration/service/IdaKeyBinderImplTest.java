package io.mosip.ida115.esignet.integration.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.esignet.api.dto.SendOtpDto;
import io.mosip.esignet.api.exception.KeyBindingException;
import io.mosip.esignet.api.exception.SendOtpException;
import io.mosip.ida115.esignet.integration.dto.IdaError;
import io.mosip.ida115.esignet.integration.dto.IdaResponseWrapper;
import io.mosip.ida115.esignet.integration.dto.KeyBindingResponse;

@RunWith(MockitoJUnitRunner.class)
public class IdaKeyBinderImplTest {

    @InjectMocks
    private Ida115KeyBinderImpl idaKeyBinderImpl;

    @Mock
    private HelperService helperService;

    @Mock
    private RestTemplate restTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String PARTNER_ID_HEADER = "partner-id";
    private static final String PARTNER_API_KEY_HEADER = "partner-api-key";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void sendBindingOtp_withValidDetails_thenPass() throws Exception {
        SendOtpDto sendOtpDto = new SendOtpDto();
        sendOtpDto.setIndividualId("1234");
        sendOtpDto.setTransactionId("4567");
        List<String> otpChannelsList = new ArrayList<>();
        otpChannelsList.add("channel");
        sendOtpDto.setOtpChannels(otpChannelsList);
        Map<String, String> headers = new HashMap<>();
        headers.put(PARTNER_ID_HEADER, PARTNER_ID_HEADER);
        headers.put(PARTNER_API_KEY_HEADER, PARTNER_API_KEY_HEADER);
        try {
        	idaKeyBinderImpl.doKeyBinding("individualId", new ArrayList<>(), new HashMap<>(),
                "WLA", headers);
        	Assert.fail();
        } catch (KeyBindingException e) {
            Assert.assertEquals("not_implemented", e.getErrorCode());
        }
    }

    @Test
    public void sendBindingOtp_withErrorResponse_throwsException() throws Exception {
        SendOtpDto sendOtpDto = new SendOtpDto();
        sendOtpDto.setIndividualId(null);
        sendOtpDto.setTransactionId("4567");
        List<String> otpChannelsList = new ArrayList<>();
        otpChannelsList.add("channel");
        sendOtpDto.setOtpChannels(otpChannelsList);
        Map<String, String> headers = new HashMap<>();
        headers.put(PARTNER_ID_HEADER, PARTNER_ID_HEADER);
        headers.put(PARTNER_API_KEY_HEADER, PARTNER_API_KEY_HEADER);
        try {
            idaKeyBinderImpl.sendBindingOtp("individualId", Arrays.asList("email"), headers);
            Assert.fail();
        } catch (SendOtpException e) {
            Assert.assertEquals("not_implemented", e.getErrorCode());
        }
    }

//    @Test
//    public void sendBindingOtp_withEmptyHeaders_throwsException() throws Exception {
//        try {
//            idaKeyBinderImpl.sendBindingOtp("individualId", Arrays.asList("email"), new HashMap<>());
//            Assert.fail();
//        } catch (SendOtpException e) {
//            Assert.assertEquals(Ida115KeyBinderImpl.REQUIRED_HEADERS_MISSING, e.getErrorCode());
//        }
//    }

    @Test
    public void doKeyBinding_withValidDetails_thenPass() throws KeyBindingException {
        IdaResponseWrapper<KeyBindingResponse> idaResponseWrapper = new IdaResponseWrapper<>();
        KeyBindingResponse keyBindingResponse = new KeyBindingResponse();
        keyBindingResponse.setAuthToken("auth-token");
        keyBindingResponse.setBindingAuthStatus(true);
        keyBindingResponse.setIdentityCertificate("certificate");
        idaResponseWrapper.setResponse(keyBindingResponse);
        ResponseEntity<IdaResponseWrapper<KeyBindingResponse>> responseEntity = new ResponseEntity<IdaResponseWrapper<KeyBindingResponse>>(
                idaResponseWrapper, HttpStatus.OK);

        Map<String, String> headers = new HashMap<>();
        headers.put(PARTNER_ID_HEADER, PARTNER_ID_HEADER);
        headers.put(PARTNER_API_KEY_HEADER, PARTNER_API_KEY_HEADER);
        try {
        	idaKeyBinderImpl.doKeyBinding("individualId", new ArrayList<>(), new HashMap<>(),
                "WLA", headers);
        	Assert.fail();
        } catch (KeyBindingException e) {
            Assert.assertEquals("not_implemented", e.getErrorCode());
        }
    }

    @Test
    public void doKeyBinding_withAuthFailure_thenPass()  {
        IdaResponseWrapper<KeyBindingResponse> idaResponseWrapper = new IdaResponseWrapper<>();
        KeyBindingResponse keyBindingResponse = new KeyBindingResponse();
        keyBindingResponse.setAuthToken("auth-token");
        keyBindingResponse.setBindingAuthStatus(false);
        keyBindingResponse.setIdentityCertificate("certificate");
        idaResponseWrapper.setResponse(keyBindingResponse);
        ResponseEntity<IdaResponseWrapper<KeyBindingResponse>> responseEntity = new ResponseEntity<IdaResponseWrapper<KeyBindingResponse>>(
                idaResponseWrapper, HttpStatus.OK);

        Map<String, String> headers = new HashMap<>();
        headers.put(PARTNER_ID_HEADER, PARTNER_ID_HEADER);
        headers.put(PARTNER_API_KEY_HEADER, PARTNER_API_KEY_HEADER);
        try {
            idaKeyBinderImpl.doKeyBinding("individualId", new ArrayList<>(), new HashMap<>(),
                    "WLA", headers);
            Assert.fail();
        } catch (KeyBindingException e) {
            Assert.assertEquals("not_implemented", e.getErrorCode());
        }
    }

    @Test
    public void doKeyBinding_withErrorResponse_thenFail() {
        IdaResponseWrapper<KeyBindingResponse> idaResponseWrapper = new IdaResponseWrapper<>();
        IdaError idaError = new IdaError();
        idaError.setErrorCode("test-err-code");
        idaResponseWrapper.setErrors(Arrays.asList(idaError));
        ResponseEntity<IdaResponseWrapper<KeyBindingResponse>> responseEntity = new ResponseEntity<IdaResponseWrapper<KeyBindingResponse>>(
                idaResponseWrapper, HttpStatus.OK);

        Map<String, String> headers = new HashMap<>();
        headers.put(PARTNER_ID_HEADER, PARTNER_ID_HEADER);
        headers.put(PARTNER_API_KEY_HEADER, PARTNER_API_KEY_HEADER);
        try {
            idaKeyBinderImpl.doKeyBinding("individualId", new ArrayList<>(), new HashMap<>(),
                    "WLA", headers);
            Assert.fail();
        } catch (KeyBindingException e) {
            Assert.assertEquals("not_implemented", e.getErrorCode());
        }
    }

//    @Test
//    public void doKeyBinding_withEmptyHeaders_thenFail() {
//        try {
//            idaKeyBinderImpl.doKeyBinding("individualId", new ArrayList<>(), new HashMap<>(),
//                    "WLA", new HashMap<>());
//            Assert.fail();
//        } catch (KeyBindingException e) {
//            Assert.assertEquals(Ida115KeyBinderImpl.REQUIRED_HEADERS_MISSING, e.getErrorCode());
//        }
//    }
}
