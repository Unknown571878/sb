package edu.du.maptest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoogleMapsController {
    private static final String API_KEY = "AIzaSyCuciS5V0LMGBLJIF0ufEVIRfg9VVPuW3A";

    @GetMapping("/geocode")
    public String getGeocode(@RequestParam String address) {
        try {
            String jsonResponse = fetchGeocodeData(address);
            return parseCoordinates(jsonResponse);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String fetchGeocodeData(String address) throws Exception {
        String encodedAddress = java.net.URLEncoder.encode(address, "UTF-8");
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + encodedAddress + "&key=" + API_KEY;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new RuntimeException("API 호출 실패: " + response);
            return response.body().string();
        }
    }

    private String parseCoordinates(String jsonResponse) {
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

        if ("OK".equals(jsonObject.get("status").getAsString())) {
            JsonObject location = jsonObject
                    .getAsJsonArray("results")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("geometry")
                    .getAsJsonObject("location");

            double lat = location.get("lat").getAsDouble();
            double lng = location.get("lng").getAsDouble();

            return "위도: " + lat + ", 경도: " + lng;
        } else {
            return "Error: " + jsonObject.get("status").getAsString();
        }
    }
}
