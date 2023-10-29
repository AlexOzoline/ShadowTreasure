import bagel.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.File;


/**
 * An example Bagel game.
 */
public class ShadowTreasure extends AbstractGame {
    /**
     * Player object in the game
     */
    private Player player;
    /**
     * Bullet used to shoot the zombies with
     */
    private Bullet bullet = new Bullet();
    /**
     * Arraylists used to store the entities that can have multiple instances
     */
    private ArrayList<Zombie> zombies = new ArrayList<Zombie>();
    private ArrayList<Sandwich> sandwiches = new ArrayList<Sandwich>();
    /**
     * treasure object which acts as the main goal
     */
    private Treasure treasure;
    /**
     * background image of the game
     */
    private Image background = new Image("res/images/background.png");
    /**
     * Custom font used to display the player's energy
     */
    private Font message;
    /**
     * DrawOptions object used to set the energy text to black
     */
    private DrawOptions black = new DrawOptions();
    /**
     * Constant representing the maximum distance between two 'touching' entities
     */
    private static final double TOUCHING_DISTANCE = 50;
    /**
     * Stores the information about the entities found in the environment file
     */
    private static ArrayList<String> inputs = new ArrayList<>();
    /**
     * A counter to track which frame the game is on
     */
    private static int frame;
    /**
     * The number of frames in each game update
     */
    private static final int TICK_RATE = 10;
    /**
     * The energy gained from the player consuming a sandwich
     */
    private static final int SANDWICH_ENERGY_INCREASE = 5;
    /**
     * The energy lost from firing a bullet at a zombie
     */
    private static final int BULLET_ENERGY_DECREASE = 3;
    /**
     * The maximum range from a zombie the player can fire a bullet from
     */
    private static final int SHOOTING_RANGE = 150;
    /**
     * The size of the font used to display the player's energy
     */
    private static final int FONT_SIZE = 20;
    /**
     * The radius from the zombie which counts as the bullet hitting said zombie
     */
    private static final int BULLET_HIT_RANGE = 25;
    /**
     * The number of zombies still alive in the game
     */
    private static int zombieCount;
    /**
     * The number of not eaten sandwiches in the game
     */
    private static int sandwichCount;
    /**
     * The current zombie that the bullet is travelling towards
     */
    private static Zombie bulletGoal = null;
    /**
     * File path for output.csv
     */
    private static File file = new File("res/IO/output.csv");
    /**
     * For storing the movement of the bullet
     */
    private static ArrayList<String> outputs = new ArrayList<String>();


    // for rounding double number; use this to print the location of the player
    private static DecimalFormat df = new DecimalFormat("0.00");

    public static void printInfo(double x, double y, int e) {
        System.out.println(df.format(x) + "," + df.format(y) + "," + e);
    }

    /**
     * Creates the objects for the ShadowTreasure game
     */
    public ShadowTreasure() {
        super();
        this.message = new Font("res/font/DejaVuSans-Bold.ttf", FONT_SIZE);
        this.black.setBlendColour(0,0,0);
        int i;
        this.loadEnvironment("res/IO/environment.csv");
        // Add code to initialize other attributes as needed
        // Setting the positions of each entity and energy level of player
        for(i=0;i<inputs.size();i++) {
            String[] testing = new String[4];
            // remove commas from the csv environment input
            testing = inputs.get(i).split(",");
            // remove special characters from the string
            String tmp = testing[0].replaceAll("[^a-zA-Z0-9]", "");
            // fill in the entity information from the data
            if(tmp.equals("Player")) {
                this.player = new Player(Double.parseDouble(testing[1]),
                        Double.parseDouble(testing[2]),Integer.parseInt(testing[3]) );
            }
            else if(tmp.equals("Zombie")) {
                zombies.add(new Zombie(Double.parseDouble(testing[1]), Double.parseDouble(testing[2])));
            }
            else if (tmp.equals("Sandwich")){
                sandwiches.add(new Sandwich(Double.parseDouble(testing[1]), Double.parseDouble(testing[2])));
            }
            //The only entity type left is treasure, so it must be the treasure's location
            else {
                this.treasure = new Treasure(Double.parseDouble(testing[1]), Double.parseDouble(testing[2]));
            }
        }
        // Count the number of zombies and sandwiches for the given environment and create the
        zombieCount = zombies.size();
        sandwichCount = sandwiches.size();

    }

