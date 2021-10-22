package scripts;

import scripts.api.gui.GUIFX;
import scripts.api.nodes.Node;
import scripts.api.works.Work;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FletchingXVariables {

    private final static FletchingXVariables instance = new FletchingXVariables();

    private GUIFX gui;
    private URL fxml;

    private FletchingXSettings settings = new FletchingXSettings();
    private List<Node> nodes = new ArrayList<>();
    private List<Integer> waitTimes = new ArrayList<>();

    private String state = "State";

    private Work currentWork;

    private boolean start;

    private final Image img = Objects.requireNonNull(getImage("https://jacksonjohnson.ca/fletchingx/FletchingX.png"))
            .getScaledInstance(520, 167, Image.SCALE_SMOOTH);

    private final RenderingHints aa = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    private final Color main_background = new Color(0,0,0, 0.8F);
    private final Font font = new Font("Verdana", Font.PLAIN, 11);
    private final Font main_font_secondary = new Font("Verdana", Font.PLAIN, 10);
    private final Font font_status = new Font("Verdana", Font.PLAIN, 12);
    private final Font font_level = new Font("Verdana", Font.PLAIN, 10);
    private final Font font_progress_bar = new Font("Verdana", Font.PLAIN, 24);
    private final Color progress_colour_background = new Color(0, 0, 0, 0.8F);
    private final Color paint_main_colour = new Color(200, 200, 200); // dark green // 0, 100, 0

    public FletchingXVariables() {}

    /**
     * Return an image from the internet.
     *
     * @param url The address belonging to the image.
     * @return The image; otherwise null.
     */
    private static Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            return null;
        }
    }

    public static FletchingXVariables get() {
        return instance;
    }

    public GUIFX getGui() {
        return gui;
    }

    public void setGui(GUIFX gui) {
        this.gui = gui;
    }

    public URL getFxml() {
        return fxml;
    }

    public void setFxml(URL fxml) {
        this.fxml = fxml;
    }

    public FletchingXSettings getSettings() {
        return settings;
    }

    public void setSettings(FletchingXSettings settings) {
        this.settings = settings;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Integer> getWaitTimes() {
        return waitTimes;
    }

    public void setWaitTimes(List<Integer> waitTimes) {
        this.waitTimes = waitTimes;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Work getCurrentWork() {
        return currentWork;
    }

    public void setCurrentWork(Work currentWork) {
        this.currentWork = currentWork;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public Image getImg() {
        return img;
    }

    public RenderingHints getAa() {
        return aa;
    }

    public Color getMain_background() {
        return main_background;
    }

    public Font getFont() {
        return font;
    }

    public Font getMain_font_secondary() {
        return main_font_secondary;
    }

    public Font getFont_status() {
        return font_status;
    }

    public Font getFont_level() {
        return font_level;
    }

    public Color getProgress_colour_background() {
        return progress_colour_background;
    }

    public Font getFont_progress_bar() {
        return font_progress_bar;
    }

    public Color getPaint_main_colour() {
        return paint_main_colour;
    }

    @Override
    public String toString() {
        return "PolymorphicFletcherVariables{" +
                "settings=" + settings +
                ", nodes=" + nodes +
                ", waitTimes=" + waitTimes +
                ", currentWork=" + currentWork +
                ", start=" + start +
                ", img=" + img +
                ", aa=" + aa +
                ", main_background=" + main_background +
                ", font=" + font +
                ", main_font_secondary=" + main_font_secondary +
                ", font_status=" + font_status +
                ", font_level=" + font_level +
                ", font_progress_bar=" + font_progress_bar +
                ", progress_colour_background=" + progress_colour_background +
                ", paint_main_colour=" + paint_main_colour +
                '}';
    }
}
