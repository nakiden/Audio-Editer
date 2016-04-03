package sample.controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.model.MyFile;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.*;

public class Controller{


    public Button btnEcho;
    public Button btnReverse;
    public Button btnSpeed;
    public Button btnReset;
    public Button btnQuite;
    public Button btnLouder;
    public Button btnSplitSave;
    private String filePath = "G://Универ/IPZ/Sound-Editor-master/vivaldi.wav";
    private ObservableList<MyFile> filesData = FXCollections.observableArrayList();
    private Audio audio = null;
    private boolean playing = false;
    //private SoundEditor se = new SoundEditor();

    @FXML
    private TextField filePathField;

    @FXML
    private Button btnStop;

    @FXML
    private Button btnPlay;

    @FXML
    private Button btnSave;

    @FXML
    private void initialize() {
        filePathField.setText(filePath);
    }


    public String getNameWithoutExtension(String name){
        return name.replaceFirst("[.][^.]+$", "");
    }

    private String correctPath(String path){
        if((path.charAt(path.length() - 1) != '/' || path.charAt(path.length() - 1) !='\\')) {
            return path+File.separator;
        }
        return path;
    }

    public void reload(){
        if (playing) {
            audio.stop();
            audio.play();
        }
    }


    public void setDisabled(){

    }
    /*
    * Method will return true if file path is right
    * else will display error window
    */
    public boolean checkPathCorrectness(String path){
        File file = new File(path);
        if (file.exists()){
            Alert alert = new Alert(Alert.AlertType.NONE, "Файл принят", ButtonType.OK);
            alert.showAndWait();
            return true;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR, "Неправильный путь", ButtonType.OK);
        alert.showAndWait();
        return false;
    }

    public Audio setAudio(String path){

        if (checkPathCorrectness(path)) {
            return new Audio(path);
        }
        return null;
    }

    public void reverse() {
        if (audio != null) {
            float[] left = audio.getLeftChannel();
            float[] right = audio.getRightChannel();
            float[] tempLeft = audio.getLeftChannel();
            float[] tempRight = audio.getRightChannel();
            int sizeLeft = left.length - 1;
            int sizeRight = right.length - 1;

            //USED FOR OFF BY ONE DEBUG
            //System.out.println(sizeLeft + "\n" + sizeRight);
            //System.out.println(tempLeft.length + "\n" + tempRight.length);

            //SWEEP IN REVERSE AND STORE INTO TEMP ARRAYS
            for (int i = 0; i <= sizeLeft; i++)
                tempLeft[i] = left[sizeLeft - i];
            for (int i = 1; i <= sizeRight; i++)
                tempRight[i] = right[sizeRight - i];

            //SET CHANNELS AS TEMP ARRAYS
            audio.setLeftChannel(tempLeft);
            audio.setRightChannel(tempRight);

            System.out.println(" Reverse");
            reload();
        }
    }


    public void saveFile(ActionEvent actionEvent){
        if (audio != null) {
            audio.save("G://Универ/");
        }
    }

    public void playFile(ActionEvent actionEvent) {
        if (!playing && audio != null) {
            audio.play();
            playing = true;
        }
    }

    public void stopFile(ActionEvent actionEvent) {
        if (playing || audio != null) {
            playing = false;
            audio.stop();
        }
    }

    public void reset(ActionEvent actionEvent) {
        if (audio != null) {

            if (playing) {
                audio.stop();
            }
            audio = new Audio(filePath);
            System.out.println(" Reset");
        }
    }

    public void applyFile (ActionEvent actionEvent){
        filesData.clear();
        String currentPath = filePathField.getText();
        filePath = correctPath(currentPath);
        audio = setAudio(filePath);
    }

    public void quiet(ActionEvent actionEvent) {
        if (audio != null) {
            float[] left = audio.getLeftChannel();
            float[] right = audio.getRightChannel();

            for (int i = 0; i < left.length; i++) left[i] = left[i] / 2;
            for (int i = 0; i < right.length; i++) right[i] = right[i] / 2;

            audio.setLeftChannel(left);
            audio.setRightChannel(right);

            System.out.println(" Quiet");
            reload();
        }
    }

