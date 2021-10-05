import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class BaseActor extends Group {
    private static Rectangle worldBounds;
    private Animation<TextureRegion> animation;
    private float elapsedTime;
    private boolean animationPaused;
    private Vector2 velocityVec;
    private Vector2 accelerationVec;
    private float acceleration;
    private float maxSpeed;
    private float deceleration;
    private Polygon boundaryPolygon;


    public BaseActor(float x, float y, Stage s) {
        // вызов конструктора из класса Actor

        super();

        // выполнение дополнительных задач инициализации
        setPosition(x, y);
        s.addActor(this);

        // инициализация данных анимации
        animation = null;
        elapsedTime = 0;
        animationPaused = false;

        // initialize physics data
        velocityVec = new Vector2(0, 0);
        accelerationVec = new Vector2(0, 0);
        acceleration = 0;
        maxSpeed = 1000;
        deceleration = 0;

        boundaryPolygon = null;
    }
    // ----------------------------------------------
    // Animation methods
    // ---

    /**
     * Set world dimensions for use by methods boundToWorld() and scrollTo().
     *
     * @param width  width of world
     * @param height height of world
     */
    public static void setWorldBounds(float width, float height) {
        worldBounds = new Rectangle(0, 0, width, height);
    }

    public static void setWorldBounds(BaseActor ba) {
        setWorldBounds(ba.getWidth(), ba.getHeight());
    }

    // ----------------------------------------------
    // Instance list methods
    // -------
    public static ArrayList<BaseActor> getList(Stage stage, String className) {
        ArrayList<BaseActor> list = new ArrayList<BaseActor>();

        Class theClass = null;
        try {
            theClass = Class.forName(className);
        } catch (Exception error) {
            error.printStackTrace();
        }

        for (Actor a : stage.getActors()) {
            if (theClass.isInstance(a))
                list.add((BaseActor) a);
        }

        return list;
    }

    public static int count(Stage stage, String className) {
        return getList(stage, className).size();
    }

    /**
     * После установки анимации можно будет задать размер (ширину и высоту) актера,
     * а также начало координат (точка, вокруг которой должен вращаться актер, как правило, центр актера).
     * Ширина и высота актера будут установлены на ширину и высоту первого изображения анимации (the
     * изображения анимации также называются ключевыми кадрами). Это достигается с помощью следующего метода:
     */
    public void setAnimation(Animation<TextureRegion> anim) {
        animation = anim;
        TextureRegion tr = animation.getKeyFrame(0);
        float w = tr.getRegionWidth();
        float h = tr.getRegionHeight();
        setSize(w, h);
        setOrigin(w / 2, h / 2);
//colision
        if (boundaryPolygon == null)
            setBoundaryRectangle();
    }

    public void setAnimationPaused(boolean pause) {
        animationPaused = pause;
    }


    // ----------------------------------------------
    // physics/motion methods
    // ----------------------------------------------

    /*
    Этот метод также возвращает анимацию, которая была создана, в том случае, если несколько анимаций требуются для
актер (в этом случае необходимо создать расширение базового класса актера и дополнительные анимации
должно храниться в дополнительных переменных).
     */
    public Animation<TextureRegion> loadAnimationFromFiles(String[] fileNames,
                                                           float frameDuration, boolean loop) {
        int fileCount = fileNames.length;
        Array<TextureRegion> textureArray = new Array<TextureRegion>();
        for (int n = 0; n < fileCount; n++) {
            String fileName = fileNames[n];
            Texture texture = new Texture(Gdx.files.internal(fileName));
            texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            textureArray.add(new TextureRegion(texture));
        }
        /*
        Для простоты,
вы предположите, что анимационные кадры либо воспроизводятся с первого по последний раз (обозначается константой
значение PlayMode.NORMAL) или повторите навсегда в цикле (обозначенном PlayMode.LOOP); это может быть указано с помощью
логическая переменная, указывающая, является ли анимация циклической или нет.
         */
        Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);
        if (loop)
            anim.setPlayMode(Animation.PlayMode.LOOP);
        else
            anim.setPlayMode(Animation.PlayMode.NORMAL);
        if (animation == null)
            setAnimation(anim);
        return anim;
    }

    /*
    Далее следует аналогичный метод для создания анимации из таблицы спрайтов. Удобно, TextureRegion
    класс имеет метод с именем split, который может быть использован для разбиения изображения на коллекцию подизображений.
    Использование этого метода требует знания размера каждого подизображения, который вычисляется в следующем коде
    на основе размера исходного изображения и количества строк и столбцов, присутствующих в таблице spritesheet.
     */
    public Animation<TextureRegion> loadAnimationFromSheet(String fileName, int rows, int cols,
                                                           float frameDuration, boolean loop) {
        Texture texture = new Texture(Gdx.files.internal(fileName), true);
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;
        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
        Array<TextureRegion> textureArray = new Array<TextureRegion>();
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                textureArray.add(temp[r][c]);
        Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration,
                textureArray);
        if (loop)
            anim.setPlayMode(Animation.PlayMode.LOOP);
        else
            anim.setPlayMode(Animation.PlayMode.NORMAL);
        if (animation == null)
            setAnimation(anim);
        return anim;
    }

    /*
    Некоторые игровые объекты могут быть представлены одним изображением и не требуют анимации. Для
    последовательность, однако, вы можете отображать неподвижное изображение с помощью однокадровой анимации; длительность кадра и
    стиль воспроизведения в данном случае не имеет значения. Для удобства для этого будет использован следующий метод
    ситуация.
     */
    public Animation<TextureRegion> loadTexture(String fileName) {
        String[] fileNames = new String[1];
        fileNames[0] = fileName;
        return loadAnimationFromFiles(fileNames, 1, true);
    }

    /*
    Еще один метод, который вы должны включить в это время, будет использоваться для проверки того, закончена ли анимация,
    что верно, если анимация не является циклической и затраченное время больше времени, необходимого для отображения
    все изображения в анимации (количество кадров, умноженное на время отображения для каждого кадра). Это
    вычисляется с использованием метода класса анимации isAnimationFinished; ваш метод просто вызывает этот метод
    на переменную анимация автоматически выводится и значение прошедшего времени.
     */
    public boolean isAnimationFinished() {
        return animation.isAnimationFinished(elapsedTime);
    }

    /**
     * Set acceleration of this object.
     *
     * @param acc Acceleration in (pixels/second) per second.
     */
    public void setAcceleration(float acc) {
        acceleration = acc;
    }

    /**
     * Set deceleration of this object.
     * Deceleration is only applied when object is not accelerating.
     *
     * @param dec Deceleration in (pixels/second) per second.
     */
    public void setDeceleration(float dec) {
        deceleration = dec;
    }

    /**
     * Set maximum speed of this object.
     *
     * @param ms Maximum speed of this object in (pixels/second).
     */
    public void setMaxSpeed(float ms) {
        maxSpeed = ms;
    }

    /**
     * Calculates the speed of movement (in pixels/second).
     *
     * @return speed of movement (pixels/second)
     */
    public float getSpeed() {
        return velocityVec.len();
    }

    /**
     * Set the speed of movement (in pixels/second) in current direction.
     * If current speed is zero (direction is undefined), direction will be set to 0 degrees.
     *
     * @param speed of movement (pixels/second)
     */
    public void setSpeed(float speed) {
        // если длина равна нулю, то предположим, что угол движения равен нулю градусов
        if (velocityVec.len() == 0)
            velocityVec.set(speed, 0);
        else
            velocityVec.setLength(speed);
    }

    /**
     * Determines if this object is moving (if speed is greater than zero).
     *
     * @return false when speed is zero, true otherwise
     */
    public boolean isMoving() {
        return (getSpeed() > 0);
    }

    /**
     * Get the angle of motion (in degrees), calculated from the velocity vector.
     * <br>
     * To align actor image angle with motion angle, use <code>setRotation( getMotionAngle() )</code>.
     *
     * @return angle of motion (degrees)
     */
    public float getMotionAngle() {
        return velocityVec.angle();
    }

    /**
     * Sets the angle of motion (in degrees).
     * If current speed is zero, this will have no effect.
     *
     * @param angle of motion (degrees)
     */
    public void setMotionAngle(float angle) {
        velocityVec.setAngle(angle);
    }

    /**
     * Update accelerate vector by angle and value stored in acceleration field.
     * Acceleration is applied by <code>applyPhysics</code> method.
     *
     * @param angle Angle (degrees) in which to accelerate.
     * @see #acceleration
     * @see #applyPhysics
     */
    public void accelerateAtAngle(float angle) {
        accelerationVec.add(
                new Vector2(acceleration, 0).setAngle(angle));
    }

    /**
     * Update accelerate vector by current rotation angle and value stored in acceleration field.
     * Acceleration is applied by <code>applyPhysics</code> method.
     *
     * @see #acceleration
     * @see #applyPhysics
     */
    public void accelerateForward() {
        accelerateAtAngle(getRotation());
    }

    /**
     * Adjust velocity vector based on acceleration vector,
     * then adjust position based on velocity vector. <br>
     * If not accelerating, deceleration value is applied. <br>
     * Speed is limited by maxSpeed value. <br>
     * Acceleration vector reset to (0,0) at end of method. <br>
     *
     * @param dt Time elapsed since previous frame (delta time); typically obtained from <code>act</code> method.
     * @see #acceleration
     * @see #deceleration
     * @see #maxSpeed
     */
    public void applyPhysics(float dt) {
        // apply acceleration
        velocityVec.add(accelerationVec.x * dt, accelerationVec.y * dt);

        float speed = getSpeed();

        // decrease speed (decelerate) when not accelerating
        if (accelerationVec.len() == 0)
            speed -= deceleration * dt;

        // keep speed within set bounds
        speed = MathUtils.clamp(speed, 0, maxSpeed);

        // update velocity
        setSpeed(speed);

        // apply velocity
        moveBy(velocityVec.x * dt, velocityVec.y * dt);

        // reset acceleration
        accelerationVec.set(0, 0);
    }

    // ----------------------------------------------
    // Collision polygon methods
    // -------------------
    public void setBoundaryRectangle() {
        float w = getWidth();
        float h = getHeight();
        float[] vertices = {0, 0, w, 0, w, h, 0, h};
        boundaryPolygon = new Polygon(vertices);
    }

    public Polygon getBoundaryPolygon() {
        boundaryPolygon.setPosition(getX(), getY());
        boundaryPolygon.setOrigin(getOriginX(), getOriginY());
        boundaryPolygon.setRotation(getRotation());
        boundaryPolygon.setScale(getScaleX(), getScaleY());
        return boundaryPolygon;
    }

    public void setBoundaryPolygon(int numSides) {
        float w = getWidth();
        float h = getHeight();
        float[] vertices = new float[2 * numSides];
        for (int i = 0; i < numSides; i++) {
            float angle = i * 6.28f / numSides;
            // x-coordinate
            vertices[2 * i] = w / 2 * MathUtils.cos(angle) + w / 2;
            // y-coordinate
            vertices[2 * i + 1] = h / 2 * MathUtils.sin(angle) + h / 2;
        }
        boundaryPolygon = new Polygon(vertices);
    }

    public boolean overlaps(BaseActor other) {
        Polygon poly1 = this.getBoundaryPolygon();
        Polygon poly2 = other.getBoundaryPolygon();
        // initial test to improve performance
        if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()))
            return false;
        return Intersector.overlapConvexPolygons(poly1, poly2);
    }

    public void centerAtPosition(float x, float y) {
        setPosition(x - getWidth() / 2, y - getHeight() / 2);
    }

    public void centerAtActor(BaseActor other) {
        centerAtPosition(other.getX() + other.getWidth() / 2, other.getY() + other.getHeight() / 2);
    }

    public void setOpacity(float opacity) {
        this.getColor().a = opacity;
    }

    public Vector2 preventOverlap(BaseActor other) {
        Polygon poly1 = this.getBoundaryPolygon();
        Polygon poly2 = other.getBoundaryPolygon();
        // initial test to improve performance
        if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()))
            return null;
        MinimumTranslationVector mtv = new MinimumTranslationVector();
        boolean polygonOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);
        if (!polygonOverlap)
            return null;
        this.moveBy(mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth);
        return mtv.normal;
    }

    /*
    Чтобы удержать актера в пределах прямоугольной области, определенной worldbounds, вам нужно будет выполнить четыре действия
    сравнение, чтобы проверить, прошли ли какие-либо ребра (левое, правое, верхнее и нижнее) актера за пределы
    соответствующие края экрана, и если это так, то соответствующая координата (x или y) устанавливается для сохранения актера на экране.
    экран. Это достигается с помощью следующего метода:
     */
    public void boundToWorld() {
        // check left edge
        if (getX() < 0)
            setX(0);
        // check right edge
        if (getX() + getWidth() > worldBounds.width)
            setX(worldBounds.width - getWidth());
        // check bottom edge
        if (getY() < 0)
            setY(0);
        // check top edge
        if (getY() + getHeight() > worldBounds.height)
            setY(worldBounds.height - getHeight());
    }

    public void alignCamera() {
        Camera cam = this.getStage().getCamera();
        Viewport v = this.getStage().getViewport();
        // center camera on actor
        cam.position.set(this.getX() + this.getOriginX(), this.getY() + this.getOriginY(), 0);
        // bound camera to layout
        cam.position.x = MathUtils.clamp(cam.position.x,
                cam.viewportWidth / 2, worldBounds.width - cam.viewportWidth / 2);
        cam.position.y = MathUtils.clamp(cam.position.y,
                cam.viewportHeight / 2, worldBounds.height - cam.viewportHeight / 2);
        cam.update();
    }

    public void wrapAroundWorld() {
        if (getX() + getWidth() < 0)
            setX(worldBounds.width);
        if (getX() > worldBounds.width)
            setX(-getWidth());
        if (getY() + getHeight() < 0)
            setY(worldBounds.height);
        if (getY() > worldBounds.height)
            setY(-getHeight());
    }

    // ----------------------------------------------
    // Actor methods: act and draw
    // ------
    public void act(float dt) {
        super.act(dt);
        if (!animationPaused)
            elapsedTime += dt;
    }

    public void draw(Batch batch, float parentAlpha) {
        // применить эффект цветового оттенка
        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a);
        if (animation != null && isVisible())
            batch.draw(animation.getKeyFrame(elapsedTime),
                    getX(), getY(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        super.draw(batch, parentAlpha);
    }
}