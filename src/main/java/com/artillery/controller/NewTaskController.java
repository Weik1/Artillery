package com.artillery.controller;

import com.artillery.entity.TableTask;
import com.artillery.entity.Task;
import com.artillery.util.PayloadHelper;
import com.artillery.util.StageManager;
import com.artillery.util.helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.controlsfx.control.ListSelectionView;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.artillery.util.PayloadHelper.getJar;
import static com.artillery.util.PayloadHelper.getPayload;

public class NewTaskController {
    ObservableList<String> vulTargetItems;
    ObservableList<String> vulSourceItems;
    ObservableList<String> typeItems;
    @FXML
    private TextField urlTextField;
    @FXML
    private TextArea targetTextArea;
    @FXML
    private ChoiceBox typeChoiceBox;
    @FXML
    private ListSelectionView vulListSelectionView;

    public void initialize() {

        // 漏洞选择控件初始化
        vulTargetItems = FXCollections.observableArrayList();
        vulSourceItems = FXCollections.observableArrayList();
        vulListSelectionView.setSourceItems(vulSourceItems);
        vulListSelectionView.setTargetItems(vulTargetItems);
        List<String> payloadType = PayloadHelper.getPayloadType("Plugin/");
        typeChoiceBox.getItems().addAll(payloadType);
    }


    public void addTask(ActionEvent event) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, MalformedURLException {


        // 通过窗口管理器获取主窗口
        MainController mainController = (MainController) StageManager.CONTROLLER.get("MainController");

        // 获取选择的目标类型
        String targetType = (String) typeChoiceBox.getValue();

        ArrayList<URL> urllist = new ArrayList<URL>();

        // 获取选择的漏洞列表
        ArrayList<String> classList = new ArrayList();

        ArrayList<String> payloadtypeList = new ArrayList();
        for (int i = 0; i < vulListSelectionView.getTargetItems().size(); i++) {
            String choicePayload = (String) vulListSelectionView.getTargetItems().get(i);
            //漏洞类型列表
            payloadtypeList.add(choicePayload.split("\\|")[0]);
            classList.add((String) vulListSelectionView.getTargetItems().get(i));
        }


        //引入 依赖jar包构建classloader
        for (String payloadType : payloadtypeList) {
            File file = new File("Plugin/" + payloadType + "/payloads");
            URL url = file.toURI().toURL();
            urllist.add(url);
            ArrayList<String> jars = getJar("Plugin/" + payloadType + "/libs");
            for (String jar : jars) {
                //System.out.println(jar);
                urllist.add((new File("Plugin/" + payloadType + "/libs/" + jar)).toURI().toURL());
            }
        }


        //判断payload是否是jar包类型的，是的话创建类加载器的时候也得引入
        for (String vuln : classList) {
            if (vuln.split("\\.")[1].equals("jar")) {
                urllist.add((new File("Plugin/" + vuln.split("\\|")[0] + "/payloads/" + vuln.split("\\|")[1])).toURI().toURL());
            }
        }


        URL[] urls = urllist.toArray(new URL[urllist.size()]);
        ClassLoader loader = new URLClassLoader(urls);//创建类载入器

        //获取 target
        String targetString = (String) targetTextArea.getText();
        ArrayList<String> targetList = helper.ParseTargetList(targetString);
        //System.out.println(targetList);
        for (String target : targetList) {
            if (target.equals("0")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("警告");
                alert.setHeaderText(null);
                alert.setContentText("Url 格式错误！");
                alert.showAndWait();
                return;
            }


            for (String vuln : classList) {
                System.out.println(vuln);
                String pocClassName = vuln.split("\\|")[1].split("\\.")[0];
                String pocType = vuln.split("\\|")[0];
                Class<?> cls = loader.loadClass(pocClassName);
                Object obj = cls.newInstance();

                //获取POC的info
                HashMap<String, String> hm = new HashMap<String, String>();   //存放属性对
                Field[] fields = cls.getDeclaredFields();
                for (Field field : fields) {
                    if (field.getGenericType().toString().equals("class java.lang.String")) {
                        field.setAccessible(true);
                        hm.put(field.getName(), (String) field.get(obj));
                    }
                }

                // taskId 任务id
                int taskId = mainController.taskController.taskCounts;
                Task task = new Task(taskId, hm.get("name"), target, pocType, cls, hm);
                //添加到任务队列
                System.out.println("[TaskId：" + task.getTaskId() + "]  任务添加成功 " + hm.get("code") + "");
                System.out.println("[TaskId：" + task.getTaskId() + "]  漏洞名称：" + hm.get("name"));
                System.out.println("[TaskId：" + task.getTaskId() + "]  漏洞详情：" + hm.get("descript"));
                mainController.taskController.putTask(task);
                //添加到表格
                mainController.tableTaskData.add(task.getTaskId(), new TableTask(target, pocType, hm.get("code"), helper.getNowTime(), "扫描中", task));
            }
        }
        //target = helper.UrlCheck(target);

        //关闭窗口
        StageManager.STAGE.get("newTargetStage").close();
    }

    public void selectType(ActionEvent event) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, MalformedURLException {
        String targetType = (String) typeChoiceBox.getValue();
        // 获取payload
        ArrayList<String> payloads = getPayload("Plugin/" + targetType + "/payloads");
        for (int i = 0; i < payloads.size(); i++) {
            vulSourceItems.add(targetType + "|" + payloads.get(i));
        }
    }
}
