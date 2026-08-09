package org.Hieke;

import java.util.Optional;

public class ChartModificationController {

    private final ChartEditor editor;
    private final Runnable refresh;


    public ChartModificationController(
            ChartEditor editor,
            Runnable refresh
    ) {

        this.editor = editor;
        this.refresh = refresh;

    }


    public void modifyChart() {

        Optional<ChartResizeDialog> result =
                ChartResizeDialog.show();


        result.ifPresent(request -> {

            int amount =
                    request.getAmount();


            if (request.getAction()
                    == ChartResizeDialog.Action.INSERT) {


                if (request.getType()
                        == ChartResizeDialog.Type.ROW) {


                    editor.insertRows(
                            editor.getChart().getRows(),
                            amount
                    );

                }
                else {


                    editor.insertColumns(
                            editor.getChart().getColumns(),
                            amount
                    );

                }

            }
            else {


                if (request.getType()
                        == ChartResizeDialog.Type.ROW) {


                    editor.deleteRows(
                            editor.getChart().getRows() - amount,
                            editor.getChart().getRows() - 1
                    );

                }
                else {


                    editor.deleteColumns(
                            editor.getChart().getColumns() - amount,
                            editor.getChart().getColumns() - 1
                    );

                }

            }


            refresh.run();

        });

    }

    public void addRow() {

        editor.insertRows(
                editor.getChart().getRows(),
                1
        );

        refresh.run();

    }

    public void addColumn() {

        editor.insertColumns(
                editor.getChart().getColumns(),
                1
        );

        refresh.run();

    }

}