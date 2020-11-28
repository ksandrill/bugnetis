package emris.GameModel.GUI.Table;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Table {
    TableView<TableNode> table;
    ObservableList<TableNode> tableData = FXCollections.observableArrayList();

    public TableView<TableNode> getTable() {
        return table;
    }

    public Table() {
        table = new TableView<>(tableData);
        TableColumn<TableNode, String> name = new TableColumn<>("name");
        TableColumn<TableNode, Integer> score = new TableColumn<>("score");


        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        score.setCellValueFactory(new PropertyValueFactory<>("score"));
        table.getColumns().add(name);
        table.getColumns().add(score);
    }

    public void addRow(String name, int score) {

        for (TableNode tableDatum : tableData) {

            if (name.equals(tableDatum.getName())) {
                return;
            }
        }
        tableData.add(new TableNode(name,score));

    }

    public void deleteRow(String login) {
        tableData.removeIf(tableDatum -> login.equals(tableDatum.getName()));
    }


    public void UpdateScore(String name,int score) {
        for (TableNode tableDatum : tableData) {
            if (name.equals(tableDatum.getName())) {
                tableDatum.setScore(score);
                return;
            }
        }

    }


}
