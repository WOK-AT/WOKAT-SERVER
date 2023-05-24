package com.sopt.wokat.domain.place.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sopt.wokat.domain.place.entity.SpaceInfo;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Hidden
public class OnePlaceInfoResponse {

    private String id;
    private String category;
    private String placeName;
    private String count;
    private List<String> hashtags;
    private List<String> introduce;
    private Map<String, Object> operationHours;
    private Map<String, Object> information;
    private String location;
    private List<String> imageURLs;
    private Map<String, Object> distance;
    
    public static OnePlaceInfoResponse creatOnePlaceInfoResponse (String category, SpaceInfo spaceInfo) {
        return OnePlaceInfoResponse.builder()
                    .id(spaceInfo.getId())
                    .category(category)
                    .placeName(spaceInfo.getName())
                    .count(spaceInfo.getHeadCount())
                    .hashtags(hashTags(spaceInfo.getHashTags()))
                    .introduce(spaceInfo.getIntroduction())
                    .location(spaceInfo.getLocationRoadName())
                    .imageURLs(spaceInfo.getImageURLs())
                    .operationHours(getOperationHours(spaceInfo.getOpenTime()))
                    .information(getInformation(
                        spaceInfo.getContact(),
                        spaceInfo.getHomepageURL(),
                        spaceInfo.getWifi(),
                        spaceInfo.getSocket(),
                        spaceInfo.getParkingLot(),
                        spaceInfo.getHdmiScreen()
                    ))
                    .distance(spaceInfo.getDistance())
                    .build();
    }

    public static List<String> hashTags(List<String> hashtags) {
        List<String> result = hashtags.get(0).equals("") ? new ArrayList<>() : hashtags;
        return result;
    }
    /*
     *  {
	 *	    "open": {
	 *			"08:00 ~ 22:00" : "화, 목, 금", 
	 *		    "09:00 ~ 18:00" : "토, 일"
	 *		},
	 *		"closed": ["월요일"]
	 *	}
     */
    public static Map<String, Object> getOperationHours(Map<String, Object> openTime) {
        Map<String, Object> timeHashMap = transformData(openTime);
        return timeHashMap;
    }

    public static Map<String, Object> transformData(Map<String, Object> obj) {
        Map<String, Object> result = new HashMap<>();

        Map<String, List<String>> openDays = new HashMap<>();
        List<String> closedDays = new ArrayList<>();

        for (Map.Entry<String, Object> entry : obj.entrySet()) {
            String day = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String) {
                String time = (String) value;

                if (time.equals("휴관")) closedDays.add(day);
                else {
                    if (!openDays.containsKey(time)) {
                        openDays.put(time, new ArrayList<>());
                    }
                    openDays.get(time).add(day);
                }
            }
        }

        if (!openDays.isEmpty()) result.put("open", openDays);
        if (!closedDays.isEmpty()) result.put("closed", closedDays);

        return result;
    }
    /*
     *   {
	 *		"contact": ["000-0000-0000"]
	 *		"homepage": "www.wokat.com",
	 *		"wi-fi": {
	 *			"ID": ["KT_GIGA_2G_Wave3_B829"],
	 *			"PW": ["123456789"]
	 *		},
	 *		"socket": true,
	 *		"parking": true,
	 *		"hdmi-screen": true
	 *	}
     */
    public static Map<String, Object> getInformation(List<String> contact, String homepageURL, 
                Map<String, Object> wiFiMap, String socket, String parkingLot, String hdmiScreen) {
        Map<String, Object> wifiHashMap = null;
        if (wiFiMap != null) {
            wifiHashMap = makeHashMap(
        "ID", wiFiMap.get("ID"),
                "PW", wiFiMap.get("PW")
            );
        }


        Map<String, Object> infoHashMap = makeHashMap(
    "contact", contact,
            "homepage", homepageURL,
           // "socket", socket != null && socket.equals("true") ? true : (socket == null ? null : false),
            "socket", socket != null ? socket : false,
          // "parking", parkingLot != null && parkingLot.equals("true") ? true : (socket == null ? null : false),
            "parking", parkingLot != null ? parkingLot : false,
            "hdmi-screen", hdmiScreen,
            "wi-fi", wifiHashMap
        );

        return infoHashMap;
    }

    public static Map<String, Object> makeHashMap(Object... args ) {
        Map<String, Object> infoMap = new HashMap<>();
        if (args.length % 2 != 0) throw new IllegalArgumentException("Invalid number of arguments");
    
        for (int i = 0; i < args.length; i += 2) {
            String key = String.valueOf(args[i]);
            Object value = args[i + 1];
            infoMap.put(key, value);
        }

        return infoMap;
    }

}
