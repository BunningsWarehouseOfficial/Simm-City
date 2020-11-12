package curtin.krados.simmcity.model;

public class GameSchema {
    public static class GameDataTable { //TODO Does game over status need to be in database? What if you leave the game in game over and re-enter?
        public static final String NAME = "game"; //Table name
        public static class Cols {                //Column names
            public static final String ID = "id";
            public static final String CITY_NAME = "city_name";
            public static final String MAP_WIDTH = "map_width";
            public static final String MAP_HEIGHT = "map_height";
            public static final String INITIAL_MONEY = "initial_money";
            public static final String TAX_RATE = "tax_rate";
            public static final String GAME_TIME = "game_time";
            public static final String MONEY = "money";
            public static final String NUM_RESIDENTIAL = "num_residential";
            public static final String NUM_COMMERCIAL = "num_commercial";
        }
    }
    //TODO Another table here
}
