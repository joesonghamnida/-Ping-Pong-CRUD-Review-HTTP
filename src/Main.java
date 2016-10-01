import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by joe on 27/09/2016.
 */
public class Main {

    static HashMap<String, Player> players = new HashMap<>();
    static ArrayList<Game> games = new ArrayList<>();
    static String playerNameSet;

    public static void main(String[] args) {

        Spark.staticFileLocation("/resources");
        Spark.init();

        Spark.get("/", ((request, response) -> {
            Session session = request.session();
            String playerName = session.attribute("playerName");

            for(Game owner: games){
                if(owner.gameOwner.equalsIgnoreCase(playerName)){
                    owner.modifyRecord = "allowed";
                }
                else{
                    owner.modifyRecord = null;
                }
            }

            Player player = players.get(playerName);

            HashMap m = new HashMap();
            if(player!=null){
                m.put("playerName", player.name);
            }
            m.put("games", games);
            return new ModelAndView(m, "home.html");

        }),new MustacheTemplateEngine());

        Spark.post("/login", ((request, response) -> {
            String name = request.queryParams("playerName");
            playerNameSet = name;

            Player player = players.get(name);
            if(player==null){
                player = new Player(name);
                players.put(name, player);
            }

            Session session = request.session();
            session.attribute("playerName", player.name);

            response.redirect("/");
            return "";
        }));

        Spark.post("/logout", ((request, response) -> {
            Session session = request.session();
            session.invalidate();
            response.redirect("/");
            return "";
        }));

        Spark.post("/enter-game", ((request, response) -> {

            Session session = request.session();
            String gameOwner = session.attribute("playerName");
            String playerOne = request.queryParams("playerOne");

            Game game = new Game(games.size(), gameOwner ,playerOne);

            games.add(game);

            response.redirect("/");
            return "";
        }));

        Spark.post("/delete-game", ((request, response) -> {

            String gameOwner = request.queryParams("gameOwner");

            //need this to avoid concurrent modification
            Game gameToDelete = new Game();

            for(Game game : games){
                if(game.gameOwner.equalsIgnoreCase(gameOwner)){
                    gameToDelete = game;
                }
            }
            games.remove(gameToDelete);

            response.redirect("/");
            return "";
        }));

        Spark.get("/edit-game", ((request, response) -> {

            Session session = request.session();
            String playerName = session.attribute("playerName");
            int gameId = Integer.parseInt(request.queryParams("gameId"));
            session.attribute("gameId", gameId);

            HashMap m = new HashMap();

            for(Game owner: games){
                if(owner.gameOwner.equalsIgnoreCase(playerName)){
                    owner.modifyRecord = "allowed";
                }
                else{
                    owner.modifyRecord = null;
                }
            }

            m.put("playerName", games.get(gameId));
            return new ModelAndView(m, "editInfo.html");
        }),new MustacheTemplateEngine());

        Spark.post("/edit-game", ((request, response) -> {

            Session session = request.session();
            String playerName = session.attribute("playerName");
            int gameId = session.attribute("gameId");

            String playerOne = request.queryParams("playerOne");

            Game game = new Game(games.size(), playerName, playerOne);
            games.set(gameId, game);

            response.redirect("/");
            return "";
        }));
    }
}
