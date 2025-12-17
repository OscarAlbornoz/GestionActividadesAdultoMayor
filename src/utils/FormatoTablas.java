package utils;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public final class FormatoTablas
{
    private FormatoTablas() {}

    public static void ajustarAnchoColumnas(JTable tabla) {
        TableColumnModel columnModel = tabla.getColumnModel();

        for (int col = 0; col < tabla.getColumnCount(); col++) {

            TableColumn tableColumn = columnModel.getColumn(col);
            int anchoMaximo = 0;

            TableCellRenderer headerRenderer = tableColumn.getHeaderRenderer();
            if (headerRenderer == null) {
                headerRenderer = tabla.getTableHeader().getDefaultRenderer();
            }
            Component headerComp = headerRenderer.getTableCellRendererComponent(tabla, tableColumn.getHeaderValue(), false, false, 0, col);
            anchoMaximo = Math.max(anchoMaximo, headerComp.getPreferredSize().width);

            for (int row = 0; row < tabla.getRowCount(); row++) {
                TableCellRenderer cellRenderer = tabla.getCellRenderer(row, col);
                Component c = tabla.prepareRenderer(cellRenderer, row, col);
                anchoMaximo = Math.max(anchoMaximo, c.getPreferredSize().width);
            }

            tableColumn.setPreferredWidth(anchoMaximo + 10);
        }
    }

    public static void headersNegrita(JTable tabla) {
        javax.swing.table.JTableHeader header = tabla.getTableHeader();
        header.setFont(header.getFont().deriveFont(java.awt.Font.BOLD));
    }
}