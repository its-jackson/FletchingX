package scripts.api.gui;

import com.allatori.annotations.DoNotRename;
import dax.api_lib.models.RunescapeBank;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.tribot.script.sdk.util.ScriptSettings;
import scripts.FletchingXSettings;
import scripts.FletchingXVariables;
import scripts.api.antiban.Seed;
import scripts.api.enums.Resource;
import scripts.api.enums.ResourceOption;
import scripts.api.enums.WorkType;
import scripts.api.time.TimeElapse;
import scripts.api.utilities.PolymorphicScriptSettings;
import scripts.api.works.Alchemy;
import scripts.api.works.Cutting;
import scripts.api.works.Stringing;
import scripts.api.works.Work;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Arrays;
import java.util.ResourceBundle;

import static scripts.api.enums.WorkType.*;

@DoNotRename
public class Controller implements Initializable {

    @FXML
    @DoNotRename
    private GUIFX gui;

    @FXML
    @DoNotRename
    private Button buttonCreateWork;

    @FXML
    @DoNotRename
    private Button buttonDeleteWork;

    @FXML
    @DoNotRename
    private Button buttonGenerateRandomLevel;

    @FXML
    @DoNotRename
    private Button buttonGenerateRandomSeed;

    @FXML
    @DoNotRename
    private Button buttonGenerateRandomTime;

    @FXML
    @DoNotRename
    private Button buttonLoadWork;

    @FXML
    @DoNotRename
    private Button buttonSaveWork;

    @FXML
    @DoNotRename
    private Button buttonStart;

    @FXML
    @DoNotRename
    private Button buttonUpdateWork;

    @FXML
    @DoNotRename
    private CheckBox checkBoxFatigueSystem;

    @FXML
    @DoNotRename
    private CheckBox checkBoxMicroSleep;

    @FXML
    @DoNotRename
    private CheckBox checkBoxRandomWorldHop;

    @FXML
    @DoNotRename
    private CheckBox checkBoxSpecificWorld;

    @FXML
    @DoNotRename
    private CheckBox checkBoxWorldHopPlayerCount;

    @FXML
    @DoNotRename
    private ChoiceBox<Resource> choiceBoxResource;

    @FXML
    @DoNotRename
    private ChoiceBox<RunescapeBank> choiceBoxResourceLocation;

    @FXML
    @DoNotRename
    private ChoiceBox<ResourceOption> choiceBoxResourceOption;

    @FXML
    @DoNotRename
    private ChoiceBox<WorkType> choiceBoxWorkType;

    @FXML
    @DoNotRename
    private TableColumn<Work, Integer> colLevel;

    @FXML
    @DoNotRename
    private TableColumn<Work, Resource> colResource;

    @FXML
    @DoNotRename
    private TableColumn<Work, RunescapeBank> colResourceLocation;

    @FXML
    @DoNotRename
    private TableColumn<Work, ResourceOption> colResourceOption;

    @FXML
    @DoNotRename
    private TableColumn<Work, Long> colSupplies;

    @FXML
    @DoNotRename
    private TableColumn<Work, TimeElapse> colTime;

    @FXML
    @DoNotRename
    private Hyperlink hyperLinkDiscordChannel;

    @FXML
    @DoNotRename
    private Hyperlink hyperLinkForum;

    @FXML
    @DoNotRename
    private Label labelFatigueHeader;

    @FXML
    @DoNotRename
    private Label labelFatigueSubHeader;

    @FXML
    @DoNotRename
    private Label labelFatigueSubHeader2;

    @FXML
    @DoNotRename
    private Label labelFatigueSubHeader3;

    @FXML
    @DoNotRename
    private Label labelGrandExchange;

    @FXML
    @DoNotRename
    private Label labelHeader;

    @FXML
    @DoNotRename
    private Label labelInstruction;

    @FXML
    @DoNotRename
    private Label labelIronMan;

    @FXML
    @DoNotRename
    private Label labelMicroSleepHeader;

    @FXML
    @DoNotRename
    private Label labelMicroSleepSubHeader;

    @FXML
    @DoNotRename
    private Label labelPatchNote;

    @FXML
    @DoNotRename
    private Label labelResource;

    @FXML
    @DoNotRename
    private Label labelResourceLocation;

    @FXML
    @DoNotRename
    private Label labelResourceOption;

    @FXML
    @DoNotRename
    private Label labelSeedHeader;

    @FXML
    @DoNotRename
    private Label labelSeedSubHeader;

    @FXML
    @DoNotRename
    private Label labelSeedSubHeader2;

    @FXML
    @DoNotRename
    private Label labelSeedSubHeader3;

    @FXML
    @DoNotRename
    private Label labelSeedSubSystem;

    @FXML
    @DoNotRename
    private Label labelSlogan;

    @FXML
    @DoNotRename
    private Label labelStoppingCondition;

    @FXML
    @DoNotRename
    private Label labelWorkType;

    @FXML
    @DoNotRename
    private Label labelWorldHeader;

    @FXML
    @DoNotRename
    private Label labelWorldSubHeader;

    @FXML
    @DoNotRename
    private Label labelWorldSubHeader2;

    @FXML
    @DoNotRename
    private Label labelWorldSubHeader3;

    @FXML
    @DoNotRename
    private RadioButton radioButtonAmountToMake;

    @FXML
    @DoNotRename
    private RadioButton radioButtonDoNotRepeat;

    @FXML
    @DoNotRename
    private RadioButton radioButtonLevel;

    @FXML
    @DoNotRename
    private RadioButton radioButtonRepeat;

    @FXML
    @DoNotRename
    private RadioButton radioButtonShuffleRepeat;

    @FXML
    @DoNotRename
    private RadioButton radioButtonTime;

    @FXML
    @DoNotRename
    private ToggleGroup repeatGroup;

    @FXML
    @DoNotRename
    private Separator separatorChoiceBox;

    @FXML
    @DoNotRename
    private Separator separatorStoppingCondition;

    @FXML
    @DoNotRename
    private Spinner<Integer> spinnerWorldHopPlayerCount;

    @FXML
    @DoNotRename
    private ToggleGroup stoppingGroup;

    @FXML
    @DoNotRename
    private TableView<Work> tableViewCore;

    @FXML
    @DoNotRename
    private TextArea textAreaInstruction;

    @FXML
    @DoNotRename
    private TextArea textAreaPatchNote;

    @FXML
    @DoNotRename
    private TextField textFieldAmountToMake;

    @FXML
    @DoNotRename
    private TextField textFieldAntiBanSeed;

    @FXML
    @DoNotRename
    private TextField textFieldSpecificWorld;