    public void addEcho(ActionEvent actionEvent) {
        if (audio != null) {
            float[] left = audio.getLeftChannel();
            float[] right = audio.getRightChannel();

            for (int i = 44100 / 8; i < left.length; i++)
                left[i] = (float) .25 * left[i] + (float) .75 * left[i - 44100 / 8];
            for (int i = 44100 / 8; i < right.length; i++)
                right[i] = (float) .25 * right[i] + (float) .75 * right[i - 44100 / 8];

            audio.setLeftChannel(left);
            audio.setRightChannel(right);
            System.out.println(" Echo");
            reload();
        }
    }

    public void faster(ActionEvent actionEvent) {
        if (audio != null) {
            float[] left = audio.getLeftChannel();
            float[] right = audio.getRightChannel();
            float[] tempLeft = new float[left.length];
            float[] tempRight = new float[right.length];
            int nLeft = 0;
            int nRight = 0;
            String speed = JOptionPane.showInputDialog("Enter how many times faster you want it.");
            int multiple = Integer.parseInt(speed);

            //SWEEP. IF POSITION IS DIVISIBLE BY 3, STORE INTO NEW ARRAY
            for (int i = 0; i < left.length; i++) {
                if (0 == i % multiple) {
                    tempLeft[nLeft] = left[i];
                    nLeft++;
                }
            }
            for (int i = 0; i < right.length; i++) {
                if (0 == i % multiple) {
                    tempRight[nRight] = right[i];
                    nRight++;
                }
            }
            audio.setLeftChannel(tempLeft);
            audio.setRightChannel(tempRight);

            System.out.println(" Faster");
            reload();
        }
    }

    public void fasterNH() {
        if (audio != null) {
            float[] left = audio.getLeftChannel();
            float[] right = audio.getRightChannel();
            float[] tempLeft = new float[(left.length / 2)];
            float[] tempRight = new float[(right.length / 2)];
            int nL = 0;
            int nR = 0;

            for (int i = 0; i < tempLeft.length; i++) {
                tempLeft[i] = left[nL];
                if (nL != 0 && nL % 1000 == 0) nL += 1000;
                nL++;
            }
            for (int i = 0; i < tempRight.length; i++) {
                tempRight[i] = right[nR];
                if (nR != 0 && nR % 1000 == 0) nR += 1000;
                nR++;
            }

            audio.setLeftChannel(tempLeft);
            audio.setRightChannel(tempRight);
            System.out.println(" Faster Not Higher");
        }
    }

    public void louder(ActionEvent actionEvent) {
        if (audio != null) {
            float[] left = audio.getLeftChannel();
            float[] right = audio.getRightChannel();

            for (int i = 0; i < left.length; i++) {
                if (left[i] >= 0) left[i] = (float) (Math.pow(left[i], .8));
                else left[i] = (float) (-1 * (Math.pow(Math.abs(left[i]), .8)));
            }
            for (int i = 0; i < right.length; i++) {
                if (right[i] >= 0) right[i] = (float) (Math.pow(right[i], .8));
                else right[i] = (float) (-1 * (Math.pow(Math.abs(right[i]), .8)));
            }

            audio.setLeftChannel(left);
            audio.setRightChannel(right);

            System.out.println(" Louder");
            reload();
        }
    }

    public Button getBtnStop() {
        return btnStop;
    }

    public void setBtnStop(Button btnStop) {
        this.btnStop = btnStop;
    }

    public Button getBtnSave() {
        return btnSave;
    }

    public void setBtnSave(Button btnSave) {
        this.btnSave = btnSave;
    }

    public Button getBtnPlay() {
        return btnPlay;
    }

    public void setBtnPlay(Button btnPlay) {
        this.btnPlay = btnPlay;
    }

    public void splitSave(ActionEvent actionEvent) {
        if (audio != null) {
            audio.splitAndSave("G://");
        }
    }
}
