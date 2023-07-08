package com.sopt.wokat.domain.place.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    private String bookingURL;
    private List<String> hashtags;
    private String introduce;
    private Map<String, Object> operationHours;
    private Map<String, Object> information;
    private String location;
    private List<String> imageURLs;
    private String distance;
    
    public static OnePlaceInfoResponse createOnePlaceInfoResponse (SpaceInfo spaceInfo, String walkDistance) {
        return OnePlaceInfoResponse.builder()
                    .id(spaceInfo.getId())
                    .category(spaceInfo.getSpace().getValue())
                    .placeName(spaceInfo.getName())
                    .count(spaceInfo.getHeadCount())
                    .bookingURL(spaceInfo.getBookingURL())
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
                    .distance(walkDistance)
                    .build();
    }

    public static List<String> hashTags(List<String> hashtags) {
        List<String> result;
        if (hashtags.size() != 0) {
            result = hashtags.get(0).equals("") ? new ArrayList<>() : hashtags;
        } else {
            result = new ArrayList<>();
        }
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

                if (time.equals("휴관") || time.contains("휴관")) closedDays.add(day);
                else {
                    if (!openDays.containsKey(time)) {
                        openDays.put(time, new ArrayList<>());
                    }
                    openDays.get(time).add(day);
                }
            }
        }

        //? 요일묶음 내 요일 정렬
        List<String> daysOfWeek = Arrays.asList("월", "화", "수", "목", "금", "토", "일");
        openDays.values().forEach(dayList -> dayList.sort(Comparator.comparingInt(daysOfWeek::indexOf)));
        closedDays.sort(Comparator.comparingInt(daysOfWeek::indexOf));

        //? 운영시간 시간끼리 요일 정렬 
        List<Map.Entry<String, List<String>>> sortedHours = new ArrayList<>(openDays.entrySet());

        sortedHours.sort(Comparator.comparingInt(entry -> {
            String day = entry.getValue().get(0);
            String targetDays = "월화수목금토일";
            return targetDays.indexOf(day);
        }));

        Map<String, List<String>> sortedOpenDays = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> entry : sortedHours) {
            sortedOpenDays.put(entry.getKey(), entry.getValue());
        }

        result.put("open", sortedOpenDays);
        result.put("closed", closedDays);

        return result;
    }

    //! 운영시간 정렬 (오픈시간 빠른 순서 -> 같은 경우, 마감시간 빠른 순서)
    private static int returnTimeOrder(String time1, String time2) {
        String[] startTime1 = time1.split(" - ");
        String[] startTime2 = time2.split(" - ");

        int startHour1 = Integer.parseInt(startTime1[0].split(":")[0]);
        int startHour2 = Integer.parseInt(startTime2[0].split(":")[0]);

        int startMinute1 = Integer.parseInt(startTime1[0].split(":")[1]);
        int startMinute2 = Integer.parseInt(startTime2[0].split(":")[1]);

        int endHour1 = Integer.parseInt(startTime1[1].split(":")[0]);
        int endHour2 = Integer.parseInt(startTime2[1].split(":")[0]);

        int endMinute1 = Integer.parseInt(startTime1[1].split(":")[1]);
        int endMinute2 = Integer.parseInt(startTime2[1].split(":")[1]);

        if (startHour1 != startHour2) {
            return Integer.compare(startHour1, startHour2);
        } else if (startMinute1 != startMinute2) {
            return Integer.compare(startMinute1, startMinute2);
        } else if (endHour1 != endHour2) {
            return Integer.compare(endHour1, endHour2);
        } else {
            return Integer.compare(endMinute1, endMinute2);
        }
    }

    /*
     *   {
	 *		"contact": ["000-0000-0000"]
	 *		"homepageURL": "www.wokat.com",
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
            "homepageURL", homepageURL,
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
