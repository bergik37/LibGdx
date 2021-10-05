import com.badlogic.gdx.scenes.scene2d.Stage;

public class Coins extends BaseActor {
    public Coins(float x, float y, Stage s) {
        super(x, y, s);
        loadTexture("assets/star.png");
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
