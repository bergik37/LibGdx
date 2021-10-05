import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class LevelScreen extends BaseScreen {

    int score;
    private Spaceship spaceship;
    private Coins coins;
    private Battarey battarey;
    private Rock rock;
    private boolean gameOver;
    private int perhaps;

    public boolean keyDown(int keycode) {
        if (keycode == Keys.X)
            spaceship.warp();
        if (keycode == Keys.SPACE)
            spaceship.shoot();
        return false;

    }

    public void initialize() {
        BaseActor space = new BaseActor(0, 0, mainStage);
        space.loadTexture("assets/space.png");
        space.setSize(800, 600);
        BaseActor.setWorldBounds(space);
        spaceship = new Spaceship(400, 300, mainStage);
        for (int i = 0; i < 25; i++) {
            rock = new Rock(MathUtils.random(0, 1280), MathUtils.random(0, 768), mainStage);
        }

        gameOver = false;
    }

    public void update(float dt) {
        for (BaseActor rockActor : BaseActor.getList(mainStage, "Rock")) {
            if (rockActor.overlaps(spaceship)) {
                if (spaceship.shieldPower <= 0) {
                    Explosion boom = new Explosion(0, 0, mainStage);
                    boom.centerAtActor(spaceship);
                    spaceship.remove();
                    spaceship.setPosition(-1000, -1000);
                    BaseActor messageLose = new BaseActor(0, 0, uiStage);
                    messageLose.loadTexture("assets/message-lose.png");
                    messageLose.centerAtPosition(400, 300);
                    messageLose.setOpacity(0);
                    messageLose.addAction(Actions.fadeIn(1));
                    gameOver = true;
                } else {
                    spaceship.shieldPower -= 34;
                    Explosion boom = new Explosion(0, 0, mainStage);
                    boom.centerAtActor(rockActor);
                    rockActor.remove();
                    perhaps = MathUtils.random(0, 10);
                    if (perhaps < 5) {
                        coins = new Coins(0, 0, mainStage);
                        coins.centerAtActor(rockActor);
                    } else if ((perhaps >= 5) && (perhaps < 7)) {
                        battarey = new Battarey(0, 0, mainStage);
                        battarey.centerAtActor(rockActor);
                    }
                }

            }

            for (BaseActor laserActor : BaseActor.getList(mainStage, "Laser")) {
                if (laserActor.overlaps(rockActor)) {
                    Explosion boom = new Explosion(0, 0, mainStage);
                    boom.centerAtActor(rockActor);
                    laserActor.remove();
                    rockActor.remove();
                    perhaps = MathUtils.random(0, 10);
                    if (perhaps < 5) {
                        coins = new Coins(0, 0, mainStage);
                        coins.centerAtActor(rockActor);
                    } else if ((perhaps >= 5) && (perhaps < 7)) {
                        battarey = new Battarey(0, 0, mainStage);
                        battarey.centerAtActor(rockActor);
                    }
                }
            }
        }

        if (!gameOver && BaseActor.count(mainStage, "Rock") == 0) {
            BaseActor messageWin = new BaseActor(0, 0, uiStage);
            messageWin.loadTexture("assets/message-win.png");
            messageWin.centerAtPosition(400, 300);
            messageWin.setOpacity(0);
            messageWin.addAction(Actions.fadeIn(1));
            gameOver = true;
        }

    }

}
