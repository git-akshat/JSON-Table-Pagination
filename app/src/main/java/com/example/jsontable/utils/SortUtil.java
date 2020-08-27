package com.example.jsontable.utils;

import com.example.jsontable.model.Airport;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortUtil implements Comparator<Airport> {
    private static int SORT_FACTOR = -1;
    private static boolean sort_asc = true;

    public static void sortByCity(List<Airport> data) {
        if (SORT_FACTOR == 0 && sort_asc == false) {
            Collections.sort(data, Collections.reverseOrder(new SortUtil()));
            sort_asc = true;
        } else {
            SORT_FACTOR = 0;
            Collections.sort(data, new SortUtil());
            sort_asc = false;
        }
    }

    public static void sortByName(List<Airport> data) {
        if (SORT_FACTOR == 1 && sort_asc == false) {
            Collections.sort(data, Collections.reverseOrder(new SortUtil()));
            sort_asc = true;
        } else {
            SORT_FACTOR = 1;
            Collections.sort(data, new SortUtil());
            sort_asc = false;
        }
    }

    public static void sortByCode(List<Airport> data) {
        if (SORT_FACTOR == 2 && sort_asc == false) {
            Collections.sort(data, Collections.reverseOrder(new SortUtil()));
            sort_asc = true;
        } else {
            SORT_FACTOR = 2;
            Collections.sort(data, new SortUtil());
            sort_asc = false;
        }
    }

    public static void sortByCountry(List<Airport> data) {
        if (SORT_FACTOR == 3 && sort_asc == false) {
            Collections.sort(data, Collections.reverseOrder(new SortUtil()));
            sort_asc = true;
        } else {
            SORT_FACTOR = 3;
            Collections.sort(data, new SortUtil());
            sort_asc = false;
        }
    }

    @Override
    public int compare(Airport a1, Airport a2) {
        switch (SORT_FACTOR) {
            case 0:
            default:
                return a1.getCity().compareToIgnoreCase(a2.getCity());
            case 1:
                return a1.getAirport().compareToIgnoreCase(a2.getAirport());
            case 2:
                return a1.getCode().compareToIgnoreCase(a2.getCode());
            case 3:
                return a1.getCountry().compareToIgnoreCase(a2.getCountry());
        }
    }
}
