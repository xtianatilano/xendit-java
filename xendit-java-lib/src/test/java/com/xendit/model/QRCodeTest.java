package com.xendit.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.xendit.Xendit;
import com.xendit.exception.XenditException;
import com.xendit.network.BaseRequest;
import com.xendit.network.RequestResource;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class QRCodeTest {
  private static String URL = String.format("%s%s", Xendit.getUrl(), "/qr_codes");
  private static String TEST_ID = "test_id";
  private static String TEST_EXTERNAL_ID = "test_external_id";
  private static String TEST_QR_STRING = "this_is_qr_string";
  private static String TEST_CALLBACK_URL = "https://callback.url";
  private static String TEST_QR_TYPE = QRCode.QRCodeType.DYNAMIC.toString();
  private static String TEST_QR_STATUS = QRCode.QRCodeStatus.ACTIVE.toString();
  private static Map<String, Object> PARAMS = new HashMap<>();
  private static Map<String, String> HEADERS = new HashMap<>();
  private static QRCode VALID_PAYMENT =
      QRCode.builder()
          .id(TEST_ID)
          .externalId(TEST_EXTERNAL_ID)
          .qrString(TEST_QR_STRING)
          .callbackUrl(TEST_CALLBACK_URL)
          .status(TEST_QR_STATUS)
          .amount(10000)
          .build();

  @Before
  public void initMocks() {
    Xendit.requestClient = mock(BaseRequest.class);
    PARAMS.clear();
  }

  private void initCreateParams() {
    PARAMS.put("external_id", TEST_EXTERNAL_ID);
    PARAMS.put("type", TEST_QR_TYPE);
    PARAMS.put("callback_url", TEST_CALLBACK_URL);
    PARAMS.put("amount", 10000);
  }

  @Test
  public void createQRCode_Success_IfMethodCalledCorrectly() throws XenditException {
    initCreateParams();

    when(Xendit.requestClient.request(RequestResource.Method.POST, URL, PARAMS, QRCode.class))
        .thenReturn(VALID_PAYMENT);
    QRCode qrCode =
        QRCode.createQRCode(TEST_EXTERNAL_ID, QRCode.QRCodeType.DYNAMIC, TEST_CALLBACK_URL, 10000);

    assertEquals(qrCode, VALID_PAYMENT);
  }

  @Test
  public void createQRCode_Success_IfParamsIsValid() throws XenditException {
    initCreateParams();

    when(Xendit.requestClient.request(
            RequestResource.Method.POST, URL, new HashMap<>(), PARAMS, QRCode.class))
        .thenReturn(VALID_PAYMENT);
    QRCode qrCode = QRCode.createQRCode(PARAMS);

    assertEquals(qrCode, VALID_PAYMENT);
  }

  @Test(expected = XenditException.class)
  public void createQRCode_ThrowsException_IfParamsIsInvalid() throws XenditException {
    initCreateParams();
    PARAMS.put("type", "NOT_DYNAMIC");

    when(Xendit.requestClient.request(
            RequestResource.Method.POST, URL, new HashMap<>(), PARAMS, QRCode.class))
        .thenThrow(new XenditException("QR Code type is invalid"));

    QRCode.createQRCode(PARAMS);
  }

  @Test
  public void createQRCode_Success_WithHeaderProvided() throws XenditException {
    initCreateParams();
    HEADERS.put("for-user-id", "user-id");

    when(Xendit.requestClient.request(
            RequestResource.Method.POST, URL, HEADERS, PARAMS, QRCode.class))
        .thenReturn(VALID_PAYMENT);

    QRCode qrCode = QRCode.createQRCode(HEADERS, PARAMS);

    assertEquals(qrCode, VALID_PAYMENT);
  }

  @Test
  public void GetQRCode_Success_WithExternalId() throws XenditException {
    String url = String.format("%s/%s", URL, TEST_EXTERNAL_ID);

    when(Xendit.requestClient.request(RequestResource.Method.GET, url, null, QRCode.class))
        .thenReturn(VALID_PAYMENT);

    QRCode qrCode = QRCode.getQRCode(TEST_EXTERNAL_ID);

    assertEquals(qrCode, VALID_PAYMENT);
  }

  @Test(expected = XenditException.class)
  public void GetQRCode_ThrowsException_OnExternalIDNotFound() throws XenditException {
    String NOT_VALID_EXTERNAL_ID = "not_valid_external_id";
    String url = String.format("%s/%s", URL, NOT_VALID_EXTERNAL_ID);

    when(Xendit.requestClient.request(RequestResource.Method.GET, url, null, QRCode.class))
        .thenThrow(new XenditException("not found"));

    QRCode.getQRCode(NOT_VALID_EXTERNAL_ID);
  }
}
