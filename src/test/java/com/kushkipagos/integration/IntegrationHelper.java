package com.kushkipagos.integration;

import com.kushki.to.Amount;
import com.kushki.Kushki;
import com.kushki.enums.KushkiEnvironment;
import org.json.JSONObject;
import org.junit.Test;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;

public class IntegrationHelper {
    final static int THREAD_SLEEP = 1000;
    final static int THREAD_SLEEP_VOID = 4000;

    public static final String MERCHANT_ID = "10000001641080185390111217";
    private static final String SECRET_MERCHANT_ID = "10000001641088709280111217";

    public static final String MERCHANT_ID_COLOMBIA = "10000001958318993042555001";
    private static final String SECRET_MERCHANT_ID_COLOMBIA = "10000001958363505343555001";


    static Kushki getKushkiTESTECCommerce() {
        try {
            return new Kushki(SECRET_MERCHANT_ID, KushkiEnvironment.TESTING, "USD");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static Kushki getKushkiTESTCOCommerce() {
        try {
            return new Kushki(SECRET_MERCHANT_ID_COLOMBIA, KushkiEnvironment.TESTING, "COP");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static String getValidChargeToken(Kushki kushki, Amount amount) {
        String merchanId = "";
        if(kushki.getMerchantId() == SECRET_MERCHANT_ID) merchanId = MERCHANT_ID;
        if(kushki.getMerchantId() == SECRET_MERCHANT_ID_COLOMBIA) merchanId = MERCHANT_ID_COLOMBIA;
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(kushki.getEnvironment().getUrl()).path("tokens");
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON_TYPE);
        invocationBuilder.header("Public-Merchant-Id", merchanId);
        try {
            JSONObject jsonObj = new JSONObject("{\"card\":{\"name\": \"CardHolderName\",\"number\": \"CardNumber\",\"expiryMonth\": \"MM\",\"expiryYear\": \"YY\",\"cvv\": \"CVV\"},\"totalAmount\": "+amount.getTotalAmount()+",\"currency\": \""+kushki.getCurrency()+"\",\"isDeferred\": false}");
            Response response = invocationBuilder.post(Entity.json(jsonObj.toString()));
            JSONObject obj = new JSONObject(response.readEntity(String.class));
            return obj.getString("token");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    static String getValidSubscriptionChargeToken(Kushki kushki) {
        String merchanId = "";
        if(kushki.getMerchantId() == SECRET_MERCHANT_ID) merchanId = MERCHANT_ID;
        if(kushki.getMerchantId() == SECRET_MERCHANT_ID_COLOMBIA) merchanId = MERCHANT_ID_COLOMBIA;
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(kushki.getEnvironment().getUrl()).path("subscription-tokens");
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON_TYPE);
        invocationBuilder.header("Public-Merchant-Id", merchanId);
        try {
            JSONObject jsonObj = new JSONObject("{\"card\":{\"name\": \"CardHolderName\",\"number\": \"CardNumber\",\"expiryMonth\": \"MM\",\"expiryYear\": \"YY\",\"cvv\": \"CVV\"}}");
            Response response = invocationBuilder.post(Entity.json(jsonObj.toString()));
            JSONObject obj = new JSONObject(response.readEntity(String.class));
            return obj.getString("token");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Test
    public void getChargeToken() {
        String token = getValidChargeToken(getKushkiTESTECCommerce(),new Amount(0d,0d,0d,0d));
        assertThat("this must return a valid token", token != null && token.length() > 0);

    }
    @Test
    public void getSubscriptionChargeToken() {
        String token = getValidSubscriptionChargeToken(getKushkiTESTECCommerce());
        assertThat("this must return a valid token", token != null && token.length() > 0);

    }

}
