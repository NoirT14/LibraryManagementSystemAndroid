package com.example.project_prm392.utils;

import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

public class JWTHelper {
    private static final String TAG = "JWTHelper";

    public static JSONObject parseJWT(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return null;
            }

            String payload = parts[1];
            byte[] decodedBytes = Base64.decode(payload, Base64.URL_SAFE);
            String decodedString = new String(decodedBytes);

            return new JSONObject(decodedString);
        } catch (Exception e) {
            Log.e(TAG, "Error parsing JWT", e);
            return null;
        }
    }

    public static String getClaimFromToken(String token, String claim) {
        try {
            JSONObject payload = parseJWT(token);
            if (payload != null) {
                return payload.optString(claim, null);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting claim from token", e);
        }
        return null;
    }

    public static boolean isTokenExpired(String token) {
        try {
            JSONObject payload = parseJWT(token);
            if (payload != null) {
                long exp = payload.optLong("exp", 0);
                long currentTime = System.currentTimeMillis() / 1000;
                return exp < currentTime;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error checking token expiration", e);
        }
        return true; // Consider expired if we can't parse
    }
}
