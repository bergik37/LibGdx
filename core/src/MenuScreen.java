import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class MenuScreen extends BaseScreen {
    @Override
    public void initialize() {
        BaseActor ship = new BaseActor(0,0, mainStage);
        ship.loadTexture( "assets/SpaceBackground.jpg" );
        ship.centerAtPosition(400,300);
        ship.moveBy(0,100);
        BaseActor start = new BaseActor(0,0, mainStage);
        start.loadTexture( "assets/message-start.png" );
        start.centerAtPosition(400,300);
        start.moveBy(0,-100);
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            SpaceGame.setActiveScreen( new LevelScreen() );
    }
}
