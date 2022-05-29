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
    //存放利用模块的exp类
    private Class<?> useClazz;

    public void initialize() throws Exception {

        initBottomBar();

        //重定向输出
        consoleText = scanconsole.getChildren();
        scanconsole.setLineSpacing(4);

        ps = new ConsolePrint(consoleText);
        System.setOut(ps);

        // 利用界面控制台的输出重定向
//        consoleText1 = useconsole.getChildren();
//        useconsole.setLineSpacing(4);
//        ps1 = new ConsolePrint(consoleText1);

        //监听tab 切换输出的位置
        mainTab.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if ("利用".equals(mainTab.getSelectionModel().getSelectedItem().getText())) {
                    System.setOut(ps1);
                } else if ("扫描".equals(mainTab.getSelectionModel().getSelectedItem().getText())) {
                    System.setOut(ps);
                }
            }
        });

        helper.SystemStatus();


        //表格绑定到tableTaskData
        tableTaskData = FXCollections.observableArrayList();
        taskTable.setItems(tableTaskData);

        //绑定插件表格
        tablePluginData = FXCollections.observableArrayList();
        pluginTable.setItems(tablePluginData);


        //处理利用界面
        //初始化类型
//        List<String>  payloadType = PayloadHelper.getPayloadType("Plugin/");
//        payloadtype.getItems().addAll(payloadType);

        //初始化 插件界面

        InitPluginTable();


        //将本窗口添加到窗口管理器中
        StageManager.CONTROLLER.put("MainController", this);

        //创建任务控制器，任务队列大小为10
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
        // 获取 tableview 中的数据库类型
        // String databaseType = ((TableTask)this.taskTable.getSelectionModel().getSelectedItem()).getTaskUrl();
        // System.out.println(databaseType);
        Stage newTargetStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/newtask.fxml"));
        newTargetStage.setTitle("-添加任务-");

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
        // 获取 tableview 中的数据库类型
        Task task = ((TableTask) this.taskTable.getSelectionModel().getSelectedItem()).getTaskTask();
        tableTaskData.remove(task.getTaskId());

    }

    @FXML
    void TaskExploit(ActionEvent event) throws ClassNotFoundException, IllegalAccessException, InstantiationException, MalformedURLException {
        // 获取 tableview 中的数据库类型
        String payloadType = ((TableTask) this.taskTable.getSelectionModel().getSelectedItem()).getTaskType();
        String payloadName = ((TableTask) this.taskTable.getSelectionModel().getSelectedItem()).getTaskVul() + ".class";
        mainTab.getSelectionModel().select(tabUse);
        payloadtype.setValue(payloadType);
        exploit.setValue(payloadName);


    }


    @FXML
    void VulInfo(ActionEvent event) {
        // 获取选中的任务
        Task task = ((TableTask) this.taskTable.getSelectionModel().getSelectedItem()).getTaskTask();
        AlertTask(task);
    }


    public void selectType(ActionEvent event) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, MalformedURLException {
        String targetType = (String) payloadtype.getValue();
        // 获取payload
        ArrayList<String> payloads = getPayload("Plugin/" + targetType + "/payloads");
        exploit.getItems().setAll();
        for (int i = 0; i < payloads.size(); i++) {
            exploit.getItems().addAll(payloads.get(i));
        }
    }


    public void selectExploit(ActionEvent event) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        String targetType = (String) payloadtype.getValue();
        String exploitName = (String) exploit.getValue();

        // 获取payload
        ArrayList<URL> urllist = new ArrayList<>();
        File file = new File("Plugin/" + targetType + "/payloads");
        URL url = file.toURI().toURL();
        urllist.add(url);
        ArrayList<String> jars = getJar("Plugin/" + targetType + "/libs");
        for (String jar : jars) {
            urllist.add((new File("Plugin/" + targetType + "/libs/" + jar)).toURI().toURL());
        }
        URL[] urls = urllist.toArray(new URL[urllist.size()]);
        ClassLoader loader = new URLClassLoader(urls);//创建类载入器


        useClazz = loader.loadClass(exploitName.split("\\.")[0]);
        Object obj = useClazz.newInstance();
        //获取POC的info
        HashMap<String, String> hm = new HashMap<>();   //存放属性对
        Field[] fields = useClazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getGenericType().toString().equals("class java.lang.String")) {
                field.setAccessible(true);
                hm.put(field.getName(), (String) field.get(obj));
            } else {
                System.out.println(obj.getClass().getName() + "插件属性应该为String类型" + "\r\n");
            }
        }


        //格式化json格式输出
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
        String result = "无";
        try {
            Object obj = useClazz.newInstance();
            Method method = useClazz.getMethod("Exploit", HashMap.class);
            result = (String) method.invoke(obj, argsMaps);
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("EXP运行异常！");
        }
        System.out.println("执行结果：" + result);


    }


    void AlertTask(Task task) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(task.getTaskTarget());
        alert.setHeaderText(task.getTaskName());

        String content = "-漏洞信息-\n";
        HashMap<String, String> payloadInfo = task.getTaskPayloadInfo();
        for (String key : payloadInfo.keySet()) {
            if (!"expArgs".equals(key)) {
                content = content + key + ": " + payloadInfo.get(key) + "\n";
            }
        }
        content = content + "\n\n-扫描结果-\n";
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


    //重定向控制台输入
    public class ConsolePrint extends PrintStream {//可以正常解析utf8和gbk码
        ObservableList console;

        public ConsolePrint(ObservableList console) {
            super(new ByteArrayOutputStream());
            this.console = console;
        }

        @Override
        public void write(byte[] buf, int off, int len) {


            String string = new String(buf, off, len);
            //获取当前时间
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
            //解决 textarea 更新报错的问题 https://www.coder.work/article/861028
            //javafx.application.Platform.rLater( () -> console.appendText(s));
            // console.appendText(s);
        }
    }


}
