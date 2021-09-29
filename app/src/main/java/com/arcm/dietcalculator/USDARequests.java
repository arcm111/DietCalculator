package com.arcm.dietcalculator;

import android.util.Log;

import com.arcm.dietcalculator.database.FoodItem;
import com.arcm.dietcalculator.database.Nutrients;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class USDARequests {
    private static final String TAG = "USDARequests";
    private static final String SERVER_URL = "https://api.nal.usda.gov/fdc/v1";
    private static final String API_KEY = "tTZHKlvOBcBAVYWICxXPeVhxtvOX2ftfdaInofZy";
    public static final int FIBER_ID = 1079;
    public static final int PROTEIN_ID = 1003;
    public static final int FAT_ID = 1004;
    public static final int CARBS_ID = 1005;
    public static final int CALS_ID = 1008;
    public static final int SUGAR_ID = 2000;
    public static final int SATURATED_FAT_ID = 1258;

    public static class SearchRequest extends GetRequest {
        private static final String ARGS = "api_key=%s&dataType=%s&pageSize=%d&pageNumber=%d&query=%s";
        private static final String URL = SERVER_URL + "/foods/search?" + ARGS;

        public SearchRequest(String query, int maxResults) {
            super(generateURL(query, maxResults));
        }

        public static String generateURL(String query, int maxResults) {
            Locale locale = Locale.getDefault();
            String dataType = "Foundation,SR Legacy";
            return String.format(locale, URL, API_KEY, dataType, maxResults, 1, query);
        }

        private List<FoodItem> parseData(String response) {
            List<FoodItem> items = new ArrayList<>();
            try {
                JSONObject json = new JSONObject(response);
                JSONArray foods = json.getJSONArray("foods");
                Log.d(TAG, "foods count: " + foods.length());
                for (int i = 0; i < foods.length(); i++) {
                    JSONObject f = foods.getJSONObject(i);
                    String name = f.getString("description");
                    String fdcId = f.getString("fdcId");
                    JSONArray nutrients = f.getJSONArray("foodNutrients");
                    Nutrients itemNutrients = new Nutrients();
                    for (int j = 0; j < nutrients.length(); j++) {
                        JSONObject nutrient = nutrients.getJSONObject(j);
                        int nutrientId = nutrient.getInt("nutrientId");
                        double value = nutrient.optDouble("value", 0);
                        String unit = nutrient.getString("unitName");
                        switch (nutrientId) {
                            case USDARequests.CALS_ID:
                                itemNutrients.cals = value;
                                if (!unit.toLowerCase().equals("kcal")) {
                                    Log.e(TAG, "unexpectd unit " + unit);
                                }
                                break;
                            case USDARequests.FAT_ID:
                                itemNutrients.fat = value;
                                if (!unit.toLowerCase().equals("g")) {
                                    Log.e(TAG, "unexpectd unit " + unit);
                                }
                                break;
                            case USDARequests.PROTEIN_ID:
                                itemNutrients.protein = value;
                                if (!unit.toLowerCase().equals("g")) {
                                    Log.e(TAG, "unexpectd unit " + unit);
                                }
                                break;
                            case USDARequests.CARBS_ID:
                                itemNutrients.carbs = value;
                                if (!unit.toLowerCase().equals("g")) {
                                    Log.e(TAG, "unexpectd unit " + unit);
                                }
                                break;
                        }
                    }
                    FoodItem item = new FoodItem(name, itemNutrients);
                    item.setFdcId(fdcId);
                    item.setGroup(FoodItem.Group.OTHER);
                    Log.d(TAG, item.toString());
                    items.add(item);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return items;
        }

        @Override
        protected void onSuccess(String response) {
            super.onSuccess(response);
            onResponse(parseData(response));
        }

        protected void onResponse(List<FoodItem> items) {
        }
    }

    public static class FoodItemRequest extends GetRequest {
        private static final String URL = SERVER_URL + "/food/%s?api_key=" + API_KEY;

        public FoodItemRequest(String fcdId) {
            super(String.format(URL, fcdId));
        }

        @Override
        protected void onSuccess(String response) {
            super.onSuccess(response);
            onResponse(parseData(response));
        }

        private FoodItem parseData(String response) {
            try {
                JSONObject food = new JSONObject(response);
                String name = food.getString("description");
                String id = food.getString("fdcId");
                JSONArray nutrients = food.getJSONArray("foodNutrients");
                Nutrients itemNutrients = new Nutrients();
                for (int i = 0; i < nutrients.length(); i++) {
                    JSONObject foodNutrient = nutrients.getJSONObject(i);
                    double value = foodNutrient.optDouble("amount", 0);
                    JSONObject nutrient = foodNutrient.getJSONObject("nutrient");
                    int nutrientId = nutrient.getInt("id");
                    String unit = nutrient.getString("unitName");
                    switch (nutrientId) {
                        case USDARequests.CALS_ID:
                            itemNutrients.cals = value;
                            if (!unit.equals("kcal")) {
                                Log.e(TAG, "unexpectd unit " + unit);
                            }
                            break;
                        case USDARequests.FAT_ID:
                            itemNutrients.fat = value;
                            if (!unit.equals("g")) {
                                Log.e(TAG, "unexpectd unit " + unit);
                            }
                            break;
                        case USDARequests.PROTEIN_ID:
                            itemNutrients.protein = value;
                            if (!unit.equals("g")) {
                                Log.e(TAG, "unexpectd unit " + unit);
                            }
                            break;
                        case USDARequests.CARBS_ID:
                            itemNutrients.carbs = value;
                            if (!unit.equals("g")) {
                                Log.e(TAG, "unexpectd unit " + unit);
                            }
                            break;
                        case USDARequests.SUGAR_ID:
                            itemNutrients.sugar = value;
                            if (!unit.equals("g")) {
                                Log.e(TAG, "unexpectd unit " + unit);
                            }
                            break;
                        case USDARequests.SATURATED_FAT_ID:
                            itemNutrients.saturatedFat = value;
                            if (!unit.equals("g")) {
                                Log.e(TAG, "unexpectd unit " + unit);
                            }
                            break;
                        case USDARequests.FIBER_ID:
                            itemNutrients.fibre = value;
                            if (!unit.equals("g")) {
                                Log.e(TAG, "unexpectd unit " + unit);
                            }
                    }
                }
                FoodItem item = new FoodItem(name, itemNutrients);
                item.setFdcId(id);
                item.setGroup(FoodItem.Group.OTHER);
                return item;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        protected void onResponse(FoodItem item) {
        }
    }

    private static class GetRequest {
        private final OkHttpClient client;
        private final Request request;

        public GetRequest(String url) {
            this.client = new OkHttpClient();
            this.request = new Request.Builder().url(url).build();
        }

        public void execute() {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) {
                    try (ResponseBody body = response.body()) {
                        if (!response.isSuccessful()) {
                            Log.e(TAG, "Unexpected response code: " + response);
                        } else {
                            if (body != null) {
                                onSuccess(body.string());
                            }
                            else Log.e(TAG, "null response");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }

        protected void onSuccess(String response) {
            Log.d(TAG, response);
        }
    }
}
