package UI.SIgnAuth.Auth.Settings.Table;

import UI.SIgnAuth.WHO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.HashMap;

public class Table {
    TableView<TableNode> table;
    ObservableList<TableNode> tableData = FXCollections.observableArrayList(new TableNode("","",""));

    public TableView<TableNode> getTable() {
        return table;
    }

    public Table(){
        table = new TableView<>(tableData);
        TableColumn<TableNode, String> loginCol = new TableColumn<>("Login");
        TableColumn<TableNode, String> passwordColl = new TableColumn<>("Password");
        TableColumn<TableNode, String> whoColl = new TableColumn<>("Who");


        loginCol.setCellValueFactory(new PropertyValueFactory<>("Login"));
        passwordColl.setCellValueFactory(new PropertyValueFactory<>("Password"));
        whoColl.setCellValueFactory(new PropertyValueFactory<>("Who"));
        table.getColumns().add(loginCol);
        table.getColumns().add(passwordColl);
        table.getColumns().add(whoColl);
    }
    public void addRow(HashMap.Entry<String,String> item, WHO who){

        for (TableNode tableDatum : tableData) {

            if (item.getKey().equals(tableDatum.getLogin())) {
                return;
            }
        }

        String whoS = who ==WHO.ADMIN? "Admin" :"root";
        tableData.add(new TableNode(item.getKey(),item.getValue(),whoS));

    }

    public void deleteRow(String login){
        tableData.removeIf(tableDatum -> login.equals(tableDatum.getLogin()));
            }





}
