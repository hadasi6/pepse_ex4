package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A class that represents a cloud in the game world.
 */
public class Cloud {
    // The base color of the cloud
    private static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);
    private static final Random random = new Random();
//    private static


    /**
     * Creates a cloud in the game world.
     *
     * @param topLeftCorner    the top-left corner of the cloud
     * @param windowDimensions the dimensions of the window
     * @param cycleLength      the length of the cloud's transition cycle
     * @param gameObjects      the collection of game objects
     * @param seed             the seed for the random number generator
     * @return a list of blocks that represent the cloud
     */
    public static List<Block> create(Vector2 topLeftCorner, Vector2 windowDimensions, float cycleLength, int seed) {
        random.setSeed(seed);
        List<Block> cloudBlocks = new ArrayList<>();
        List<List<Integer>> cloudPattern = List.of(
                List.of(0, 1, 1, 0, 0, 0),
                List.of(1, 1, 1, 0, 1, 0),
                List.of(1, 1, 1, 1, 1, 1),
                List.of(1, 1, 1, 1, 1, 1),
                List.of(0, 1, 1, 1, 0, 0),
                List.of(0, 0, 0, 0, 0, 0)
        );

        for (int i = 0; i < cloudPattern.size(); i++) {
            for (int j = 0; j < cloudPattern.get(i).size(); j++) {
                if (cloudPattern.get(i).get(j) == 1) {
                    // Calculate block position
                    Vector2 position = topLeftCorner.add(new Vector2(j * Block.SIZE, i * Block.SIZE));

                    // Create the block
                    RectangleRenderable renderable = new RectangleRenderable(
                            ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR)
                    );
                    Block cloudBlock = new Block(position, renderable);
                    cloudBlock.setTag("CloudBlock");

                    // Define the start and end points for this block's transition
                    Vector2 start = position;
                    Vector2 end = new Vector2(windowDimensions.x() + j * Block.SIZE, position.y());

                    // Add a transition for this block
                    new Transition<>(
                            cloudBlock,
                            cloudBlock.transform()::setTopLeftCorner,
                            start,
                            end,
                            Transition.LINEAR_INTERPOLATOR_VECTOR,
                            cycleLength,
                            Transition.TransitionType.TRANSITION_LOOP,
                            null
                    );

                    cloudBlocks.add(cloudBlock);
                }
            }
        }

        return cloudBlocks;
    }

    /**
     * Creates rain in the game world.
     *
     * @param cloudPosition the position of the cloud
     * @param numberOfDrops the number of rain drops to create
     * @param gameObjects   the collection of game objects
     */
    public static void createRain(Vector2 cloudPosition, int numberOfDrops,
                                  Consumer<GameObject> addGameObject,
                                  Consumer<GameObject> removeGameObject) {
        System.out.println("Creating rain...");
        float cloudWidth = 6 * Block.SIZE; // Assuming the cloud is 6 blocks wide
        float cloudHeight = 6 * Block.SIZE; // Assuming the cloud is 6 blocks high
        for (int i = 0; i < numberOfDrops; i++) {
//            float randomX = cloudPosition.x() + random.nextInt((int) cloudWidth);
//            float randomY = cloudPosition.y() + cloudHeight + random.nextInt(50); // Adjust the range as
//            needed
//            Vector2 dropPosition = new Vector2(randomX, randomY);
            Vector2 dropPosition = cloudPosition.add(new Vector2(random.nextInt(180),
                    random.nextInt(150)));
            RainDrop rainDrop = new RainDrop(dropPosition, new Vector2(5, 10), removeGameObject);
            rainDrop.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

            addGameObject.accept(rainDrop);
        }
    }
}