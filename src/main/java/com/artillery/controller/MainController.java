package com.artillery.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.artillery.core.Constants;
import com.artillery.entity.TablePlugin;
import com.artillery.entity.TableTask;
import com.artillery.entity.Task;
import com.artillery.util.PayloadHelper;
import com.artillery.util.StageManager;
import com.artillery.util.helper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.artillery.util.PayloadHelper.getJar;
import static com.artillery.util.PayloadHelper.getPayload;


public class MainController {

    public TaskController taskController;

    public ObservableList<TableTask> tableTaskData;

    public ObservableList<TablePlugin> tablePluginData;
    @FXML
    public Label versionLabel;
    @FXML
    public Label authorLabel;
    @FXML
    public Label aboutlauthorabel;
    @FXML
    public Label aboutversionlabel;
    @FXML
    public TableView taskTable;
    @FXML
    public TableView pluginTable;
    @FXML
    public SplitPane scansplitpane;
    @FXML
    public ScrollPane scanscrollpane;
    @FXML
    public TableColumn yyyy;
    @FXML
    public ChoiceBox payloadtype;
    @FXML
    public ChoiceBox exploit;
    public ObservableList consoleText;
    public ObservableList consoleText1;
    private PrintStream ps;
    private PrintStream ps1;
    @FXML
    private TextFlow scanconsole;
    @FXML
    private TextArea exploitTextArea;
    @FXML
    private TextFlow useconsole;
    @FXML
    private TabPane mainTab;
    @FXML
    private Tab tabUse;
    //?????????????????????exp???
    private Class<?> useClazz;

    public void initialize() throws Exception {

        initBottomBar();

        //???????????????
        consoleText = scanconsole.getChildren();
        scanconsole.setLineSpacing(4);

        ps = new ConsolePrint(consoleText);
        System.setOut(ps);

        // ???????????????????????????????????????
//        consoleText1 = useconsole.getChildren();
//        useconsole.setLineSpacing(4);
//        ps1 = new ConsolePrint(consoleText1);

        //??????tab ?????????????????????
        mainTab.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if ("??????".equals(mainTab.getSelectionModel().getSelectedItem().getText())) {
                    System.setOut(ps1);
                } else if ("??????".equals(mainTab.getSelectionModel().getSelectedItem().getText())) {
                    System.setOut(ps);
                }
            }
        });

        helper.SystemStatus();


        //???????????????tableTaskData
        tableTaskData = FXCollections.observableArrayList();
        taskTable.setItems(tableTaskData);

        //??????????????????
        tablePluginData = FXCollections.observableArrayList();
        pluginTable.setItems(tablePluginData);


        //??????????????????
        //???????????????
