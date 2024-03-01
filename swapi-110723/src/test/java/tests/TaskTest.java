package tests;

import dto.Film;
import dto.Person;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.lang.ref.SoftReference;
import java.util.*;


import static tests.BaseTest.getRequest;


public class TaskTest {
@Test
    public  void lessPlanets(){
        List<Person> people =getRequest("https://swapi.dev/api/people/?search=Vader", 200)
                .body().jsonPath().getList("results",Person.class);
        List<String> films = people.get(0).getFilms();
        films.forEach(x -> System.out.println(x)) ;
        Map<String,Integer> filmPlanetsQuantity = new HashMap<>();
        for (String film :films){
            Film filmInfo =getRequest(film,200)
                    .body().jsonPath().getObject("",Film.class);
    filmPlanetsQuantity.put(filmInfo.getTitle(),filmInfo.getPlanets().size());

        }
        String minFilmName = (Collections.min(filmPlanetsQuantity.entrySet(), Map.Entry.comparingByValue()).getKey());
        System.out.println(minFilmName);
        System.out.println(filmPlanetsQuantity.get(minFilmName));

    }
//zadacea
    //Using prebios responses (1) verifity if Vatder 's starship is on film with id 1
    @Test
    public void verifyVaderStarshipInFilmWothId(){
        Response vaderResponse = RestAssured.get("https://swapi.dev/api/people/?search=Vader");
        List<String> vaderStarshipUrls =vaderResponse.jsonPath().getList("results[0].starships");

        Response filmResponse =RestAssured.get("https://swapi.dev/api/films/1");
        List<String> filmStraships =filmResponse.jsonPath().getList("starships");
                boolean isVaderShipInFilmWithId1= false;

        for (String vaderStarship :vaderStarshipUrls){
            if (filmStraships.contains(vaderStarship)){
                isVaderShipInFilmWithId1=true;
                break;
            }
        }
        String resultMessage =isVaderShipInFilmWithId1 ? "да" : "нет";
        System.out.println("корабль Вейдера пристствует в фильме с I0 1: " + resultMessage);
    }
    @Test
    public void verifyVaderStarshipInFilmWithLeastPlanets() {
        List<Person> people = getRequest("/people/?search=Vader", 200)
                .body().jsonPath().getList("results", Person.class);

        //people.forEach(person -> assertTrue(person.getName().contains("Vader")));

        List<String> filmUrls = new ArrayList<>();
        people.forEach(person -> filmUrls.addAll(person.getFilms()));

        String filmWithLeastPlanets = null;
        int leastPlanetFilmId = 0;
        int leastPlanetsCount = Integer.MAX_VALUE;
        String filmWithLeastPlanetsUrl = "";

        for (String filmUrl : filmUrls) {
            Response filmResponse = getRequest(filmUrl, 200);
            Film film = filmResponse.getBody().as(Film.class);
            int planetCount = film.getPlanets().size();

            if (planetCount < leastPlanetsCount) {
                leastPlanetsCount = planetCount;
                filmWithLeastPlanetsUrl = filmUrl;
                leastPlanetFilmId = film.getEpisode_id();


            }

            System.out.println("911");
            Response filmInfoLessPlanets = getRequest(filmWithLeastPlanetsUrl, 200);
            Film filmLessPlanets = filmInfoLessPlanets.getBody().as(Film.class);
            List<String> starshipsInFilm = filmLessPlanets.getStarships();


            List<String> vaderStarships = new ArrayList<>();
            people.forEach(person -> vaderStarships.addAll(person.getStarships()));

            for (String starship : vaderStarships) {
                if (starshipsInFilm.contains(starship)) {
                    System.out.println("Корабль Вайдера присутствует в фильме? Да");

                } else {System.out.println("Корабль Вайдера присутствует в фильме? Нет");
                }

                System.out.println("Ид фильма = " + leastPlanetFilmId);
            }

        }
    }
}