    @FXML
    @DoNotRename
    private TextField textFieldStoppingCondition;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // set up the controls
        setUpControls();
    }

    @FXML
    @DoNotRename
    private void setUpControls() {
        // choice box work
        initializeChoiceBoxWork();

        // set the tableview columns
        getColResource().setCellValueFactory(workResourceCellDataFeatures ->
                new ReadOnlyObjectWrapper<>(workResourceCellDataFeatures.getValue().getResource()));

        getColResourceLocation().setCellValueFactory(workResourceLocationCellDataFeatures ->
                new ReadOnlyObjectWrapper<>(workResourceLocationCellDataFeatures.getValue().getBankLocation()));

        getColResourceOption().setCellValueFactory(workResourceOptionCellDataFeatures ->
                new ReadOnlyObjectWrapper<>(workResourceOptionCellDataFeatures.getValue().getResourceOption()));

        getColLevel().setCellValueFactory(workIntegerCellDataFeatures ->
                new ReadOnlyObjectWrapper<>(workIntegerCellDataFeatures.getValue().getLevel()));

        getColTime().setCellValueFactory(workTimeElapseCellDataFeatures ->
                new ReadOnlyObjectWrapper<>(workTimeElapseCellDataFeatures.getValue().getTime()));

        getColSupplies().setCellValueFactory(workLongCellDataFeatures ->
                new ReadOnlyObjectWrapper<>(workLongCellDataFeatures.getValue().getSuppliesToMake()));

        // World hop spinner
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 300, 1);

        getSpinnerWorldHopPlayerCount().setValueFactory(valueFactory);


        // on action checkbox world hop player count
        onActionCheckBoxWorldHopPlayerCount();

        // on action specific world
        onActionCheckBoxSpecificWorld();

        // on action choice box resource
        onActionChoiceBoxWorkType();

        // on action resource
        onActionChoiceBoxResource();

        // on action
        onActionButtonGenerateRandomLevel();
        // on action
        onActionButtonGenerateRandomTime();
        // on action
        onActionButtonGenerateRandomSeed();
        // on action
        onActionRadioButtonTime();
        // on action
        onActionRadioButtonLevel();
        // on action
        onActionRadioButtonAmountToMake();
        // on action
        onActionRadioButtonCreateWork();
        // on action
        onActionButtonUpdateWork();
        // on action
        onActionButtonDeleteWork();
        // on action
        onActionHyperLinkDiscordChannel();
        // on action
        onActionForumsLink();
        // on action
        onActionButtonSaveSettings();
        // on action
        onActionButtonLoadSettings();
        // on action
        onActionButtonStart();

        // init tooltips
        getRadioButtonShuffleRepeat().setTooltip(new Tooltip("Will shuffle and repeat the script once all work is completed"));
        getRadioButtonRepeat().setTooltip(new Tooltip("Will repeat the script once all work is completed"));
        getRadioButtonDoNotRepeat().setTooltip(new Tooltip("Will end the script once all work is completed"));
        getRadioButtonTime().setTooltip(new Tooltip("DAYS:HOURS:MINUTES:SECONDS - 00:00:00:00"));
        getRadioButtonLevel().setTooltip(new Tooltip("Less than 0 or greater than 100 is invalid"));
        getButtonGenerateRandomLevel().setTooltip(new Tooltip("Generates a random level greater than zero and less than 100"));
        getButtonGenerateRandomTime().setTooltip(new Tooltip("Generate a random time - DAYS:HOUR:MINUTES:SECONDS"));
        getSpinnerWorldHopPlayerCount().setTooltip(new Tooltip("Please enter only integers."));
    }

    @FXML
    @DoNotRename
    void onActionRadioButtonCreateWork() {
        getButtonCreateWork().setOnAction(actionEvent -> {
            if (validateWorkState()) {
                WorkType type = getChoiceBoxWorkType().getSelectionModel().getSelectedItem();
                Resource resource = getChoiceBoxResource().getSelectionModel().getSelectedItem();
                RunescapeBank resourceLocation = getChoiceBoxResourceLocation().getSelectionModel().getSelectedItem();
                ResourceOption resourceOption = getChoiceBoxResourceOption().getSelectionModel().getSelectedItem();
                String stoppingCondition = getTextFieldStoppingCondition().getText();
                String amount = getTextFieldAmountToMake().getText();
                Work work = null;

                if (getRadioButtonLevel().isSelected()) {
                    if (getRadioButtonAmountToMake().isSelected()) {
                        if (type.equals(CUTTING)) {
                            work = new Cutting(resource, resourceOption, resourceLocation, Integer.parseInt(stoppingCondition), Long.parseLong(amount));
                        } else if (type.equals(STRINGING)) {
                            work = new Stringing(resource, resourceOption, resourceLocation, Integer.parseInt(stoppingCondition), Long.parseLong(amount));
                        } else if (type.equals(ALCHEMY)) {
                            work = new Alchemy(resource, resourceOption, resourceLocation, Integer.parseInt(stoppingCondition), Long.parseLong(amount));
                        }
                    } else {
                        if (type.equals(CUTTING)) {
                            work = new Cutting(resource, resourceOption, resourceLocation, Integer.parseInt(stoppingCondition));
                        } else if (type.equals(STRINGING)) {
                            work = new Stringing(resource, resourceOption, resourceLocation, Integer.parseInt(stoppingCondition));
                        } else if (type.equals(ALCHEMY)) {
                            work = new Alchemy(resource, resourceOption, resourceLocation, Integer.parseInt(stoppingCondition));
                        }
                    }
                } else if (getRadioButtonTime().isSelected()) {
                    TimeElapse time = new TimeElapse(stoppingCondition);
                    if (getRadioButtonAmountToMake().isSelected()) {
                        if (type.equals(CUTTING)) {
                            work = new Cutting(resource, resourceOption, resourceLocation, time, Long.parseLong(amount));
                        } else if (type.equals(STRINGING)) {
                            work = new Stringing(resource, resourceOption, resourceLocation, time, Long.parseLong(amount));
                        } else if (type.equals(ALCHEMY)) {
                            work = new Alchemy(resource, resourceOption, resourceLocation, time, Long.parseLong(amount));
                        }
                    } else {
                        if (type.equals(CUTTING)) {
                            work = new Cutting(resource, resourceOption, resourceLocation, time);
                        } else if (type.equals(STRINGING)) {
                            work = new Stringing(resource, resourceOption, resourceLocation, time);
                        } else if (type.equals(ALCHEMY)) {
                            work = new Alchemy(resource, resourceOption, resourceLocation, time);
                        }
                    }
                }

                if (work != null) {
                    getTableViewCore().getItems().add(work);
                    System.out.println("[GUI] Created: " + work);
                }
            }
        });
    }

    @FXML
    @DoNotRename
    void onActionButtonUpdateWork() {
        getButtonUpdateWork().setOnAction(actionEvent -> {
            Work work = getTableViewCore().getSelectionModel().getSelectedItem();
            int indexOf = getTableViewCore().getItems().indexOf(work);
            if (validateWorkState() && work != null) {
                WorkType type = getChoiceBoxWorkType().getSelectionModel().getSelectedItem();
                Resource resource = getChoiceBoxResource().getSelectionModel().getSelectedItem();
                RunescapeBank resourceLocation = getChoiceBoxResourceLocation().getSelectionModel().getSelectedItem();
                ResourceOption resourceOption = getChoiceBoxResourceOption().getSelectionModel().getSelectedItem();
                String stoppingCondition = getTextFieldStoppingCondition().getText();
                String amount = getTextFieldAmountToMake().getText();
                // remove work

                if (type.equals(CUTTING)) {
                    Cutting cutting = new Cutting(resource, resourceOption, resourceLocation);
                    if (getRadioButtonLevel().isSelected()) {
                        cutting.setLevel(Integer.parseInt(stoppingCondition));
                        cutting.setTime(null);
                    } else {
                        cutting.setTime(new TimeElapse(stoppingCondition));
                        cutting.setLevel(0);
                    }
                    if (getRadioButtonAmountToMake().isSelected()) {
                        cutting.setSuppliesToMake(Integer.parseInt(amount));
                    }
                    getTableViewCore().getItems().add(indexOf, cutting);
                    getTableViewCore().getItems().remove(work);
                } else if (type.equals(STRINGING)) {
                    Stringing stringing = new Stringing(resource, resourceOption, resourceLocation);
                    if (getRadioButtonLevel().isSelected()) {
                        stringing.setLevel(Integer.parseInt(stoppingCondition));
                        stringing.setTime(null);
                    } else {
                        stringing.setTime(new TimeElapse(stoppingCondition));
                        stringing.setLevel(0);
                    }
                    if (getRadioButtonAmountToMake().isSelected()) {
                        stringing.setSuppliesToMake(Integer.parseInt(amount));
                    }
                    getTableViewCore().getItems().add(indexOf, stringing);
                    getTableViewCore().getItems().remove(work);
                } else if (type.equals(ALCHEMY)) {
                    Alchemy alchemy = new Alchemy(resource, resourceOption, resourceLocation);
                    if (getRadioButtonLevel().isSelected()) {
                        alchemy.setLevel(Integer.parseInt(stoppingCondition));
                        alchemy.setTime(null);
                    } else {
                        alchemy.setTime(new TimeElapse(stoppingCondition));
                        alchemy.setLevel(0);
                    }
                    if (getRadioButtonAmountToMake().isSelected()) {
                        alchemy.setSuppliesToMake(Integer.parseInt(amount));
                    }
                    getTableViewCore().getItems().add(indexOf, alchemy);
                    getTableViewCore().getItems().remove(work);
                }
                getTableViewCore().refresh();
                System.out.println("[GUI] Updated: " + work);
            }
        });
    }

    @FXML
    @DoNotRename
    void onActionButtonDeleteWork() {
        getButtonDeleteWork().setOnAction(actionEvent -> {
            Work work = getTableViewCore().getSelectionModel().getSelectedItem();
            if (work != null) {
                getTableViewCore().getItems().remove(work);
                getTableViewCore().refresh();
                System.out.println("[GUI] Deleted: " + work);
            }
        });
    }

    @FXML
    @DoNotRename
    void onActionButtonGenerateRandomSeed() {
        getButtonGenerateRandomSeed().setOnAction(actionEvent -> {
            getTextFieldAntiBanSeed().setText(Seed.generateRandomSeed().getSeed());
        });
    }

    @FXML
    @DoNotRename
    void onActionButtonGenerateRandomLevel() {
        getButtonGenerateRandomLevel().setOnAction(actionEvent -> {
            int randomLevel;
            SecureRandom secureRandom = new SecureRandom();

            randomLevel = secureRandom.nextInt(99);

            while (randomLevel == 0) {
                randomLevel = secureRandom.nextInt(99);
                if (randomLevel > 0) {
                    break;
                }
            }

            getTextFieldStoppingCondition().setText(String.valueOf(randomLevel));
        });
    }

    @FXML
    @DoNotRename
    void onActionButtonGenerateRandomTime() {
        getButtonGenerateRandomTime().setOnAction(actionEvent -> {
            getTextFieldStoppingCondition().setText(TimeElapse.generateRandomTimer().getCondition());
        });
    }

    @FXML
    @DoNotRename
    void onActionForumsLink() {
        getHyperLinkForum().setOnAction(actionEvent -> {
            try {
                Desktop.getDesktop().browse(new URI("https://community.tribot.org/topic/84061-iron-man-support-fletching-x-abc2tribot-sdkwork-subsystemfatigue-subsystemhigh-alchingplayer-prefs/"));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    @DoNotRename
    void onActionHyperLinkDiscordChannel() {
        getHyperLinkDiscordChannel().setOnAction(actionEvent -> {
            try {
                Desktop.getDesktop().browse(new URI("https://discord.gg/5kKq3X2X6g"));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    @DoNotRename
    void onActionRadioButtonAmountToMake() {
        getRadioButtonAmountToMake().setOnAction(actionEvent -> {
            if (getRadioButtonAmountToMake().isSelected()) {
                getTextFieldAmountToMake().setDisable(false);
            } else {
                getTextFieldAmountToMake().setDisable(true);
            }
        });
    }

    @FXML
    @DoNotRename
    void onActionRadioButtonLevel() {
        getRadioButtonLevel().setOnAction(actionEvent -> {
            if (getRadioButtonLevel().isSelected()) {
                getButtonGenerateRandomTime().setDisable(true);
                getButtonGenerateRandomLevel().setDisable(false);
                getTextFieldStoppingCondition().setPromptText("99");
                getTextFieldStoppingCondition().setText("99");
            }
        });
    }

    @FXML
    @DoNotRename
    void onActionRadioButtonTime() {
        getRadioButtonTime().setOnAction(actionEvent -> {
            if (getRadioButtonTime().isSelected()) {
                getButtonGenerateRandomLevel().setDisable(true);
                getButtonGenerateRandomTime().setDisable(false);
                getTextFieldStoppingCondition().setPromptText("00:00:00:00");
                getTextFieldStoppingCondition().setText("00:00:00:00");
            }
        });
    }

    @FXML
    @DoNotRename
    private void onActionCheckBoxWorldHopPlayerCount() {
        getCheckBoxWorldHopPlayerCount().setOnAction(actionEvent -> {
            if (getCheckBoxWorldHopPlayerCount().isSelected()) {
                getSpinnerWorldHopPlayerCount().setDisable(false);
            } else {
                getSpinnerWorldHopPlayerCount().setDisable(true);
            }
        });
    }

    @FXML
    @DoNotRename
    private void onActionCheckBoxSpecificWorld() {
        getCheckBoxSpecificWorld().setOnAction(actionEvent -> {
            if (getCheckBoxSpecificWorld().isSelected()) {
                getTextFieldSpecificWorld().setDisable(false);
            } else {
                getTextFieldSpecificWorld().setDisable(true);
            }
        });
    }

    @FXML
    @DoNotRename
    private void onActionChoiceBoxWorkType() {
        getChoiceBoxWorkType().setOnAction(actionEvent -> {
            getChoiceBoxResourceLocation().getItems().clear();
            getChoiceBoxResourceOption().getItems().clear();

            WorkType workType = getChoiceBoxWorkType()
                    .getSelectionModel()
                    .getSelectedItem();

            reviseResource(workType);
        });
    }

    @FXML
    @DoNotRename
    private void onActionChoiceBoxResource() {
        getChoiceBoxResource().setOnAction(actionEvent -> {
            Resource resource = getChoiceBoxResource()
                    .getSelectionModel()
                    .getSelectedItem();

            reviseResourceLocation(resource);
        });
    }

    @FXML
    @DoNotRename
    private void onActionButtonSaveSettings() {
        getButtonSaveWork().setOnAction(actionEvent -> {
            // settings handler
            ScriptSettings settingsHandler = new PolymorphicScriptSettings()
                    .getSettings();

            // the settings
            FletchingXSettings settings = new FletchingXSettings();

            settings.getWork().addAll(getTableViewCore().getItems());
            settings.setRepeat(getRadioButtonRepeat().isSelected());
            settings.setRepeatShuffle(getRadioButtonShuffleRepeat().isSelected());
            settings.setDoNotRepeat(getRadioButtonDoNotRepeat().isSelected());

            settings.setFatigue(getCheckBoxFatigueSystem().isSelected());
            settings.setMicroSleep(getCheckBoxMicroSleep().isSelected());
            settings.setAntiBanSeed(new Seed(getTextFieldAntiBanSeed().getText()));
            settings.setWorldHopPlayerCount(getCheckBoxWorldHopPlayerCount().isSelected());
            settings.setWorldHopRandom(getCheckBoxRandomWorldHop().isSelected());
            settings.setWorldHopFactor(getSpinnerWorldHopPlayerCount().getValue());

            // file chooser - save settings file
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(".json", "*.json");
            fileChooser.getExtensionFilters().add(extensionFilter);
            fileChooser.setInitialDirectory(settingsHandler.getDirectory());
            fileChooser.setTitle("Save settings");

            // show save file
            File file = fileChooser.showSaveDialog(getGui().getStage());

            // save settings
            if (file != null) {
                boolean saveResult = settingsHandler.save(file.getName(), settings);
                if (saveResult) {
                    System.out.println("[GUI] Saved settings file at: " + settingsHandler.getDirectory());
                    System.out.println("[GUI] All Saved settings: " + settingsHandler.getSaveNames());
                }
            }
        });
    }

    @FXML
    @DoNotRename
    private void onActionButtonLoadSettings() {
        getButtonLoadWork().setOnAction(actionEvent -> {
            // settings handler
            ScriptSettings settingsHandler = new PolymorphicScriptSettings()
                    .getSettings();

            // file chooser - save settings file
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(".json", "*.json");
            fileChooser.getExtensionFilters().add(extensionFilter);
            fileChooser.setInitialDirectory(settingsHandler.getDirectory());
            fileChooser.setTitle("Load settings");

            // show save file
            File file = fileChooser.showOpenDialog(getGui().getStage());

            // save settings
            if (file != null) {
                settingsHandler.load(file.getName(), FletchingXSettings.class)
                        .ifPresent(settings -> {
                            System.out.println("[GUI] Loaded settings: " + settings);
                            getTableViewCore().getItems().clear();
                            getTableViewCore().getItems().addAll(settings.getWork());

                            getRadioButtonShuffleRepeat().setSelected(settings.isRepeatShuffle());
                            getRadioButtonRepeat().setSelected(settings.isRepeat());
                            getRadioButtonDoNotRepeat().setSelected(settings.isDoNotRepeat());

                            getCheckBoxFatigueSystem().setSelected(settings.isFatigue());
                            getCheckBoxMicroSleep().setSelected(settings.isMicroSleep());
                            getTextFieldAntiBanSeed().setText(settings.getAntiBanSeed().getSeed());

                            getCheckBoxWorldHopPlayerCount().setSelected(settings.isWorldHopPlayerCount());
                            getCheckBoxRandomWorldHop().setSelected(settings.isWorldHopRandom());
                            getSpinnerWorldHopPlayerCount().getValueFactory().setValue((int) settings.getWorldHopFactor());
                        });
            }
        });
    }

    @FXML
    @DoNotRename
    private void onActionButtonStart() {
        getButtonStart().setOnAction(actionEvent -> {
            // get settings
            FletchingXSettings settings = FletchingXVariables.get()
                    .getSettings();

            // set settings
            settings.getWork().addAll(getTableViewCore().getItems());

            settings.setRepeat(getRadioButtonRepeat().isSelected());
            settings.setRepeatShuffle(getRadioButtonShuffleRepeat().isSelected());
            settings.setDoNotRepeat(getRadioButtonDoNotRepeat().isSelected());

            settings.setFatigue(getCheckBoxFatigueSystem().isSelected());
            settings.setMicroSleep(getCheckBoxMicroSleep().isSelected());
            settings.setAntiBanSeed(new Seed(getTextFieldAntiBanSeed().getText()));

            settings.setWorldHopPlayerCount(getCheckBoxWorldHopPlayerCount().isSelected());
            settings.setWorldHopRandom(getCheckBoxRandomWorldHop().isSelected());
            settings.setWorldHopFactor(getSpinnerWorldHopPlayerCount().getValue());

            if (getTableViewCore().getItems().isEmpty()) {
                System.out.println("[GUI] Please add work before starting.");
            } else {
                System.out.println("[GUI] Script started");
                getGui().close();
                FletchingXVariables.get().setStart(true);
            }
        });
    }

    @FXML
    @DoNotRename
    private void initializeChoiceBoxWork() {
        ObservableList<WorkType> list = getChoiceBoxWorkType().getItems();
        list.addAll(Arrays.asList(WorkType.values()));
    }

    @FXML
    @DoNotRename
    private void initializeResourceLocation() {
        ObservableList<RunescapeBank> list = getChoiceBoxResourceLocation().getItems();
        list.addAll(Arrays.asList(RunescapeBank.values()));
    }

    @FXML
    @DoNotRename
    private void reviseResource(WorkType workType) {
        ObservableList<Resource> resourceList = getChoiceBoxResource().getItems();
        resourceList.clear();
        getChoiceBoxResourceLocation().getItems().clear();

        if (workType != null) {
            switch (workType) {
                case CUTTING:
                    resourceList.add(Resource.SHORTBOW_UNSTRUNG);
                    resourceList.add(Resource.LONGBOW_UNSTRUNG);
                    resourceList.add(Resource.OAK_SHORTBOW_UNSTRUNG);
                    resourceList.add(Resource.OAK_LONGBOW_UNSTRUNG);
                    resourceList.add(Resource.WILLOW_SHORTBOW_UNSTRUNG);
                    resourceList.add(Resource.WILLOW_LONGBOW_UNSTRUNG);
                    resourceList.add(Resource.MAPLE_SHORTBOW_UNSTRUNG);
                    resourceList.add(Resource.MAPLE_LONGBOW_UNSTRUNG);
                    resourceList.add(Resource.YEW_SHORTBOW_UNSTRUNG);
                    resourceList.add(Resource.YEW_LONGBOW_UNSTRUNG);
                    resourceList.add(Resource.MAGIC_SHORTBOW_UNSTRUNG);
                    resourceList.add(Resource.MAGIC_LONGBOW_UNSTRUNG);
                    resourceList.add(Resource.OAK_SHIELD);
                    resourceList.add(Resource.WILLOW_SHIELD);
                    resourceList.add(Resource.MAPLE_SHIELD);
                    resourceList.add(Resource.YEW_SHIELD);
                    resourceList.add(Resource.MAGIC_SHIELD);
                    resourceList.add(Resource.REDWOOD_SHIELD);
                    resourceList.add(Resource.SHAFTS);
                    resourceList.add(Resource.OAK_SHAFTS);
                    resourceList.add(Resource.WILLOW_SHAFTS);
                    resourceList.add(Resource.MAPLE_SHAFTS);
                    resourceList.add(Resource.YEW_SHAFTS);
                    resourceList.add(Resource.MAGIC_SHAFTS);
                    resourceList.add(Resource.REDWOOD_SHAFTS);
                    resourceList.add(Resource.STOCK);
                    resourceList.add(Resource.OAK_STOCK);
                    resourceList.add(Resource.WILLOW_STOCK);
                    resourceList.add(Resource.MAPLE_STOCK);
                    resourceList.add(Resource.TEAK_STOCK);
                    resourceList.add(Resource.MAHOGANY_STOCK);
                    resourceList.add(Resource.YEW_STOCK);
                    resourceList.add(Resource.MAGIC_STOCK);
                    break;
                case STRINGING:
                    resourceList.add(Resource.SHORTBOW);
                    resourceList.add(Resource.LONGBOW);
                    resourceList.add(Resource.OAK_SHORTBOW);
                    resourceList.add(Resource.OAK_LONGBOW);
                    resourceList.add(Resource.WILLOW_SHORTBOW);
                    resourceList.add(Resource.WILLOW_LONGBOW);
                    resourceList.add(Resource.MAPLE_SHORTBOW);
                    resourceList.add(Resource.MAPLE_LONGBOW);
                    resourceList.add(Resource.YEW_SHORTBOW);
                    resourceList.add(Resource.YEW_LONGBOW);
                    resourceList.add(Resource.MAGIC_SHORTBOW);
                    resourceList.add(Resource.MAGIC_LONGBOW);
                    break;
                case ALCHEMY:
                    resourceList.add(Resource.SHORTBOW);
                    resourceList.add(Resource.LONGBOW);
                    resourceList.add(Resource.OAK_SHORTBOW);
                    resourceList.add(Resource.OAK_LONGBOW);
                    resourceList.add(Resource.WILLOW_SHORTBOW);
                    resourceList.add(Resource.WILLOW_LONGBOW);
                    resourceList.add(Resource.MAPLE_SHORTBOW);
                    resourceList.add(Resource.MAPLE_LONGBOW);
                    resourceList.add(Resource.YEW_SHORTBOW);
                    resourceList.add(Resource.YEW_LONGBOW);
                    resourceList.add(Resource.MAGIC_SHORTBOW);
                    resourceList.add(Resource.MAGIC_LONGBOW);
                    resourceList.add(Resource.SHORTBOW_UNSTRUNG);
                    resourceList.add(Resource.LONGBOW_UNSTRUNG);
                    resourceList.add(Resource.OAK_SHORTBOW_UNSTRUNG);
                    resourceList.add(Resource.OAK_LONGBOW_UNSTRUNG);
                    resourceList.add(Resource.WILLOW_SHORTBOW_UNSTRUNG);
                    resourceList.add(Resource.WILLOW_LONGBOW_UNSTRUNG);
                    resourceList.add(Resource.MAPLE_SHORTBOW_UNSTRUNG);
                    resourceList.add(Resource.MAPLE_LONGBOW_UNSTRUNG);
                    resourceList.add(Resource.YEW_SHORTBOW_UNSTRUNG);
                    resourceList.add(Resource.YEW_LONGBOW_UNSTRUNG);
                    resourceList.add(Resource.MAGIC_SHORTBOW_UNSTRUNG);
                    resourceList.add(Resource.MAGIC_LONGBOW_UNSTRUNG);
                    resourceList.add(Resource.OAK_SHIELD);
                    resourceList.add(Resource.WILLOW_SHIELD);
                    resourceList.add(Resource.MAPLE_SHIELD);
                    resourceList.add(Resource.YEW_SHIELD);
                    resourceList.add(Resource.MAGIC_SHIELD);
                    resourceList.add(Resource.REDWOOD_SHIELD);
                    resourceList.add(Resource.STOCK);
                    resourceList.add(Resource.OAK_STOCK);
                    resourceList.add(Resource.WILLOW_STOCK);
                    resourceList.add(Resource.MAPLE_STOCK);
                    resourceList.add(Resource.TEAK_STOCK);
                    resourceList.add(Resource.MAHOGANY_STOCK);
                    resourceList.add(Resource.YEW_STOCK);
                    resourceList.add(Resource.MAGIC_STOCK);
            }
        }
    }

    @FXML
    @DoNotRename
    private void reviseResourceLocation(Resource resource) {
        ObservableList<RunescapeBank> list = getChoiceBoxResourceLocation().getItems();
        list.clear();

        if (resource != null) {
            initializeResourceLocation();
        }
    }

    @FXML
    @DoNotRename
    private boolean validateWorkState() {
        if (getChoiceBoxWorkType().getSelectionModel().isEmpty()) {
            System.out.println("[GUI] Please select a type of work.");
            return false;
        }

        if (getChoiceBoxResource().getSelectionModel().isEmpty()) {
            System.out.println("[GUI] Please select a resource.");
            return false;
        }

        if (getChoiceBoxResourceLocation().getSelectionModel().isEmpty()) {
            System.out.println("[GUI] Please select a resource location.");
            return false;
        }

//        if (getChoiceBoxResourceOption().getSelectionModel().isEmpty()) {
//            if (getChoiceBoxResource().getSelectionModel().getSelectedItem() != Resource.ORE_VEIN) {
//                System.out.println("[GUI] Please select a resource option.");
//                return false;
//            }
//        }

        if (!(getRadioButtonLevel().isSelected() || getRadioButtonTime().isSelected())) {
            System.out.println("[GUI] Please select level or time for the stopping condition.");
            return false;
        }

        String stoppingCondition = getTextFieldStoppingCondition().getText();

        if (stoppingCondition.isEmpty() || stoppingCondition.isBlank()) {
            System.out.println("[GUI] Please enter a stopping condition.");
            return false;
        }

        if (getRadioButtonLevel().isSelected()) {
            try {
                int levelParsed = Integer.parseInt(stoppingCondition, 10);
                if (levelParsed <= 0 || levelParsed > 100) {
                    System.out.println("[GUI] The level must be greater than 0 and less than 100.");
                    return false;
                }
            } catch (NumberFormatException numberFormatException) {
                System.out.println("[GUI] The level must only be numerical.");
                return false;
            }
        } else if (getRadioButtonTime().isSelected()) {
            TimeElapse time = new TimeElapse(stoppingCondition);
            if (time.getDuration().equals(Duration.ZERO)) {
                System.out.println("[GUI] Incorrect time format - DAYS:HOURS:MINUTES:SECONDS - 00:00:00:00");
                return false;
            }
        }

        if (getRadioButtonAmountToMake().isSelected()) {
            String amountStr = getTextFieldAmountToMake().getText();
            try {
                int amountParsed = Integer.parseInt(amountStr, 10);
                if (amountParsed <= 0 || amountParsed > 2147000000) {
                    System.out.println("[GUI] The amount must be greater than 0 and less than 2147,000,000.");
                    return false;
                }
            } catch (NumberFormatException numberFormatException) {
                System.out.println("[GUI] The amount must only be numerical.");
                return false;
            }
        }

        return true;
    }

    @FXML
    @DoNotRename
    public GUIFX getGui() {
        return gui;
    }

    @FXML
    @DoNotRename
    public void setGui(GUIFX gui) {
        this.gui = gui;
    }

    public Button getButtonCreateWork() {
        return buttonCreateWork;
    }

    public void setButtonCreateWork(Button buttonCreateWork) {
        this.buttonCreateWork = buttonCreateWork;
    }

    public Button getButtonDeleteWork() {
        return buttonDeleteWork;
    }

    public void setButtonDeleteWork(Button buttonDeleteWork) {
        this.buttonDeleteWork = buttonDeleteWork;
    }

    public Button getButtonGenerateRandomLevel() {
        return buttonGenerateRandomLevel;
    }

    public void setButtonGenerateRandomLevel(Button buttonGenerateRandomLevel) {
        this.buttonGenerateRandomLevel = buttonGenerateRandomLevel;
    }

    public Button getButtonGenerateRandomSeed() {
        return buttonGenerateRandomSeed;
    }

    public void setButtonGenerateRandomSeed(Button buttonGenerateRandomSeed) {
        this.buttonGenerateRandomSeed = buttonGenerateRandomSeed;
    }

    public Button getButtonGenerateRandomTime() {
        return buttonGenerateRandomTime;
    }

    public void setButtonGenerateRandomTime(Button buttonGenerateRandomTime) {
        this.buttonGenerateRandomTime = buttonGenerateRandomTime;
    }

    public Button getButtonLoadWork() {
        return buttonLoadWork;
    }

    public void setButtonLoadWork(Button buttonLoadWork) {
        this.buttonLoadWork = buttonLoadWork;
    }

    public Button getButtonSaveWork() {
        return buttonSaveWork;
    }

    public void setButtonSaveWork(Button buttonSaveWork) {
        this.buttonSaveWork = buttonSaveWork;
    }

    public Button getButtonStart() {
        return buttonStart;
    }

    public void setButtonStart(Button buttonStart) {
        this.buttonStart = buttonStart;
    }

    public Button getButtonUpdateWork() {
        return buttonUpdateWork;
    }

    public void setButtonUpdateWork(Button buttonUpdateWork) {
        this.buttonUpdateWork = buttonUpdateWork;
    }

    public CheckBox getCheckBoxFatigueSystem() {
        return checkBoxFatigueSystem;
    }

    public void setCheckBoxFatigueSystem(CheckBox checkBoxFatigueSystem) {
        this.checkBoxFatigueSystem = checkBoxFatigueSystem;
    }

    public CheckBox getCheckBoxMicroSleep() {
        return checkBoxMicroSleep;
    }

    public void setCheckBoxMicroSleep(CheckBox checkBoxMicroSleep) {
        this.checkBoxMicroSleep = checkBoxMicroSleep;
    }

    public CheckBox getCheckBoxRandomWorldHop() {
        return checkBoxRandomWorldHop;
    }

    public void setCheckBoxRandomWorldHop(CheckBox checkBoxRandomWorldHop) {
        this.checkBoxRandomWorldHop = checkBoxRandomWorldHop;
    }

    public CheckBox getCheckBoxSpecificWorld() {
        return checkBoxSpecificWorld;
    }

    public void setCheckBoxSpecificWorld(CheckBox checkBoxSpecificWorld) {
        this.checkBoxSpecificWorld = checkBoxSpecificWorld;
    }

    public CheckBox getCheckBoxWorldHopPlayerCount() {
        return checkBoxWorldHopPlayerCount;
    }

    public void setCheckBoxWorldHopPlayerCount(CheckBox checkBoxWorldHopPlayerCount) {
        this.checkBoxWorldHopPlayerCount = checkBoxWorldHopPlayerCount;
    }

    public ChoiceBox<Resource> getChoiceBoxResource() {
        return choiceBoxResource;
    }

    public void setChoiceBoxResource(ChoiceBox<Resource> choiceBoxResource) {
        this.choiceBoxResource = choiceBoxResource;
    }

    public ChoiceBox<RunescapeBank> getChoiceBoxResourceLocation() {
        return choiceBoxResourceLocation;
    }

    public void setChoiceBoxResourceLocation(ChoiceBox<RunescapeBank> choiceBoxResourceLocation) {
        this.choiceBoxResourceLocation = choiceBoxResourceLocation;
    }

    public ChoiceBox<ResourceOption> getChoiceBoxResourceOption() {
        return choiceBoxResourceOption;
    }

    public void setChoiceBoxResourceOption(ChoiceBox<ResourceOption> choiceBoxResourceOption) {
        this.choiceBoxResourceOption = choiceBoxResourceOption;
    }

    public ChoiceBox<WorkType> getChoiceBoxWorkType() {
        return choiceBoxWorkType;
    }

    public void setChoiceBoxWorkType(ChoiceBox<WorkType> choiceBoxWorkType) {
        this.choiceBoxWorkType = choiceBoxWorkType;
    }

    public TableColumn<Work, Integer> getColLevel() {
        return colLevel;
    }

    public void setColLevel(TableColumn<Work, Integer> colLevel) {
        this.colLevel = colLevel;
    }

    public TableColumn<Work, Resource> getColResource() {
        return colResource;
    }

    public void setColResource(TableColumn<Work, Resource> colResource) {
        this.colResource = colResource;
    }

    public TableColumn<Work, RunescapeBank> getColResourceLocation() {
        return colResourceLocation;
    }

    public void setColResourceLocation(TableColumn<Work, RunescapeBank> colResourceLocation) {
        this.colResourceLocation = colResourceLocation;
    }

    public TableColumn<Work, ResourceOption> getColResourceOption() {
        return colResourceOption;
    }

    public void setColResourceOption(TableColumn<Work, ResourceOption> colResourceOption) {
        this.colResourceOption = colResourceOption;
    }

    public TableColumn<Work, Long> getColSupplies() {
        return colSupplies;
    }

    public void setColSupplies(TableColumn<Work, Long> colSupplies) {
        this.colSupplies = colSupplies;
    }

    public TableColumn<Work, TimeElapse> getColTime() {
        return colTime;
    }

    public void setColTime(TableColumn<Work, TimeElapse> colTime) {
        this.colTime = colTime;
    }

    public Hyperlink getHyperLinkDiscordChannel() {
        return hyperLinkDiscordChannel;
    }

    public void setHyperLinkDiscordChannel(Hyperlink hyperLinkDiscordChannel) {
        this.hyperLinkDiscordChannel = hyperLinkDiscordChannel;
    }

    public Hyperlink getHyperLinkForum() {
        return hyperLinkForum;
    }

    public void setHyperLinkForum(Hyperlink hyperLinkForum) {
        this.hyperLinkForum = hyperLinkForum;
    }

    public Label getLabelFatigueHeader() {
        return labelFatigueHeader;
    }

    public void setLabelFatigueHeader(Label labelFatigueHeader) {
        this.labelFatigueHeader = labelFatigueHeader;
    }

    public Label getLabelFatigueSubHeader() {
        return labelFatigueSubHeader;
    }

    public void setLabelFatigueSubHeader(Label labelFatigueSubHeader) {
        this.labelFatigueSubHeader = labelFatigueSubHeader;
    }

    public Label getLabelFatigueSubHeader2() {
        return labelFatigueSubHeader2;
    }

    public void setLabelFatigueSubHeader2(Label labelFatigueSubHeader2) {
        this.labelFatigueSubHeader2 = labelFatigueSubHeader2;
    }

    public Label getLabelFatigueSubHeader3() {
        return labelFatigueSubHeader3;
    }

    public void setLabelFatigueSubHeader3(Label labelFatigueSubHeader3) {
        this.labelFatigueSubHeader3 = labelFatigueSubHeader3;
    }

    public Label getLabelGrandExchange() {
        return labelGrandExchange;
    }

    public void setLabelGrandExchange(Label labelGrandExchange) {
        this.labelGrandExchange = labelGrandExchange;
    }

    public Label getLabelHeader() {
        return labelHeader;
    }

    public void setLabelHeader(Label labelHeader) {
        this.labelHeader = labelHeader;
    }

    public Label getLabelInstruction() {
        return labelInstruction;
    }

    public void setLabelInstruction(Label labelInstruction) {
        this.labelInstruction = labelInstruction;
    }

    public Label getLabelIronMan() {
        return labelIronMan;
    }

    public void setLabelIronMan(Label labelIronMan) {
        this.labelIronMan = labelIronMan;
    }

    public Label getLabelMicroSleepHeader() {
        return labelMicroSleepHeader;
    }

    public void setLabelMicroSleepHeader(Label labelMicroSleepHeader) {
        this.labelMicroSleepHeader = labelMicroSleepHeader;
    }

    public Label getLabelMicroSleepSubHeader() {
        return labelMicroSleepSubHeader;
    }

    public void setLabelMicroSleepSubHeader(Label labelMicroSleepSubHeader) {
        this.labelMicroSleepSubHeader = labelMicroSleepSubHeader;
    }

    public Label getLabelPatchNote() {
        return labelPatchNote;
    }

    public void setLabelPatchNote(Label labelPatchNote) {
        this.labelPatchNote = labelPatchNote;
    }

    public Label getLabelResource() {
        return labelResource;
    }

    public void setLabelResource(Label labelResource) {
        this.labelResource = labelResource;
    }

    public Label getLabelResourceLocation() {
        return labelResourceLocation;
    }

    public void setLabelResourceLocation(Label labelResourceLocation) {
        this.labelResourceLocation = labelResourceLocation;
    }

    public Label getLabelResourceOption() {
        return labelResourceOption;
    }

    public void setLabelResourceOption(Label labelResourceOption) {
        this.labelResourceOption = labelResourceOption;
    }

    public Label getLabelSeedHeader() {
        return labelSeedHeader;
    }

    public void setLabelSeedHeader(Label labelSeedHeader) {
        this.labelSeedHeader = labelSeedHeader;
    }

    public Label getLabelSeedSubHeader() {
        return labelSeedSubHeader;
    }

    public void setLabelSeedSubHeader(Label labelSeedSubHeader) {
        this.labelSeedSubHeader = labelSeedSubHeader;
    }

    public Label getLabelSeedSubHeader2() {
        return labelSeedSubHeader2;
    }

    public void setLabelSeedSubHeader2(Label labelSeedSubHeader2) {
        this.labelSeedSubHeader2 = labelSeedSubHeader2;
    }

    public Label getLabelSeedSubHeader3() {
        return labelSeedSubHeader3;
    }

    public void setLabelSeedSubHeader3(Label labelSeedSubHeader3) {
        this.labelSeedSubHeader3 = labelSeedSubHeader3;
    }

    public Label getLabelSeedSubSystem() {
        return labelSeedSubSystem;
    }

    public void setLabelSeedSubSystem(Label labelSeedSubSystem) {
        this.labelSeedSubSystem = labelSeedSubSystem;
    }

    public Label getLabelSlogan() {
        return labelSlogan;
    }

    public void setLabelSlogan(Label labelSlogan) {
        this.labelSlogan = labelSlogan;
    }

    public Label getLabelStoppingCondition() {
        return labelStoppingCondition;
    }

    public void setLabelStoppingCondition(Label labelStoppingCondition) {
        this.labelStoppingCondition = labelStoppingCondition;
    }

    public Label getLabelWorkType() {
        return labelWorkType;
    }

    public void setLabelWorkType(Label labelWorkType) {
        this.labelWorkType = labelWorkType;
    }

    public Label getLabelWorldHeader() {
        return labelWorldHeader;
    }

    public void setLabelWorldHeader(Label labelWorldHeader) {
        this.labelWorldHeader = labelWorldHeader;
    }

    public Label getLabelWorldSubHeader() {
        return labelWorldSubHeader;
    }

    public void setLabelWorldSubHeader(Label labelWorldSubHeader) {
        this.labelWorldSubHeader = labelWorldSubHeader;
    }

    public Label getLabelWorldSubHeader2() {
        return labelWorldSubHeader2;
    }

    public void setLabelWorldSubHeader2(Label labelWorldSubHeader2) {
        this.labelWorldSubHeader2 = labelWorldSubHeader2;
    }

    public Label getLabelWorldSubHeader3() {
        return labelWorldSubHeader3;
    }

    public void setLabelWorldSubHeader3(Label labelWorldSubHeader3) {
        this.labelWorldSubHeader3 = labelWorldSubHeader3;
    }

    public RadioButton getRadioButtonAmountToMake() {
        return radioButtonAmountToMake;
    }

    public void setRadioButtonAmountToMake(RadioButton radioButtonAmountToMake) {
        this.radioButtonAmountToMake = radioButtonAmountToMake;
    }

    public RadioButton getRadioButtonDoNotRepeat() {
        return radioButtonDoNotRepeat;
    }

    public void setRadioButtonDoNotRepeat(RadioButton radioButtonDoNotRepeat) {
        this.radioButtonDoNotRepeat = radioButtonDoNotRepeat;
    }

    public RadioButton getRadioButtonLevel() {
        return radioButtonLevel;
    }

    public void setRadioButtonLevel(RadioButton radioButtonLevel) {
        this.radioButtonLevel = radioButtonLevel;
    }

    public RadioButton getRadioButtonRepeat() {
        return radioButtonRepeat;
    }

    public void setRadioButtonRepeat(RadioButton radioButtonRepeat) {
        this.radioButtonRepeat = radioButtonRepeat;
    }

    public RadioButton getRadioButtonShuffleRepeat() {
        return radioButtonShuffleRepeat;
    }

    public void setRadioButtonShuffleRepeat(RadioButton radioButtonShuffleRepeat) {
        this.radioButtonShuffleRepeat = radioButtonShuffleRepeat;
    }

    public RadioButton getRadioButtonTime() {
        return radioButtonTime;
    }

    public void setRadioButtonTime(RadioButton radioButtonTime) {
        this.radioButtonTime = radioButtonTime;
    }

    public ToggleGroup getRepeatGroup() {
        return repeatGroup;
    }

    public void setRepeatGroup(ToggleGroup repeatGroup) {
        this.repeatGroup = repeatGroup;
    }

    public Separator getSeparatorChoiceBox() {
        return separatorChoiceBox;
    }

    public void setSeparatorChoiceBox(Separator separatorChoiceBox) {
        this.separatorChoiceBox = separatorChoiceBox;
    }

    public Separator getSeparatorStoppingCondition() {
        return separatorStoppingCondition;
    }

    public void setSeparatorStoppingCondition(Separator separatorStoppingCondition) {
        this.separatorStoppingCondition = separatorStoppingCondition;
    }

    public Spinner<Integer> getSpinnerWorldHopPlayerCount() {
        return spinnerWorldHopPlayerCount;
    }

    public void setSpinnerWorldHopPlayerCount(Spinner<Integer> spinnerWorldHopPlayerCount) {
        this.spinnerWorldHopPlayerCount = spinnerWorldHopPlayerCount;
    }

    public ToggleGroup getStoppingGroup() {
        return stoppingGroup;
    }

    public void setStoppingGroup(ToggleGroup stoppingGroup) {
        this.stoppingGroup = stoppingGroup;
    }

    public TableView<Work> getTableViewCore() {
        return tableViewCore;
    }

    public void setTableViewCore(TableView<Work> tableViewCore) {
        this.tableViewCore = tableViewCore;
    }

    public TextArea getTextAreaInstruction() {
        return textAreaInstruction;
    }

    public void setTextAreaInstruction(TextArea textAreaInstruction) {
        this.textAreaInstruction = textAreaInstruction;
    }

    public TextArea getTextAreaPatchNote() {
        return textAreaPatchNote;
    }

    public void setTextAreaPatchNote(TextArea textAreaPatchNote) {
        this.textAreaPatchNote = textAreaPatchNote;
    }

    public TextField getTextFieldAmountToMake() {
        return textFieldAmountToMake;
    }

    public void setTextFieldAmountToMake(TextField textFieldAmountToMake) {
        this.textFieldAmountToMake = textFieldAmountToMake;
    }

    public TextField getTextFieldAntiBanSeed() {
        return textFieldAntiBanSeed;
    }

    public void setTextFieldAntiBanSeed(TextField textFieldAntiBanSeed) {
        this.textFieldAntiBanSeed = textFieldAntiBanSeed;
    }

    public TextField getTextFieldSpecificWorld() {
        return textFieldSpecificWorld;
    }

    public void setTextFieldSpecificWorld(TextField textFieldSpecificWorld) {
        this.textFieldSpecificWorld = textFieldSpecificWorld;
    }

    public TextField getTextFieldStoppingCondition() {
        return textFieldStoppingCondition;
    }

    public void setTextFieldStoppingCondition(TextField textFieldStoppingCondition) {
        this.textFieldStoppingCondition = textFieldStoppingCondition;
    }
}

