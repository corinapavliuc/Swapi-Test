package tests;

import dto.Film;
import dto.Person;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static tests.BaseTest.getRequest;

public class GetUsersListTest {
    @Test
    public void getVaderInfo(){
        List<Person> people = getRequest("/people/?search=Vader", 200)
                .body().jsonPath().getList("results", Person.class);
        people.forEach(person -> assertTrue(person.getName().contains("Vader")));

        List<String> filmUrls =new ArrayList<>();
        people.forEach(person -> filmUrls.addAll(person.getFilms()));

        String filmWithLeastPlanets =null;
        int leastPlanetsCount =Integer.MAX_VALUE;

        for (String filmUrl: filmUrls){
            Response filmResponse = getRequest(filmUrl,200);
            Film film = filmResponse .getBody().as(Film.class);
            int planetCount = film.getPlanets().size();

            if(planetCount < leastPlanetsCount){
                leastPlanetsCount =planetCount;
                filmWithLeastPlanets = film.getTitle();

            }
        }
        System.out.println("Фильм с наименьшим количеством планет: "+filmWithLeastPlanets);
        System.out.println("Количество планет в этом фильме: "+leastPlanetsCount);;
//        System.out.println(people.get(0).getName());
//        //Check that name contains "Vader"
//   for (Person person: people){
//       assertTrue(person.getName().contains("Vader"));
//   }
     //   Using previous response (1) find which film that Darth Vader joined has the less planets.
    }


//        Response response = RestAssured.given()
//                .baseUri("https://swapi.dev/api/people/")
//                .queryParam("search", "Vader")
//                .get();
//        response.then().assertThat().statusCode(200);
//        assertTrue(response.body().asString().contains("Vader"));
    }
