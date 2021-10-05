import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public abstract class BaseGame extends Game {
    public static Label.LabelStyle labelStyle;
    public static TextButton.TextButtonStyle textButtonStyle;
    private static BaseGame game;


    public BaseGame() {
        game = this;
    }

    public static void setActiveScreen(BaseScreen s) {
        game.setScreen(s);
    }

    public void create() {
        // подготовка к нескольким классам / этапам приема дискретных входных данных
        InputMultiplexer im = new InputMultiplexer();
        Gdx.input.setInputProcessor(im);

        labelStyle = new Label.LabelStyle();


        textButtonStyle = new TextButton.TextButtonStyle();

        Texture buttonTex = new Texture(Gdx.files.internal("assets/button.png"));
        NinePatch buttonPatch = new NinePatch(buttonTex, 24, 24, 24, 24);
        textButtonStyle.up = new NinePatchDrawable(buttonPatch);
        textButtonStyle.fontColor = Color.GRAY;
    }
}
