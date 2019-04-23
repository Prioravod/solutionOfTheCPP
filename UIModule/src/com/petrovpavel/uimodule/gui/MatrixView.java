package com.petrovpavel.uimodule.gui;


import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class MatrixView extends TableView<ArrayList<Integer>> {

    private final static int PREF_COLUMN_WIDTH = 40;

    private ObservableList<ArrayList<Integer>> adjMatrixList;

    public MatrixView() {
        this.setEditable(true);
        adjMatrixList = FXCollections.observableArrayList();
    }

    public MatrixView(int[][] adjMatrix) {
        this();
        initMatrix(adjMatrix);
    }


    public int[][] getAdjMatrix() {
        return convertMatrix(adjMatrixList);
    }


    private TableColumn<ArrayList<Integer>, Integer> createColumn(int columnIndex) {

        if (columnIndex == 0) {
            return initLayoutColumn();
        }

        TableColumn<ArrayList<Integer>, Integer> tableColumn = new TableColumn<>(String.valueOf(columnIndex));

        tableColumn.setSortable(false);
        tableColumn.setEditable(true);
        tableColumn.setPrefWidth(PREF_COLUMN_WIDTH);
        tableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        tableColumn.setOnEditCommit(event -> {
            final Integer newValue = event.getNewValue();
            final Integer value = newValue != null ? newValue : event.getOldValue();
            event.getTableView().getItems().get(event.getTablePosition().getRow()).set(columnIndex, value);
            this.refresh();
        });
        tableColumn.setCellValueFactory(param -> new ObservableValueBase<Integer>() {
            @Override
            public Integer getValue() {
                return param.getValue().get(columnIndex);
            }
        });

        return tableColumn;
    }

    private List<ArrayList<Integer>> convertMatrix(int[][] adjMatrix) {
        List<ArrayList<Integer>> adjMatrixList = new ArrayList<ArrayList<Integer>>();

        for (int i = 0; i < adjMatrix.length; i++) {
            int[] row = adjMatrix[i];
            List<Integer> rowList = Arrays.stream(row).boxed().collect(Collectors.toList());
            rowList.add(0, i + 1);
            adjMatrixList.add(new ArrayList<>(rowList));
        }

        return adjMatrixList;
    }

    private int[][] convertMatrix(List<ArrayList<Integer>> adjMatrixList) {
        int[][] adjMatrix = new int[adjMatrixList.size()][];

        for (int i = 0; i < adjMatrix.length; i++) {
            ArrayList<Integer> row = new ArrayList<>(adjMatrixList.get(i));
            row.remove(0);
            adjMatrix[i] = row.stream().mapToInt(value -> value).toArray();
        }

        return adjMatrix;
    }

    private void initMatrix(int[][] adjMatrix) {
        this.adjMatrixList.addAll(convertMatrix(adjMatrix));
        this.setItems(adjMatrixList);
        initColumns();
    }

    private void initColumns() {
        this.getColumns().clear();
        for (int i = 0; i < adjMatrixList.get(0).size(); i++) {
            this.getColumns().add(i, createColumn(i));
        }
    }

    private TableColumn<ArrayList<Integer>, Integer> initLayoutColumn() {
        TableColumn<ArrayList<Integer>, Integer> tableColumn = new TableColumn<>("№");

        tableColumn.setSortable(false);
        tableColumn.setEditable(false);
        tableColumn.setPrefWidth(PREF_COLUMN_WIDTH);
        tableColumn.setCellValueFactory(param -> new ObservableValueBase<Integer>() {
            @Override
            public Integer getValue() {
                return param.getValue().get(0);
            }
        });

        return tableColumn;
    }

}
