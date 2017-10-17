# PopularMovie2
All the data is fetched from https://www.themoviedb.org

This project consist of following features:

1. Movie posters are displayed on main screen according to sorting criteria like "popular movies", "top rated" or "favorite".

2. Favorite movies are stored in database so that if user is offline he/she can check its details.

3. On tap of movie poster it will take you to detail screen where movie's details such as 
   release date , rating , plot synopsis , trailers , reviews etc.

4. You can toggle between favorite/unfavorite movie by clicking on favorite button on action bar.

5. Multiscreen support.

Note: If you want to run it on device please ensure to replace your TMDB api key in "CommonUtility.java" file.

Thanks!


 ` /** A simple endpoint method to provide a joke */
    @ApiMethod(name = "getJoke")
    public MyBean getJoke(){
        MyBean response = new MyBean();
        response.setData(JokeProvider.getJoke());

        return response;
    }`
