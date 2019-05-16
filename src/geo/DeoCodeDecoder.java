package geo;

import org.json.JSONObject;
import utils.JsonReader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DeoCodeDecoder {

    public static String getLatAndLong(String address) throws IOException {
        String key = Files.readAllLines(Paths.get("params.txt")).get(0).trim();
        final String baseUrl = "https://maps.googleapis.com/maps/api/geocode/json";// путь к Geocoding API по HTTP

        final Map<String, String> params = new HashMap<>();
        params.put("sensor", "false");// исходит ли запрос на геокодирование от устройства с датчиком местоположения
        params.put("key", key);// исходит ли запрос на геокодирование от устройства с датчиком местоположения
        params.put("address", address);// адрес, который нужно геокодировать

        final String url = baseUrl + '?' + encodeParams(params);// генерируем путь с параметрами

        final JSONObject response = JsonReader.read(url);// делаем запрос к вебсервису и получаем от него ответ
        // как правило наиболее подходящий ответ первый и данные о координатах можно получить по пути
        // //results[0]/geometry/location/lng и //results[0]/geometry/location/lat
        if (response == null){
            return "";
        }
        try {
            JSONObject location = response.getJSONArray("results").getJSONObject(0);
            location = location.getJSONObject("geometry");
            location = location.getJSONObject("location");
            final double lng = location.getDouble("lng");// долгота
            final double lat = location.getDouble("lat");// широта
            return String.format("%f;%f", lat, lng);// итоговая широта и долгота
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    private static String encodeParams(final Map<String, String> params) {
        final StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry: params.entrySet()){
            try {
                builder.append(entry.getKey());// получаем значение вида key=value
                builder.append('=');
                builder.append(URLEncoder.encode(entry.getValue(), "utf-8"));// кодируем строку в соответствии со стандартом HTML 4.01
                builder.append('&');
            }catch (UnsupportedEncodingException e){
            }
        }
        if (builder.toString().endsWith("&")){
            return builder.substring(0, builder.length() - 1);
        }else {
            return builder.toString();
        }
    }
}
