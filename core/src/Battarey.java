import com.badlogic.gdx.scenes.scene2d.Stage;

public class Battarey extends BaseActor {
    public Battarey(float x, float y, Stage s) {
        super(x, y, s);
        loadTexture("assets/bat.png");
        setSize(30, 50);
    }

    public void act(float dt) {
        super.act(dt);
        applyPhysics(dt);
        wrapAroundWorld();
        // удалить после перемещения мимо левого края экрана
        //if (( getX() + getWidth() < 0 )|( getX() < getWidth())|(getY() + getHeight() < 0 )|(getY()  < getHeight()))
        if ((getX() + getWidth() < 0))
            remove();
    }
}