//        List<String>  payloadType = PayloadHelper.getPayloadType("Plugin/");
//        payloadtype.getItems().addAll(payloadType);

        //????????? ????????????

        InitPluginTable();


        //???????????????????????????????????????
        StageManager.CONTROLLER.put("MainController", this);

        //?????????????????????????????????????????????10
        taskController = new TaskController(10);

    }

    private void initBottomBar() {
        this.versionLabel.setText(String.format(this.versionLabel.getText(), Constants.VERSION));
        this.aboutversionlabel.setText(String.format(this.aboutversionlabel.getText(), Constants.VERSION));
        this.authorLabel.setText(String.format(this.authorLabel.getText(), Constants.AUTHOR));
        this.aboutlauthorabel.setText(String.format(this.aboutlauthorabel.getText(), Constants.AUTHOR));
    }

    @FXML
    void TaskAdd(ActionEvent event) throws IOException {
        // ?????? tableview ?????????????????????
        // String databaseType = ((TableTask)this.taskTable.getSelectionModel().getSelectedItem()).getTaskUrl();
        // System.out.println(databaseType);
        Stage newTargetStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/newtask.fxml"));
        newTargetStage.setTitle("-????????????-");

        Scene scene = new Scene(root);
//        JMetro jMetro = new JMetro(Style.LIGHT);
//        jMetro.setScene(scene);
//        scene.getStylesheets().add(
//                getClass().getResource("/css/win7glass.css")
//                        .toExternalForm());


        //scene.getStylesheets().add(MainScene.class.getResource("/css/jfoenix-components.css").toExternalForm());
        final MenuButton choices = new MenuButton("Obst");
        final List<CheckMenuItem> items = Arrays.asList(
                new CheckMenuItem("Apfel"),
                new CheckMenuItem("Banane"),
                new CheckMenuItem("Birne"),
                new CheckMenuItem("Kiwi")
        );
        choices.getItems().addAll(items);
        ListView<String> selectedItems = new ListView<>();
        for (final CheckMenuItem item : items) {
            item.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue) {
                    selectedItems.getItems().add(item.getText());
                } else {
                    selectedItems.getItems().remove(item.getText());
                }
            });
        }
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(choices);
        borderPane.setCenter(selectedItems);


        newTargetStage.setScene(scene);
        newTargetStage.show();

        StageManager.STAGE.put("newTargetStage", newTargetStage);

    }

    @FXML
    void TaskDelete(ActionEvent event) throws ClassNotFoundException, IllegalAccessException, InstantiationException, MalformedURLException {
        // ?????? tableview ?????????????????????
        Task task = ((TableTask) this.taskTable.getSelectionModel().getSelectedItem()).getTaskTask();
        tableTaskData.remove(task.getTaskId());

    }

    @FXML
    void TaskExploit(ActionEvent event) throws ClassNotFoundException, IllegalAccessException, InstantiationException, MalformedURLException {
        // ?????? tableview ?????????????????????
        String payloadType = ((TableTask) this.taskTable.getSelectionModel().getSelectedItem()).getTaskType();
        String payloadName = ((TableTask) this.taskTable.getSelectionModel().getSelectedItem()).getTaskVul() + ".class";
        mainTab.getSelectionModel().select(tabUse);
        payloadtype.setValue(payloadType);
        exploit.setValue(payloadName);


    }


    @FXML
    void VulInfo(ActionEvent event) {
        // ?????????????????????
        Task task = ((TableTask) this.taskTable.getSelectionModel().getSelectedItem()).getTaskTask();
        AlertTask(task);
    }


    public void selectType(ActionEvent event) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, MalformedURLException {
        String targetType = (String) payloadtype.getValue();
        // ??????payload
        ArrayList<String> payloads = getPayload("Plugin/" + targetType + "/payloads");
        exploit.getItems().setAll();
        for (int i = 0; i < payloads.size(); i++) {
            exploit.getItems().addAll(payloads.get(i));
        }
    }


    public void selectExploit(ActionEvent event) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        String targetType = (String) payloadtype.getValue();
        String exploitName = (String) exploit.getValue();

        // ??????payload
        ArrayList<URL> urllist = new ArrayList<>();
        File file = new File("Plugin/" + targetType + "/payloads");
        URL url = file.toURI().toURL();
        urllist.add(url);
        ArrayList<String> jars = getJar("Plugin/" + targetType + "/libs");
        for (String jar : jars) {
            urllist.add((new File("Plugin/" + targetType + "/libs/" + jar)).toURI().toURL());
        }
        URL[] urls = urllist.toArray(new URL[urllist.size()]);
        ClassLoader loader = new URLClassLoader(urls);//??????????????????


        useClazz = loader.loadClass(exploitName.split("\\.")[0]);
        Object obj = useClazz.newInstance();
        //??????POC???info
        HashMap<String, String> hm = new HashMap<>();   //???????????????
        Field[] fields = useClazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getGenericType().toString().equals("class java.lang.String")) {
                field.setAccessible(true);
                hm.put(field.getName(), (String) field.get(obj));
            } else {
                System.out.println(obj.getClass().getName() + "?????????????????????String??????" + "\r\n");
            }
        }


        //?????????json????????????
        JSONObject object = JSONObject.parseObject(hm.get("expArgs"));
        String pretty = JSON.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
        exploitTextArea.setText("");
        exploitTextArea.appendText(pretty);


    }


    public void runExp(ActionEvent event) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        String targetType = (String) payloadtype.getValue();
        String exploitName = (String) exploit.getValue();
        String exploitArgs = exploitTextArea.getText();
        HashMap<String, String> argsMaps = JSON.parseObject(exploitArgs, HashMap.class);
        String result = "???";
        try {
            Object obj = useClazz.newInstance();
            Method method = useClazz.getMethod("Exploit", HashMap.class);
            result = (String) method.invoke(obj, argsMaps);
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("EXP???????????????");
        }
        System.out.println("???????????????" + result);


    }


    void AlertTask(Task task) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(task.getTaskTarget());
        alert.setHeaderText(task.getTaskName());

        String content = "-????????????-\n";
        HashMap<String, String> payloadInfo = task.getTaskPayloadInfo();
        for (String key : payloadInfo.keySet()) {
            if (!"expArgs".equals(key)) {
                content = content + key + ": " + payloadInfo.get(key) + "\n";
            }
        }
        content = content + "\n\n-????????????-\n";
        content = content + task.getTaskResult();

        alert.setContentText(content);
        // alert.setContentText("I have a great message for you!");

        alert.showAndWait();
    }

    void InitPluginTable() {
        List<String> pluginType = PayloadHelper.getPayloadType("Plugin/");
        int pluginId = 1;
        for (String type : pluginType) {
            ArrayList<String> payloads = getPayload("Plugin/" + type + "/payloads");
            for (String payload : payloads) {
                tablePluginData.add(new TablePlugin(pluginId, payload, type, System.getProperty("user.dir") + "/Plugin/" + type + "/payloads/" + payload));
                pluginId++;
            }
        }
        // System.out.println();
    }


    //????????????????????????
    public class ConsolePrint extends PrintStream {//??????????????????utf8???gbk???
        ObservableList console;

        public ConsolePrint(ObservableList console) {
            super(new ByteArrayOutputStream());
            this.console = console;
        }

        @Override
        public void write(byte[] buf, int off, int len) {


            String string = new String(buf, off, len);
            //??????????????????
            if (!string.equals("\n") && !string.equals("\r\n")) {
                String date = helper.getNowTime();
                Text textDate = new Text(date + "     ");
                textDate.setFont(new Font(11));
                textDate.setFill(Color.rgb(95, 99, 104));
                print(textDate);
            }

            Text text = new Text(string);
            text = new Text(string);
            text.setFont(new Font(14));
            text.setFill(Color.rgb(198, 198, 201));
            print(text);

        }

        //@Override
        public void print(Text text) {
            javafx.application.Platform.runLater(() -> this.console.add(text));
            //?????? textarea ????????????????????? https://www.coder.work/article/861028
            //javafx.application.Platform.rLater( () -> console.appendText(s));
            // console.appendText(s);
        }
    }


}
