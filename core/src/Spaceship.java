import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;


public class Spaceship extends BaseActor {

    private Thrusters thrusters;
    private Shield shield;
    public int shieldPower;

    public Spaceship(float x, float y, Stage s)
    {
        super(x,y,s);
        loadTexture( "assets/spaceship.png" );
        setBoundaryPolygon(8);
        setAcceleration(200);
        setMaxSpeed(100);
        //трение
        setDeceleration(10);
        thrusters = new Thrusters(0,0, s);
        addActor(thrusters);
        thrusters.setPosition(-thrusters.getWidth(), getHeight()/2 - thrusters.getHeight()/2 );
        shield = new Shield(0,0, s);
        addActor(shield);
        shield.centerAtPosition( getWidth()/2, getHeight()/2 );
        shieldPower = 100;
        shield.setOpacity(shieldPower / 100f);
        if (shieldPower <= 0)
            shield.setVisible(false);
    }
/*
Затем добавьте следующий метод, который использует случайный метод класса MathUtils для генерации
случайные числа с плавающей точкой (до заданного параметра), которые используются для задания положения космического корабля:
 */
/*
Цель условного оператора в начале метода warp состоит в том, чтобы проверить, что
космический корабль все еще является частью игры (обозначается тем, что он прикреплен к сцене). Если метод getstate возвращает
null, то космический корабль был удален из игры, и метод возвращается сразу же, эффективно
остановка кода, который следует из выполнения.
 */
    public void warp()
    {
        if ( getStage() == null)
            return;
        Warp warp1 = new Warp(0,0, this.getStage());
        warp1.centerAtActor(this);
        setPosition(MathUtils.random(800), MathUtils.random(600));
        Warp warp2 = new Warp(0,0, this.getStage());
        warp2.centerAtActor(this);
    }

    public void shoot()
    {
        if ( getStage() == null )
            return;
        Laser laser = new Laser(0,0, this.getStage());
        laser.centerAtActor(this);
        laser.setRotation( this.getRotation() );
        laser.setMotionAngle( this.getRotation() );
    }


    public void act(float dt)
    {
        super.act( dt );
        float degreesPerSecond = 120; // rotation speed
        if (Gdx.input.isKeyPressed(Keys.LEFT))
            rotateBy(degreesPerSecond * dt);
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            rotateBy(-degreesPerSecond * dt);
            if (Gdx.input.isKeyPressed(Keys.UP))
            {
                accelerateAtAngle( getRotation() );
                thrusters.setVisible(true);
            }
            else
            {
                thrusters.setVisible(false);
            }
        accelerateAtAngle( getRotation() );
        applyPhysics(dt);
        wrapAroundWorld();
    }
}