    /**
     * Load from input file
     */
    private void loadEnvironment(String filename) {
        // Code here to read from the file and set up the environment
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                // put each line in an array to be read later
                inputs.add(line);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Performs a state update.
     */
    @Override
    public void update(Input input) {
        // Logic to update the game, as per specification must go here
        // check to see if the program needs to terminate due to manual input, writing output to output.csv
        if (input.wasPressed(Keys.ESCAPE)) {
            writeToOutput(outputs);
            Window.close();
        }
        // check to see we are on the right tick for an update to occur
        if (frame == TICK_RATE) {
            // check to see if the program needs to terminate due to manual input, writing
            // check to see if the player has met the treasure and whether all zombies are dead
            if (player.getPosition().distanceTo(treasure.getPosition()) < TOUCHING_DISTANCE && zombieCount == 0) {
                // write bullet positions to output.csv and end the game in success
                writeToOutput(outputs);
                System.out.println(player.getEnergy() + ",success");
                Window.close();
            }
            // check if the player's energy is too low to fight, with no sandwiches left, no bullet active,
            // but zombies are still alive
            if (player.getEnergy() < BULLET_ENERGY_DECREASE && sandwichCount == 0 &&
                    zombieCount > 0 && !bullet.getIsFired()) {
                // write bullet positions to output.csv and end the game in failure
                writeToOutput(outputs);
                System.out.println(player.getEnergy());
                Window.close();
            }
            // check to see if the player should interact with entities
            for (Sandwich sandwich: sandwiches) {
                if (player.getPosition().distanceTo(sandwich.getPosition()) < TOUCHING_DISTANCE
                        && !sandwich.getIsEaten()) {
                    // player eats sandwich gaining energy, and the sandwich is no longer rendered
                    player.setEnergy(player.getEnergy() + SANDWICH_ENERGY_INCREASE);
                    sandwich.setIsEaten(true);
                    sandwichCount--;
                }
            }
            for (Zombie zombie: zombies) {
                if (player.getPosition().distanceTo(zombie.getPosition()) < SHOOTING_RANGE
                        && !zombie.getShotDead() && !bullet.getIsFired()) {
                    // shoot the bullet at the zombie the player is close to, and decrease energy
                    player.setEnergy(player.getEnergy() - BULLET_ENERGY_DECREASE);
                    bullet.setPosition(player.getPosition().x, player.getPosition().y);
                    // store the target zombie for the bullet to continue moving towards
                    bulletGoal = zombie;
                    bullet.setIsFired(true);
                }
            }
            // update the bullet's movement when it has been shot at a zombie
            if (bullet.getIsFired() && bulletGoal != null) {
                bullet.moveTo(bulletGoal.getPosition());
                // storing it's new position to later write to output.csv
                outputs.add(df.format(bullet.getPosition().x) + "," + df.format(bullet.getPosition().y));
            }
            // decide where the player should move
            // most simple case, move to treasure when all zombies are dead
            if (zombieCount == 0) {
                player.moveTo(treasure.position);
            }
            // check if the player has energy to fight or is in a niche case.
            // e.g last zombie alive or no sandwiches left
            else if (player.getEnergy() >= BULLET_ENERGY_DECREASE || (zombieCount == 1 && bullet.getIsFired())
                || (sandwichCount == 0 && bullet.getIsFired())) {
                // determine the closest zombie and start moving there
                Zombie closestZombie = null;
                for (Zombie zombie: zombies) {
                    // Assuring that we have a first zombie to compare the others to
                    if (!zombie.getShotDead() && closestZombie == null) {
                        closestZombie = zombie;
                    }
                    if (!zombie.getShotDead() && player.getPosition().distanceTo(zombie.getPosition())
                            < player.getPosition().distanceTo(closestZombie.getPosition())) {
                        // Replace the closest zombie with the new closer zombie
                        closestZombie = zombie;
                    }
                }
                // Now we are sure of which zombie is closest the player can move to it
                 player.moveTo(closestZombie.getPosition());
            }
            // player needs more energy so should find the nearest sandwich
            else {
                Sandwich closestSandwich = null;
                for (Sandwich sandwich: sandwiches) {
                    if(!sandwich.getIsEaten() && closestSandwich == null) {
                        closestSandwich = sandwich;
                    }
                    if (!sandwich.getIsEaten() && player.getPosition().distanceTo(sandwich.getPosition())
                            < player.getPosition().distanceTo(closestSandwich.getPosition())) {
                        closestSandwich = sandwich;
                    }
                }
                // Defensive check to protect against no sandwiches and low energy game states
                if (sandwichCount > 0) {
                    player.moveTo(closestSandwich.getPosition());
                }
            }
            // check to see if the bullet has hit a zombie
            if (bulletGoal!= null && bullet.getIsFired() &&
                    bullet.getPosition().distanceTo(bulletGoal.getPosition()) < BULLET_HIT_RANGE) {
                // update the state of the zombie and bullet
                bulletGoal.setShotDead(true);
                bullet.setIsFired(false);
                zombieCount--;
            }
                frame = 0;
        }
        // render the background and entities to the screen
        background.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        player.drawEntity();
        if (bullet.getIsFired()) {
            bullet.drawEntity();
        }
        treasure.drawEntity();
        // Render the non dead zombies
        for (Zombie zombie: zombies) {
            if (!zombie.getShotDead()) {
                zombie.drawEntity();
            }
        }
        // Render the non-eaten sandwiches
        for (Sandwich sandwich: sandwiches) {
            if (!sandwich.getIsEaten()) {
                sandwich.drawEntity();
            }
        }
        // Display energy to the screen and go to next frame
        message.drawString("energy: " + player.getEnergy(), 20, 760, black);
        frame++;
    }


    /**
     * writes the bullet's movements to output.csv
     * @param outputs ArrayList of all positions the bullet has taken during this test
     */
    public static void writeToOutput(ArrayList<String> outputs) {
        try (FileWriter csvWriter = new FileWriter(file)) {
            // write each line to the file
            for (String line: outputs) {
                csvWriter.append(line);
                csvWriter.append("\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * The entry point for the program.
     */
    public static void main(String[] args) throws IOException {
        ShadowTreasure game = new ShadowTreasure();
        game.run();
    }
}
