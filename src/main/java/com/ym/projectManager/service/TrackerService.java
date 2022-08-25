package com.ym.projectManager.service;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ym.projectManager.model.Parcel;
import com.ym.projectManager.model.TrackParcel;
import com.ym.projectManager.model.comparator.TrackParcelComparator;
import com.ym.projectManager.repository.ParcelRepository;
import com.ym.projectManager.repository.TrackParcelRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TrackerService {

    /**
     * Apikey
     */
    private String Apikey = "D377580BE09FDBCD101AF34889F3E093";

    private final ParcelRepository parcelRepository;
    private final TrackParcelRepository trackParcelRepository;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    public TrackerService(ParcelRepository parcelRepository, TrackParcelRepository trackParcelRepository) {
        this.parcelRepository = parcelRepository;
        this.trackParcelRepository = trackParcelRepository;
    }

    public void registerParcelInTracker(String trackNum) {
        Parcel parcel = parcelRepository.findFirstByTrackNumber(trackNum);
        if (parcel == null) {
            String requestData = "[{\"number\": \"" + trackNum + "\"}]";

            try {
                String result = orderOnlineByJson(requestData, "register");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String orderOnlineByJson(String requestData, String type) throws Exception {
        //---headerParams
        Map<String, String> headerparams = new HashMap<String, String>();
        headerparams.put("17token", Apikey);
        headerparams.put("Content-Type", "application/json");
        //---bodyParams
        List<String> bodyParams = new ArrayList<String>();
        String result = null;
        if (type.equals("register")) {
            String ReqURL = "https://api.17track.net/track/v1/register";
            bodyParams.add(requestData);
            result = sendPost(ReqURL, headerparams, bodyParams, "POST");

        } else if (type.equals("getTrackInfo")) {

            String ReqURL = "https://api.17track.net/track/v1/gettrackinfo";
            bodyParams.add(requestData);
            result = sendPost(ReqURL, headerparams, bodyParams, "POST");

        } else if (type.equals("batch")) {

            String ReqURL = "http://api.trackru.ru/v1/trackings/batch";
            bodyParams.add(requestData);
            result = sendPost(ReqURL, headerparams, bodyParams, "POST");

        } else if (type.equals("codeNumberGet")) {

            String ReqURL = "http://api.trackru.ru/v1/trackings";
            String RelUrl = ReqURL;
            result = sendGet(RelUrl, headerparams, "GET");

        } else if (type.equals("codeNumberPut")) {

            String ReqURL = "http://api.trackru.ru/v1/trackings";
            bodyParams.add(requestData);
            String RelUrl = ReqURL;
            result = sendPost(RelUrl, headerparams, bodyParams, "PUT");

        } else if (type.equals("codeNumberDelete")) {

            String ReqURL = "http://api.trackru.ru/v1/trackings";
            String RelUrl = ReqURL;
            result = sendGet(RelUrl, headerparams, "DELETE");

        } else if (type.equals("realtime")) {

            String ReqURL = "http://api.trackru.ru/v1/trackings/realtime";
            bodyParams.add(requestData);
            result = sendPost(ReqURL, headerparams, bodyParams, "POST");

        }


        return result;


    }

    public void updateTrackingParcel(){
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
        StringBuilder requestData = new StringBuilder("[");
        Optional<Parcel> notDeliveredParcel = parcelRepository.findFirstByDeliveredIsFalseAndTrackNumberIsNotNullOrderByLastUpdateDesc();
        if (notDeliveredParcel.isPresent()) {
            if (!format1.format(notDeliveredParcel.get().getLastUpdate())
                    .equals(format1.format(new Date()))) {

                Set<Parcel> parcels = parcelRepository.findAllByDeliveredIsFalse();
                if (parcels != null) {
                    parcels.stream().forEach(parcel -> {
                        requestData.append("{\"number\": \"" +
                                parcel.getTrackNumber() + "\"},");
                    });

                    requestData.deleteCharAt(requestData.length() - 1).append("]");
                    try {
                        String result = new TrackerService(parcelRepository, trackParcelRepository).orderOnlineByJson(requestData.toString(), "getTrackInfo");
                        JsonObject trackInfo = JsonParser.parseString(result).getAsJsonObject();
                        parseJsonAndSaveData(trackInfo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        }
    }

    private void parseJsonAndSaveData(JsonObject trackInfo) {
        String data0 = trackInfo.get("code").toString();

        if (data0.equals("0")) {
            JsonObject data = trackInfo.get("data").getAsJsonObject();
            JsonArray parcels = data.get("accepted").getAsJsonArray();

            for (JsonElement parcel : parcels) {
                String trackNum = parcel.getAsJsonObject().get("number").getAsString();
                JsonObject track = parcel.getAsJsonObject().get("track").getAsJsonObject();
                JsonObject lastStatJson = track.get("z0").getAsJsonObject();
                Boolean delivered = track.get("e").toString().equals("40");

                StringBuilder lastStatus = new StringBuilder(lastStatJson.get("a").getAsString()).append(", ");
                lastStatus.append(lastStatJson.get("c").getAsString()).append(", ").append(lastStatJson.get("d").getAsString());
                lastStatus.append(", ").append(lastStatJson.get("z").getAsString());


                JsonArray tracking = track.get("z1").getAsJsonArray();
                Parcel newParcel = new Parcel(trackNum, lastStatus.toString(), new Date(), delivered);
                Set<TrackParcel> trackParcel = new HashSet<>();

                for (JsonElement trackStatus : tracking) {
                    JsonObject statusJson = trackStatus.getAsJsonObject();
                    String updDate = statusJson.get("a").getAsString();

                    StringBuilder stat = new StringBuilder(statusJson.get("c").getAsString()).append(", ");
                    stat.append(statusJson.get("d").getAsString()).append(", ").append(statusJson.get("z").getAsString());

                    try {
                        trackParcel.add(new TrackParcel(stat.toString(), formatter.parse(updDate)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    System.out.println(stat);
                }
                newParcel.setTrackParcels(trackParcel);
                saveParcel(newParcel);
            }

        }

    }

    private void saveParcel(Parcel parcel) {
        parcel.setParcelId(parcelRepository.findFirstByTrackNumber(parcel.getTrackNumber()).getParcelId());
        Set<TrackParcel> oldTrackParcel = trackParcelRepository.findAllByParcelOrderByTrackParcelIdDesc(parcel.getParcelId());
        Set<TrackParcel> newTrackParcel = parcel.getTrackParcels();
        if (oldTrackParcel != null) {
            newTrackParcel.removeAll(oldTrackParcel);
        }
        if (newTrackParcel.size() > 0) {
            newTrackParcel.stream().sorted(new TrackParcelComparator().reversed()).forEach(status -> {
                status.setParcel(parcel);
                status.setTrackParcelId(trackParcelRepository.save(status).getTrackParcelId());
            });

            parcel.setTrackParcels(trackParcelRepository.findAllByParcelOrderByTrackParcelIdDesc(parcel.getParcelId()));
            parcelRepository.saveAndFlush(parcel);
        } else {
            parcel.setTrackParcels(oldTrackParcel);
            parcelRepository.saveAndFlush(parcel);
        }

    }


    private String sendPost(String url, Map<String, String> headerParams, List<String> bodyParams, String mothod) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestMethod(mothod);

            for (Map.Entry<String, String> entry : headerParams.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
            conn.connect();

            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

            StringBuffer sbBody = new StringBuffer();
            for (String str : bodyParams) {
                sbBody.append(str);
            }
            out.write(sbBody.toString());

            out.flush();

            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    public static String sendGet(String url, Map<String, String> headerParams, String mothod) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);

            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();

            connection.setRequestMethod(mothod);

            for (Map.Entry<String, String> entry : headerParams.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            connection.connect();

            Map<String, List<String>> map = connection.getHeaderFields();

            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }

            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("Exception " + e);
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }


}



